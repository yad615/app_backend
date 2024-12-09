package com.tecsup.orientatec.services;

import com.tecsup.orientatec.models.Usuario;
import com.tecsup.orientatec.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<Usuario> getAllUsers() {
        return (List<Usuario>) userRepository.findAll();
    }

    public Optional<Usuario> getUserById(int id) {
        return userRepository.findById(id);
    }

    public int registerNewUserServiceMethod(String nombre_completo, String email, String contraseña) {
        return userRepository.registerNewUser(nombre_completo, email, contraseña);
    }

    public List<String> checkUserEmail(String email) {
        return userRepository.checkUserEmail(email);
    }

    public String checkUserPasswordByEmail(String email) {
        return userRepository.checkUserPasswordByEmail(email);
    }

    public Usuario getUserDetailsByEmail(String email) {
        return userRepository.GetUserDetailsByEmail(email);
    }

    public Usuario updateUser(Usuario usuario) {
        return userRepository.save(usuario);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }
}
