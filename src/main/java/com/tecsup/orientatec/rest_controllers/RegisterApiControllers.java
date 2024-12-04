package com.tecsup.orientatec.rest_controllers;

import com.tecsup.orientatec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
public class RegisterApiControllers {
    private static final Logger logger = LoggerFactory.getLogger(RegisterApiControllers.class);

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Map<String, String> userData) {
        logger.info("Iniciando proceso de registro - Email: {}", userData.get("email"));

        Map<String, String> response = new HashMap<>();

        try {
            // Validación de campos requeridos
            String nombreCompleto = userData.get("nombre_completo");
            String email = userData.get("email");
            String contraseña = userData.get("contraseña");

            if (nombreCompleto == null || email == null || contraseña == null ||
                    nombreCompleto.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
                logger.warn("Campos incompletos en el registro");
                response.put("status", "error");
                response.put("message", "Por favor complete todos los campos");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Validación de formato de email
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                logger.warn("Formato de email inválido: {}", email);
                response.put("status", "error");
                response.put("message", "Formato de email inválido");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Verificar si el email ya existe
            if (!userService.checkUserEmail(email).isEmpty()) {
                logger.warn("Intento de registro con email existente: {}", email);
                response.put("status", "error");
                response.put("message", "El email ya está registrado");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Encriptar contraseña
            String hashedPassword = BCrypt.hashpw(contraseña, BCrypt.gensalt());

            // Registrar usuario
            int result = userService.registerNewUserServiceMethod(nombreCompleto, email, hashedPassword);

            if (result == 1) {
                logger.info("Usuario registrado exitosamente - Email: {}", email);
                response.put("status", "success");
                response.put("message", "Usuario registrado exitosamente");
                return ResponseEntity.ok(response);
            } else {
                logger.error("Error al registrar usuario - Email: {}", email);
                response.put("status", "error");
                response.put("message", "Error al registrar usuario");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

        } catch (Exception e) {
            logger.error("Error en el proceso de registro: ", e);
            response.put("status", "error");
            response.put("message", "Error del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Manejador de solicitudes OPTIONS
    @RequestMapping(value = "/user/register", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity
                .ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, OPTIONS")
                .header("Access-Control-Allow-Headers", "*")
                .build();
    }
}
