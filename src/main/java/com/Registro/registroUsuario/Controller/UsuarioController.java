package com.Registro.registroUsuario.Controller;

import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

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
        return agregarLinks(usuario);
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
        return agregarLinks(usuario);
    }

    @Operation(summary = "Actualizar datos del usuario")
    @PutMapping("/{id}")
    public EntityModel<UsuarioRegistroDTO> actualizar(@PathVariable long id, @RequestBody UsuarioCreateDTO dto) {
        UsuarioRegistroDTO actualizado = usuarioService.actualizarUsuario(id, dto);
        return agregarLinks(actualizado);
    }

    @Operation(summary = "Eliminar (desactivar) usuario")
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable long id) {
        usuarioService.eliminarUsuario(id);
    }

      private EntityModel<UsuarioRegistroDTO> agregarLinks(UsuarioRegistroDTO dto) {
        EntityModel<UsuarioRegistroDTO> resource = EntityModel.of(dto);

        resource.add(
            WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).obtener(dto.getId())
            ).withSelfRel()
        );

        resource.add(
            WebMvcLinkBuilder.linkTo(UsuarioController.class)
                .slash(dto.getId())
                .withRel("desactivar")
        );

        return resource;
    }
}
