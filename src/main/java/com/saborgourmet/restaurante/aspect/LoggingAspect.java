package com.saborgourmet.restaurante.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Pointcut: intercepta TODOS los métodos públicos de los servicios
    @Pointcut("execution(public * com.saborgourmet.restaurante.service.*.*(..))")
    public void serviceMethods() {}

    // Advice: mide el tiempo de ejecución de todos los métodos de servicio
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String nombreClase = joinPoint.getSignature().getDeclaringTypeName();
        String nombreMetodo = joinPoint.getSignature().getName();

        logger.info("→ Ejecutando: {}.{}", nombreClase, nombreMetodo);

        long inicio = System.currentTimeMillis();

        Object resultado = joinPoint.proceed();

        long tiempoEjecucion = System.currentTimeMillis() - inicio;

        logger.info("← Completado: {}.{} en {} ms", nombreClase, nombreMetodo, tiempoEjecucion);

        return resultado;
    }
}
