package com.Registro.registroUsuario.Controller;
import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Hateoas.UsuarioModelAssembler;
import com.Registro.registroUsuario.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

      private final UsuarioService usuarioService;
    private final UsuarioModelAssembler assembler;

    @Operation(summary = "Test básico del sistema")
    @GetMapping("/test")
    public String test() {
        return "OK";
    }

    @Operation(summary = "Verifica la conectividad")
    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @Operation(summary = "Crear un nuevo usuario")
    @PostMapping("/crear")
    public EntityModel<UsuarioRegistroDTO> crear(@RequestBody UsuarioCreateDTO dto) {
        UsuarioRegistroDTO usuario = usuarioService.crearUsuario(dto);
        return assembler.toModel(usuario);
    }

    @Operation(summary = "Inicio de sesión de usuario")
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return usuarioService.login(request);
    }

    @Operation(summary = "Obtener información de usuario por ID")
    @GetMapping("/{id}")
    public EntityModel<UsuarioRegistroDTO> obtener(@PathVariable long id) {
        UsuarioRegistroDTO usuario = usuarioService.obtenerUsuario(id);
        return assembler.toModel(usuario);
    }

    @Operation(summary = "Actualizar datos del usuario")
    @PutMapping("/{id}")
    public EntityModel<UsuarioRegistroDTO> actualizar(@PathVariable long id, @RequestBody UsuarioCreateDTO dto) {
        UsuarioRegistroDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return assembler.toModel(actualizado);
    }

    @Operation(summary = "Eliminar usuario")
    @DeleteMapping("/{id}")
    public Class<?> eliminar(@PathVariable long id) {
        usuarioService.eliminarUsuario(id);
        return null;
    }
}
