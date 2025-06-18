package com.Registro.registroUsuario.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.Registro.registroUsuario.Models.RolModel;

public interface UsuarioRolRespo extends JpaRepository<RolModel ,Long>{
    RolModel findByNombre(String nombre);
}
