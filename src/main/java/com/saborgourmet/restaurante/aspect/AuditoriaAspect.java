package com.saborgourmet.restaurante.aspect;

import com.saborgourmet.restaurante.service.AuditoriaService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditoriaAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditoriaAspect.class);
    private final AuditoriaService auditoriaService;

    // Pointcut: intercepta todos los métodos CRUD de ClienteService
    @Pointcut("execution(* com.saborgourmet.restaurante.service.ClienteService.crear(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.ClienteService.actualizar(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.ClienteService.eliminar(..))")
    public void clienteOperaciones() {}

    // Pointcut: intercepta todos los métodos CRUD de MesaService
    @Pointcut("execution(* com.saborgourmet.restaurante.service.MesaService.crear(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.MesaService.actualizar(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.MesaService.eliminar(..))")
    public void mesaOperaciones() {}

    // Pointcut: intercepta todos los métodos de AsignacionService
    @Pointcut("execution(* com.saborgourmet.restaurante.service.AsignacionService.asignarMesa(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.AsignacionService.crearReserva(..)) || " +
            "execution(* com.saborgourmet.restaurante.service.AsignacionService.liberarMesa(..))")
    public void asignacionOperaciones() {}

    // Advice: ejecuta antes y después de las operaciones de Cliente
    @Around("clienteOperaciones()")
    public Object auditarCliente(ProceedingJoinPoint joinPoint) throws Throwable {
        return auditarOperacion(joinPoint, "Cliente");
    }

    // Advice: ejecuta antes y después de las operaciones de Mesa
    @Around("mesaOperaciones()")
    public Object auditarMesa(ProceedingJoinPoint joinPoint) throws Throwable {
        return auditarOperacion(joinPoint, "Mesa");
    }

    // Advice: ejecuta antes y después de las operaciones de Asignación
    @Around("asignacionOperaciones()")
    public Object auditarAsignacion(ProceedingJoinPoint joinPoint) throws Throwable {
        return auditarOperacion(joinPoint, "Asignacion");
    }

    // Método auxiliar para auditar operaciones
    private Object auditarOperacion(ProceedingJoinPoint joinPoint, String entidad) throws Throwable {
        String metodo = joinPoint.getSignature().getName();
        String operacion = determinarOperacion(metodo);
        String usuario = obtenerUsuarioActual();

        // Obtener argumentos del método
        Object[] args = joinPoint.getArgs();
        String argumentos = Arrays.toString(args);

        logger.info("=== AUDITORÍA: {} iniciando {} en {} por usuario {} ===",
                operacion, metodo, entidad, usuario);

        long inicio = System.currentTimeMillis();
        Object resultado = null;
        boolean exitoso = true;
        String detalles = "";

        try {
            // Ejecutar el método original
            resultado = joinPoint.proceed();

            // Construir detalles de éxito
            detalles = String.format("Operación exitosa - Usuario: %s, Método: %s, Argumentos: %s",
                    usuario, metodo, argumentos);

            logger.info("=== AUDITORÍA: {} en {} completado exitosamente ===", operacion, entidad);

        } catch (Exception e) {
            exitoso = false;
            detalles = String.format("Operación fallida - Usuario: %s, Método: %s, Error: %s",
                    usuario, metodo, e.getMessage());
            logger.error("=== AUDITORÍA: Error en {} de {} - {} ===", operacion, entidad, e.getMessage());
            throw e;

        } finally {
            long tiempoEjecucion = System.currentTimeMillis() - inicio;

            // Registrar en la base de datos
            try {
                auditoriaService.registrarConUsuario(entidad, operacion, metodo, detalles, tiempoEjecucion, usuario);
            } catch (Exception e) {
                logger.error("Error al registrar auditoría: {}", e.getMessage());
            }

            logger.info("=== AUDITORÍA: Tiempo de ejecución: {} ms ===", tiempoEjecucion);
        }

        return resultado;
    }

    // Determinar el tipo de operación según el nombre del método
    private String determinarOperacion(String metodo) {
        if (metodo.startsWith("crear") || metodo.startsWith("asignar")) {
            return "CREAR";
        } else if (metodo.startsWith("actualizar")) {
            return "ACTUALIZAR";
        } else if (metodo.startsWith("eliminar") || metodo.startsWith("liberar")) {
            return "ELIMINAR";
        } else {
            return "OTRA";
        }
    }

    // Obtener el usuario autenticado actualmente
    private String obtenerUsuarioActual() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication.getName();
            }
        } catch (Exception e) {
            logger.warn("No se pudo obtener el usuario actual: {}", e.getMessage());
        }
        return "SYSTEM";
    }
}
