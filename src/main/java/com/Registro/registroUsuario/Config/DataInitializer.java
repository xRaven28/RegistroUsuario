package com.Registro.registroUsuario.Config;

import com.Registro.registroUsuario.Models.RolModel;
import com.Registro.registroUsuario.Repository.UsuarioRolRespo;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRolRespo rolRepository;

    @PostConstruct
    public void initRoles() {
        List<String> roles = List.of(
                "Administrador",
                "Gerente de Curso",
                "Profesor",
                "Estudiante",
                "Servicio Tecnico");

        for (String nombre : roles) {
            if (!rolRepository.existsByNombre(nombre)) {
                RolModel rol = new RolModel();
                rol.setNombre(nombre);
                rolRepository.save(rol);
            }
        }
        System.out.println("Roles cargados");
    }
}
