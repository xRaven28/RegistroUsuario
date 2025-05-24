package com.Registro.registroUsuario.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String mensaje;
}
