package com.Registro.registroUsuario.Hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import com.Registro.registroUsuario.Controller.UsuarioController;
import com.Registro.registroUsuario.DTO.UsuarioRegistroDTO;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioRegistroDTO, EntityModel<UsuarioRegistroDTO>>{
    @Override
    public @NonNull EntityModel<UsuarioRegistroDTO> toModel(@NonNull UsuarioRegistroDTO dto) {
        EntityModel<UsuarioRegistroDTO> resource = EntityModel.of(dto);

        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).obtener(dto.getId())
        ).withSelfRel());

        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UsuarioController.class).eliminar(dto.getId())
        ).withRel("desactivar"));

        return resource;
    }
}
