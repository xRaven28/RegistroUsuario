package com.Registro.registroUsuario.Config;

import com.Registro.registroUsuario.Models.RolModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.Registro.registroUsuario.Repository.UsuarioRolRespo;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRolRespo rolRepository;

    @Override
    public void run(String... args) throws Exception {
        if (rolRepository.count() == 0) {
            rolRepository.save(new RolModel(null, "Estudiante", null));
            rolRepository.save(new RolModel(null, "Docente", null));
            rolRepository.save(new RolModel(null, "Administrador", null));
            rolRepository.save(new RolModel(null, "Gerente de curso", null));
            rolRepository.save(new RolModel(null, "Servicio técnico", null));
            System.out.println("Roles iniciales creados automáticamente.");
        }
    }
}