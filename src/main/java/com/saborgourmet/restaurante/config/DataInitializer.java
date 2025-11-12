package com.saborgourmet.restaurante.config;

import com.saborgourmet.restaurante.model.*;
import com.saborgourmet.restaurante.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository,
                                   ClienteRepository clienteRepository,
                                   MesaRepository mesaRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // Solo insertar si no hay usuarios
            if (usuarioRepository.count() == 0) {
                // Crear usuarios
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("password123"));
                admin.setNombreCompleto("Administrador Principal");
                admin.setRol(Rol.ADMIN);
                admin.setActivo(true);
                usuarioRepository.save(admin);

                Usuario mozo = new Usuario();
                mozo.setUsername("mozo1");
                mozo.setPassword(passwordEncoder.encode("password123"));
                mozo.setNombreCompleto("Juan Pérez (Mozo)");
                mozo.setRol(Rol.MOZO);
                mozo.setActivo(true);
                usuarioRepository.save(mozo);

                Usuario cocinero = new Usuario();
                cocinero.setUsername("cocinero1");
                cocinero.setPassword(passwordEncoder.encode("password123"));
                cocinero.setNombreCompleto("María García (Cocinera)");
                cocinero.setRol(Rol.COCINERO);
                cocinero.setActivo(true);
                usuarioRepository.save(cocinero);

                Usuario cajero = new Usuario();
                cajero.setUsername("cajero1");
                cajero.setPassword(passwordEncoder.encode("password123"));
                cajero.setNombreCompleto("Carlos López (Cajero)");
                cajero.setRol(Rol.CAJERO);
                cajero.setActivo(true);
                usuarioRepository.save(cajero);

                System.out.println("✅ Usuarios creados correctamente");
            }

            // Solo insertar si no hay clientes
            if (clienteRepository.count() == 0) {
                clienteRepository.save(new Cliente(null, "12345678", "Luis", "Martínez",
                        "987654321", "luis@email.com", EstadoCliente.ACTIVO));
                clienteRepository.save(new Cliente(null, "87654321", "Ana", "Rodríguez",
                        "912345678", "ana@email.com", EstadoCliente.ACTIVO));
                clienteRepository.save(new Cliente(null, "11223344", "Pedro", "Sánchez",
                        "999888777", "pedro@email.com", EstadoCliente.ACTIVO));

                System.out.println("✅ Clientes creados correctamente");
            }

            // Solo insertar si no hay mesas
            if (mesaRepository.count() == 0) {
                mesaRepository.save(new Mesa(null, 1, 2, EstadoMesa.DISPONIBLE));
                mesaRepository.save(new Mesa(null, 2, 4, EstadoMesa.DISPONIBLE));
                mesaRepository.save(new Mesa(null, 3, 4, EstadoMesa.DISPONIBLE));
                mesaRepository.save(new Mesa(null, 4, 6, EstadoMesa.DISPONIBLE));
                mesaRepository.save(new Mesa(null, 5, 8, EstadoMesa.DISPONIBLE));
                mesaRepository.save(new Mesa(null, 10, 2, EstadoMesa.OCUPADA));
                mesaRepository.save(new Mesa(null, 11, 4, EstadoMesa.RESERVADA));

                System.out.println("✅ Mesas creadas correctamente");
            }
        };
    }
}
