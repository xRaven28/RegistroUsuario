package com.Registro.registroUsuario.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Registro.registroUsuario.Models.PermisoModel;

public interface UsuarioPermisoRespo extends JpaRepository<PermisoModel,Integer> {

    List<PermisoModel> findByNombreIn(List<String> of);

}
