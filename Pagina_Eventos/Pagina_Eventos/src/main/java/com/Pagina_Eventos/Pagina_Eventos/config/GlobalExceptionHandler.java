package com.Pagina_Eventos.Pagina_Eventos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación
 * Captura errores comunes y devuelve respuestas JSON amigables
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de integridad de datos (duplicados, restricciones, etc.)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        String message = ex.getMessage();
        String userMessage = "Error al guardar: Datos duplicados o inválidos";

        // Detectar el tipo específico de error
        if (message != null) {
            if (message.contains("Duplicate entry") && message.contains("dni")) {
                userMessage = "El DNI ya está registrado. Por favor, use un DNI diferente.";
                errorResponse.put("field", "dni");
            } else if (message.contains("Duplicate entry") && message.contains("email")) {
                userMessage = "El email ya está registrado. Por favor, use un email diferente.";
                errorResponse.put("field", "email");
            } else if (message.contains("Duplicate entry") && message.contains("nombreusuario")) {
                userMessage = "El nombre de usuario ya está en uso. Por favor, elija otro.";
                errorResponse.put("field", "nombreusuario");
            } else if (message.contains("Duplicate entry")) {
                userMessage = "El registro ya existe en el sistema.";
                errorResponse.put("field", "unknown");
            } else if (message.contains("cannot be null")) {
                userMessage = "Faltan campos obligatorios. Por favor, complete todos los datos requeridos.";
            } else if (message.contains("Data too long")) {
                userMessage = "Uno o más campos exceden la longitud máxima permitida.";
            }
        }

        errorResponse.put("error", "Constraint Violation");
        errorResponse.put("message", userMessage);
        errorResponse.put("status", HttpStatus.CONFLICT.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Maneja errores generales no capturados
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "Ocurrió un error inesperado. Por favor, intente nuevamente.");
        errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        // Log del error
        logger.error("Error no manejado: {}", ex.getMessage(), ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Maneja errores de argumentos inválidos
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("error", "Invalid Argument");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

