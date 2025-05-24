package com.Registro.registroUsuario.Controller;

import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

  
    private final UsuarioService usuarioService;

    @PostMapping("/crear")
    public UsuarioRegistroDTO crear(@RequestBody UsuarioCreateDTO dto) {
        return usuarioService.crearUsuario(dto);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    @GetMapping("/{id}")
    public UsuarioRegistroDTO obtener(@PathVariable int id) {
        return usuarioService.obtenerUsuario(id);
    }

    @PutMapping("/{id}")
    public UsuarioRegistroDTO actualizar(@PathVariable int id, @RequestBody UsuarioCreateDTO dto) {
        return usuarioService.actualizarUsuario(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable int id) {
        usuarioService.eliminarUsuario(id);
    }
}
