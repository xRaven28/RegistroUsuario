package com.Registro.registroUsuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Registro.registroUsuario.Models.UsuarioModel;

public interface UsuarioRespo extends JpaRepository<UsuarioModel, Integer> {
    boolean existsByEmail(String email);
    boolean existsByRut(String rut);
}
