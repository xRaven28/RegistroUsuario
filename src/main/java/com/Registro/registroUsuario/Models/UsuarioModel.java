package com.Registro.registroUsuario.Models;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 50, nullable = false)
    private String nombre;

    @Column(length = 50, nullable = false)
    private String apellidoPaterno;

    @Column(length = 50, nullable = false)
    private String apellidoMaterno;

    @Column(length = 13, nullable = false, unique = true)
    private String rut;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String contrasenia;

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private RolModel rol;
    public boolean validar() {
        return nombre != null && !nombre.trim().isEmpty()
            && apellidoPaterno != null && !apellidoPaterno.trim().isEmpty()
            && apellidoMaterno != null && !apellidoMaterno.trim().isEmpty()
            && rut != null && !rut.trim().isEmpty()
            && email != null && !email.trim().isEmpty()
            && contrasenia != null && !contrasenia.trim().isEmpty();
    }

    public boolean verificarContraseña(String password) {
        return this.contrasenia.equals(password);
    }
}
