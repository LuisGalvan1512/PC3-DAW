# 🍽️ Sistema de Gestión de Restaurante "Sabor Gourmet"

**Módulo de Clientes y Mesas** - Sistema web desarrollado con Spring Boot para la gestión integral de clientes, mesas y sus asignaciones en el restaurante "Sabor Gourmet".

---

## 📋 Descripción

Sistema que permite:
- ✅ Gestión de clientes (registro, búsqueda, estados)
- ✅ Administración de mesas (disponibilidad, capacidad, estados)
- ✅ Asignación de clientes a mesas (ocupaciones y reservas)
- ✅ Auditoría automática con AOP
- ✅ Control de acceso por roles (ADMIN, MOZO, COCINERO, CAJERO)

---

## 🚀 Tecnologías

- **Java 17** + **Spring Boot 3.5.7**
- **Spring Security 6** + **Spring AOP**
- **JPA/Hibernate** + **MySQL 8.0**
- **Thymeleaf** + **Bootstrap 5**
- **Maven**

---

## ⚙️ Instalación Rápida

### 1. Crear base de datos
CREATE DATABASE sabor_gourmet;

### 2. Configurar `application.properties`
spring.datasource.url=jdbc:mysql://localhost:3306/sabor_gourmet
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update

### 3. Ejecutar
mvn spring-boot:run

### 4. Acceder
http://localhost:8080

---

## 👤 Usuarios de Prueba

| Usuario | Contraseña | Rol | Accesos |
|---------|------------|-----|---------|
| admin | password123 | ADMIN | Acceso total |
| mozo1 | password123 | MOZO | Clientes, Mesas, Asignaciones |
| cocinero1 | password123 | COCINERO | Solo Pedidos |
| cajero1 | password123 | CAJERO | Solo Ventas |

---

## 🗄️ Entidades Principales

### Cliente
- DNI (único), nombres, apellidos, teléfono, correo
- Estado: **ACTIVO** / **INACTIVO**

### Mesa
- Número (único), capacidad
- Estado: **DISPONIBLE** / **OCUPADA** / **RESERVADA** / **MANTENIMIENTO**

### Asignacion
- Cliente + Mesa + Tipo (OCUPACIÓN/RESERVA)
- Fecha asignación/liberación

### Auditoria (AOP)
- Registro automático de todas las operaciones CRUD
- Usuario, fecha/hora, método, tiempo de ejecución

---

## 🔄 Flujo de Uso

1. **Registrar Cliente** → `/clientes/nuevo`
2. **Asignar Mesa** → `/asignaciones/nueva` → Seleccionar cliente y mesa → OCUPAR
3. **Mesa cambia automáticamente** a OCUPADA
4. **Liberar Mesa** → `/asignaciones` → Liberar → Mesa vuelve a DISPONIBLE

---

## 📊 APIs REST

### Clientes
GET /api/clientes Listar todos
GET /api/clientes/activos Solo activos
POST /api/clientes Crear
PUT /api/clientes/{id} Actualizar
DELETE /api/clientes/{id} Eliminar

### Mesas
GET /api/mesas Listar todas
GET /api/mesas/disponibles Solo disponibles
POST /api/mesas Crear
PUT /api/mesas/{id} Actualizar

### Asignaciones
GET /api/asignaciones/activas Ver activas
POST /api/asignaciones/asignar Asignar mesa
PUT /api/asignaciones/liberar/{id} Liberar

---

## 🛡️ Características de Seguridad

- **Autenticación:** Spring Security con BCrypt
- **Autorización:** Control por roles
- **Auditoría AOP:** Registro automático de operaciones
- **Soft Delete:** Clientes inactivos en lugar de eliminación permanente

---

## 📁 Estructura del Proyecto

src/main/java/com/saborgourmet/restaurante/
├── aspect/ # AOP (Auditoría, Logging)
├── config/ # Security, DataInitializer
├── controller/ # REST y View Controllers
├── model/ # Entidades JPA
├── repository/ # Repositorios JPA
└── service/ # Lógica de negocio

src/main/resources/
├── templates/ # Vistas Thymeleaf
│ ├── clientes/
│ ├── mesas/
│ ├── asignaciones/
│ └── auditoria/
└── application.properties


---

## 🎯 Funcionalidades Implementadas

✅ CRUD completo de Clientes y Mesas  
✅ Asignación Cliente-Mesa con validaciones  
✅ Estados y disponibilidad en tiempo real  
✅ Auditoría automática con AOP  
✅ Control de acceso por roles  
✅ API REST completa  
✅ Interfaz web responsive  
✅ Validaciones robustas  

---

## 👨‍💻 Autor

**Luis Enrique Galván Morales**  
Proyecto académico - Desarrollo de Aplicaciones Web Avanzado

---

## 📄 Licencia

Proyecto educativo desarrollado con fines académicos.
