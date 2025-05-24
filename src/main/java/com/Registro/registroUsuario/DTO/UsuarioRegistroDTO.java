package com.Registro.registroUsuario.DTO;

import lombok.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
     private int id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rut;
    private String email;
    private LocalDate fechaRegistro;
    private String rol;
    private Set<String> permisos;
}
