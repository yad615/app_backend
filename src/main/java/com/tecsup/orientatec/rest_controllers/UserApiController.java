package com.tecsup.orientatec.rest_controllers;

import com.tecsup.orientatec.models.Usuario;
import com.tecsup.orientatec.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class UserApiController {
    private static final Logger logger = LoggerFactory.getLogger(UserApiController.class);

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        try {
            List<Usuario> usuarios = userService.getAllUsers();
            if (usuarios.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "No hay usuarios registrados");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(usuarios, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al obtener usuarios: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al obtener usuarios: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            Optional<Usuario> user = userService.getUserById(id);
            if (!user.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al obtener usuario: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al obtener usuario: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody Usuario usuario) {
        try {
            if (!userService.checkUserEmail(usuario.getEmail()).isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "El email ya está registrado");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            usuario.setFecha_registro(new Date());

            int result = userService.registerNewUserServiceMethod(
                    usuario.getNombre_completo(),
                    usuario.getEmail(),
                    usuario.getContraseña()
            );

            if (result > 0) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "success");
                response.put("message", "Usuario registrado exitosamente");
                return new ResponseEntity<>(response, HttpStatus.CREATED);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "No se pudo registrar el usuario");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            logger.error("Error al crear usuario: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al crear usuario: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable int id, @RequestBody Usuario usuario) {
        try {
            Optional<Usuario> existingUser = userService.getUserById(id);
            if (!existingUser.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            usuario.setIdusuario(id);
            usuario.setFecha_actualizacion(new Date());
            Usuario updatedUsuario = userService.updateUser(usuario);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Usuario actualizado exitosamente");
            response.put("user", updatedUsuario);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al actualizar usuario: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al actualizar usuario: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            Optional<Usuario> existingUser = userService.getUserById(id);
            if (!existingUser.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            userService.deleteUser(id);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Usuario eliminado exitosamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error al eliminar usuario: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("contraseña");

            if (email == null || password == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Email y contraseña son requeridos");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            List<String> userEmails = userService.checkUserEmail(email);
            if (userEmails.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Usuario no encontrado");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            String storedPassword = userService.checkUserPasswordByEmail(email);
            if (!password.equals(storedPassword)) {
                Map<String, Object> response = new HashMap<>();
                response.put("status", "error");
                response.put("message", "Contraseña incorrecta");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }

            Usuario usuario = userService.getUserDetailsByEmail(email);
            return new ResponseEntity<>(usuario, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error en el login: ", e);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", "Error en el login: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testEndpoint() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Test endpoint is working");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
