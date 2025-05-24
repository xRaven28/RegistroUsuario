package com.Registro.registroUsuario.Config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.Registro.registroUsuario.Models.PermisoModel;
import com.Registro.registroUsuario.Models.RolModel;
import com.Registro.registroUsuario.Repository.UsuarioPermisoRespo;
import com.Registro.registroUsuario.Repository.UsuarioRolRespo;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RolPermisoDataLoader implements CommandLineRunner {

    private final UsuarioRolRespo rolRepository;
    private final UsuarioPermisoRespo permisoRepository;
    @Override
    public void run(String... args) throws Exception {

        if (permisoRepository.count() == 0) {
            List<PermisoModel> permisosBase = List.of(
                new PermisoModel(0, "USUARIO_CREAR"),
                new PermisoModel(0, "USUARIO_EDITAR"),
                new PermisoModel(0, "USUARIO_ELIMINAR"),
                new PermisoModel(0, "CURSO_VER"),
                new PermisoModel(0, "CURSO_EDITAR"),
                new PermisoModel(0, "ADMINISTRACION")
            );
            permisoRepository.saveAll(permisosBase);
            System.out.println("Permisos base creados.");
        }

        List<PermisoModel> todosPermisos = permisoRepository.findAll();

        Map<String, PermisoModel> permisosMap = new HashMap<>();
        for (PermisoModel p : todosPermisos) {
            permisosMap.put(p.getNombre(), p);
        }

        if (rolRepository.count() == 0) {
            Map<String, Set<PermisoModel>> permisosPorRol = new HashMap<>();

            permisosPorRol.put("Estudiante", Set.of(permisosMap.get("CURSO_VER")));
            permisosPorRol.put("Docente", Set.of(permisosMap.get("CURSO_VER"), permisosMap.get("CURSO_EDITAR")));
            permisosPorRol.put("Administrador", new HashSet<>(todosPermisos));
            permisosPorRol.put("Gerente de curso", Set.of(permisosMap.get("CURSO_VER"), permisosMap.get("USUARIO_CREAR"), permisosMap.get("USUARIO_EDITAR")));
            permisosPorRol.put("Servicio técnico", Set.of(permisosMap.get("ADMINISTRACION")));

            for (Map.Entry<String, Set<PermisoModel>> entry : permisosPorRol.entrySet()) {
                RolModel rol = new RolModel();
                rol.setNombre(entry.getKey());
                rol.setPermisos(entry.getValue());
                rolRepository.save(rol);
            }

            System.out.println("Roles base con permisos creados.");
        }
    }
}
