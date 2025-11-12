package com.saborgourmet.restaurante.service;

import com.saborgourmet.restaurante.model.Cliente;
import com.saborgourmet.restaurante.model.EstadoCliente;
import com.saborgourmet.restaurante.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // Listar todos los clientes
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Listar clientes activos
    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        return clienteRepository.findByEstado(EstadoCliente.ACTIVO);
    }

    // Obtener cliente por ID
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    // Buscar cliente por DNI
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepository.findByDni(dni);
    }

    // Buscar clientes por nombre o apellido
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorNombreOApellido(String texto) {
        return clienteRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
                texto, texto);
    }

    // Crear nuevo cliente
    @Transactional
    public Cliente crear(Cliente cliente) {
        // Verificar que el DNI no exista
        if (clienteRepository.findByDni(cliente.getDni()).isPresent()) {
            throw new RuntimeException("Ya existe un cliente con ese DNI");
        }
        cliente.setEstado(EstadoCliente.ACTIVO);
        return clienteRepository.save(cliente);
    }

    // Actualizar cliente
    @Transactional
    public Cliente actualizar(Long id, Cliente clienteActualizado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Verificar DNI duplicado (si cambió)
        if (!cliente.getDni().equals(clienteActualizado.getDni())) {
            if (clienteRepository.findByDni(clienteActualizado.getDni()).isPresent()) {
                throw new RuntimeException("Ya existe un cliente con ese DNI");
            }
        }

        cliente.setDni(clienteActualizado.getDni());
        cliente.setNombres(clienteActualizado.getNombres());
        cliente.setApellidos(clienteActualizado.getApellidos());
        cliente.setTelefono(clienteActualizado.getTelefono());
        cliente.setCorreo(clienteActualizado.getCorreo());

        return clienteRepository.save(cliente);
    }

    // Cambiar estado del cliente
    @Transactional
    public Cliente cambiarEstado(Long id, EstadoCliente nuevoEstado) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        cliente.setEstado(nuevoEstado);
        return clienteRepository.save(cliente);
    }

    // Eliminar cliente (solo si está inactivo)
    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (cliente.getEstado() == EstadoCliente.ACTIVO) {
            throw new RuntimeException("No se puede eliminar un cliente activo");
        }

        clienteRepository.deleteById(id);
    }
}
