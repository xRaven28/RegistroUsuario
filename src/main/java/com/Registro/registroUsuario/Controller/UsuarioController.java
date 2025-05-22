package com.Registro.registroUsuario.Controller;

import com.Registro.registroUsuario.DTO.UsuarioRegistroDTO;
import com.Registro.registroUsuario.Models.UsuarioModel;
import com.Registro.registroUsuario.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/Usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegistroDTO dto) {
        if (!dto.getContrasenia().equals(dto.getConfirmarContrasenia())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Las contraseñas no coinciden");
        }

        UsuarioModel usuario = usuarioService.registrar(
            dto.getNombre(),
            dto.getApellidoPaterno(),
            dto.getApellidoMaterno(),
            dto.getRut(),
            dto.getEmail(),
            dto.getContrasenia(),
            dto.getConfirmarContrasenia(),
            dto.getRolId()
        );

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error en los datos de registro");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }
    @GetMapping("/ping")
    public String ping() {
        return "Servidor arriba";
    }
}
