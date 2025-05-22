package com.Registro.registroUsuario.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRegistroDTO {
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno; 
    private String rut;
    private String email;
    private String contrasenia;
    private String confirmarContrasenia;
    private Integer rolId;
}
