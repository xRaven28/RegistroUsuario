package com.Registro.registroUsuario.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioCreateDTO {
 private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String rut;
    private String email;
    private String contrasenia;
    private int rolId;

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty()
            && apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()
            && apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()
            && rut != null && !rut.trim().isEmpty()
            && email != null && !email.trim().isEmpty()
            && contrasenia != null && !contrasenia.trim().isEmpty();
    }
}
