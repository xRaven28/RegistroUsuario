package com.Registro.registroUsuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Registro.registroUsuario.Models.UsuarioModel;
import java.util.Optional;

public interface UsuarioRespo extends JpaRepository<UsuarioModel, Integer> {
     Optional<UsuarioModel> findByEmail(String email);
}
