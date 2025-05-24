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
            rolRepository.save(new RolModel(0, "Estudiante", null));
            rolRepository.save(new RolModel(0, "Docente", null));
            rolRepository.save(new RolModel(0, "Administrador", null));
            rolRepository.save(new RolModel(0, "Gerente de curso", null));
            rolRepository.save(new RolModel(0, "Servicio técnico", null));
            System.out.println("Roles iniciales creados automáticamente.");
        }
    }
}