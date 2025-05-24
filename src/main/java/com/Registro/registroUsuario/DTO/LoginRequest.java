package com.Registro.registroUsuario.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String contrasenia;
}
