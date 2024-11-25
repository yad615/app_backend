package com.tecsup.orientatec.rest_controllers;

import com.tecsup.orientatec.models.Login;
import com.tecsup.orientatec.models.User;
import com.tecsup.orientatec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class LoginApiController {
    private static final Logger logger = LoggerFactory.getLogger(LoginApiController.class);

    @Autowired
    UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Login login) {
        try {
            logger.info("Intento de login - Email: {}", login.getEmail());

            if (login.getEmail() == null || login.getContraseña() == null ||
                    login.getEmail().isEmpty() || login.getContraseña().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Email y contraseña son requeridos");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<String> userEmail = userService.checkUserEmail(login.getEmail());
            Map<String, Object> response = new HashMap<>();

            if (userEmail.isEmpty()) {
                logger.warn("Email no encontrado: {}", login.getEmail());
                response.put("status", "error");
                response.put("message", "El Email que ingresó no existe");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            String hashedPassword = userService.checkUserPasswordByEmail(login.getEmail());

            if (!BCrypt.checkpw(login.getContraseña(), hashedPassword)) {
                logger.warn("Contraseña incorrecta para el email: {}", login.getEmail());
                response.put("status", "error");
                response.put("message", "El email o la Contraseña es Incorrecta");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            User user = userService.getUserDetailsByEmail(login.getEmail());
            logger.info("Login exitoso - Usuario: {}", user.getEmail());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error durante la autenticación: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error del servidor: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
