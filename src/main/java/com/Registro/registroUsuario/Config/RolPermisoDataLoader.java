package com.Registro.registroUsuario.Config;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.Registro.registroUsuario.Models.PermisoModel;
import com.Registro.registroUsuario.Models.RolModel;
import com.Registro.registroUsuario.Repository.UsuarioPermisoRespo;
import com.Registro.registroUsuario.Repository.UsuarioRolRespo;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RolPermisoDataLoader implements CommandLineRunner {

    private final UsuarioRolRespo rolRepository;
    private final UsuarioPermisoRespo permisoRepository;
    
     @Override
    public void run(String... args) throws Exception {

        if (permisoRepository.count() == 0) {
            permisoRepository.saveAll(Arrays.asList(
                new PermisoModel(null, "USUARIO CREAR"),
                new PermisoModel(null, "USUARIO VER"),
                new PermisoModel(null, "USUARIO EDITAR"),
                new PermisoModel(null, "USUARIO ELIMINAR"),
                new PermisoModel(null, "ROL ASIGNAR"),
                new PermisoModel(null, "ROL VER")
            ));
            System.out.println("Permisos iniciales creados.");
        }

        if (rolRepository.count() == 0) {
            List<PermisoModel> permisos = permisoRepository.findAll();

            Set<PermisoModel> permisosAdmin = new HashSet<>(permisos);

            Set<PermisoModel> permisosDocente = permisos.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase("USUARIO VER") || p.getNombre().equalsIgnoreCase("ROL VER"))
                .collect(Collectors.toSet());
                RolModel admin = new RolModel(null, "ADMIN", permisosAdmin);
                RolModel docente = new RolModel(null, "DOCENTE", permisosDocente);

            rolRepository.saveAll(Arrays.asList(admin, docente));
            System.out.println("Roles ADMIN y DOCENTE creados.");
}
}
}