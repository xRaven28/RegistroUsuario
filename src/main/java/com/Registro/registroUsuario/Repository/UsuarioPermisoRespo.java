package com.Registro.registroUsuario.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Registro.registroUsuario.Models.PermisoModel;

public interface UsuarioPermisoRespo extends JpaRepository<PermisoModel,Integer> {

}
