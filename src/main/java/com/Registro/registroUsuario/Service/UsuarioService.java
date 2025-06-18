package com.Registro.registroUsuario.Service;

import com.Registro.registroUsuario.Models.*;
import com.Registro.registroUsuario.Repository.*;
import com.Registro.registroUsuario.DTO.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRespo usuarioRepository;
    private final UsuarioRolRespo rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioRegistroDTO crearUsuario(UsuarioCreateDTO dto) {
        if (!dto.esValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Datos inválidos");
        }
        RolModel rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rol no encontrado"));

        UsuarioModel usuario = new UsuarioModel();
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());
        usuario.setRut(dto.getRut());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        usuario.setFechaRegistro(LocalDate.now());
        usuario.setRol(rol);
        usuario.setActivo(true);

        UsuarioModel guardado = usuarioRepository.save(usuario);
        return mapToDTO(guardado);
    }

    public LoginResponse login(LoginRequest request) {
        Optional<UsuarioModel> usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        if (usuarioOpt.isEmpty()
                || !passwordEncoder.matches(request.getContrasenia(), usuarioOpt.get().getContrasenia())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }
        return new LoginResponse("Usuario ingresado", "Inicio exitoso");
    }

    public UsuarioRegistroDTO obtenerUsuario(long id) {
        UsuarioModel u = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return mapToDTO(u);
    }

    public UsuarioRegistroDTO actualizarUsuario(long id, UsuarioCreateDTO dto) {
        UsuarioModel existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        existente.setNombre(dto.getNombre());
        existente.setApellidoPaterno(dto.getApellidoPaterno());
        existente.setApellidoMaterno(dto.getApellidoMaterno());
        existente.setContrasenia(passwordEncoder.encode(dto.getContrasenia()));
        return mapToDTO(usuarioRepository.save(existente));
    }

    public void eliminarUsuario(long id) {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private UsuarioRegistroDTO mapToDTO(UsuarioModel usuario) {
        return new UsuarioRegistroDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidoPaterno(),
                usuario.getApellidoMaterno(),
                usuario.getRut(),
                usuario.getEmail(),
                usuario.getFechaRegistro(),
                usuario.getRol().getNombre(),
                usuario.getRol().getPermisos().stream().map(PermisoModel::getNombre).collect(Collectors.toSet()));
    }
}
