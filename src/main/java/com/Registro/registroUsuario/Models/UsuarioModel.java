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

    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private RolModel rol;
}
