package com.task.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TaskFlow - Sistema de Gestión de Tareas y Proyectos
 * 
 * MEJORAS IMPLEMENTADAS:
 * 
 * MÓDULO 1: AUTENTICACIÓN Y SEGURIDAD
 * - HU-001: Registro de usuarios con validación
 * - HU-002: Inicio de sesión con JWT
 * - HU-003: Recuperación de contraseña
 * - HU-004: Cierre de sesión (frontend)
 * - HU-005: Asignación de roles (EMPLEADO, GERENTE_PROYECTO)
 * 
 * MÓDULO 2: GESTIÓN DE PROYECTOS Y TAREAS
 * - HU-006 a HU-013: CRUD completo de proyectos y tareas
 * - Asignación de tareas a empleados
 * - Actualización de estados de tareas
 * 
 * MÓDULO 3: NOTIFICACIONES Y ALERTAS
 * - HU-014: Sistema de notificaciones
 * - HU-015: Alertas de vencimiento y retraso
 * 
 * MÓDULO 4: REPORTES Y ANÁLISIS
 * - HU-016: Listar proyectos con estadísticas
 * - HU-017: Listar tareas con filtros
 * - HU-018: Reportes de productividad por empleado
 * 
 * TECNOLOGÍAS:
 * - Spring Boot 3.5.7 + Spring Security
 * - JWT para autenticación
 * - MongoDB para persistencia
 * - BCrypt para encriptación de contraseñas
 */
@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

}
