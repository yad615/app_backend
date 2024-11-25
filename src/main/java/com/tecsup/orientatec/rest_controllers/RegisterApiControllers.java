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
@CrossOrigin(origins = "*")

public class RegisterApiControllers {
    private static final Logger logger = LoggerFactory.getLogger(RegisterApiControllers.class);

    @Autowired
    UserService userService;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerNewUser(@RequestBody Map<String, String> userData) {
        logger.info("Recibiendo solicitud de registro - Email: {}", userData.get("email"));

        String nombreCompleto = userData.get("nombre_completo");
        String email = userData.get("email");
        String contraseña = userData.get("contraseña");

        Map<String, String> response = new HashMap<>();

        if(nombreCompleto == null || email == null || contraseña == null ||
                nombreCompleto.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
            response.put("status", "error");
            response.put("message", "Por favor complete todos los campos");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Validar formato de email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            response.put("status", "error");
            response.put("message", "Formato de email inválido");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Verificar si el email ya existe
        try {
            if (!userService.checkUserEmail(email).isEmpty()) {
                response.put("status", "error");
                response.put("message", "El email ya está registrado");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            logger.error("Error al verificar email existente: ", e);
        }

        String hashedPassword = BCrypt.hashpw(contraseña, BCrypt.gensalt());

        try {
            int result = userService.registerNewUserServiceMethod(nombreCompleto, email, hashedPassword);
            if (result == 1) {
                response.put("status", "success");
                response.put("message", "Usuario registrado exitosamente");
                logger.info("Usuario registrado exitosamente - Email: {}", email);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("status", "error");
                response.put("message", "Error al registrar usuario");
                logger.error("Error al registrar usuario - Email: {}", email);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error en el registro: ", e);
            response.put("status", "error");
            response.put("message", "Error del servidor: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
