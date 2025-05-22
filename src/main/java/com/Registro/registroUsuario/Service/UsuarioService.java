package com.Registro.registroUsuario.Service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Registro.registroUsuario.Models.RolModel;
import com.Registro.registroUsuario.Models.UsuarioModel;
import com.Registro.registroUsuario.Repository.UsuarioRespo;
import com.Registro.registroUsuario.Repository.UsuarioRolRespo;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRespo usuarioRepository;

    @Autowired
    private UsuarioRolRespo rolRepository;

    public UsuarioModel registrar(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String rut,
            String email,
            String contrasenia,
            String confirmarContrasenia,
            Integer rolId) {

        if (!contrasenia.equals(confirmarContrasenia)) {
            System.out.println("Las contraseñas no coinciden");
            return null;
        }
        if (usuarioRepository.existsByEmail(email)) {
            System.out.println("Ya existe un usuario con este correo");
            return null;
        }

        if (usuarioRepository.existsByRut(rut)) {
            System.out.println("Ya existe un usuario con este RUT");
            return null;
        }
        RolModel rol = rolRepository.findById(rolId).orElse(null);
        if (rol == null) {
            System.out.println("Rol no encontrado");
            return null;
        }
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(nombre);
        usuario.setApellidoPaterno(apellidoPaterno);
        usuario.setApellidoMaterno(apellidoMaterno);
        usuario.setRut(rut);
        usuario.setEmail(email);
        usuario.setContrasenia(contrasenia);
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }
}

