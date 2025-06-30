package com.Registro.vero.Controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.Registro.registroUsuario.Controller.UsuarioController;
import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Service.UsuarioService;

import java.util.Objects;
import java.util.Set;

public class UsuarioControllerTest {
  
    private UsuarioController controller;
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = mock(UsuarioService.class);
        controller = new UsuarioController(usuarioService);
    }

    @Test
    void ping_Pong() {
        String resultado = controller.ping();
        assertThat(resultado).isEqualTo("pong");
    }

    @Test
    void test_Ok() {
        String resultado = controller.test();
        assertThat(resultado).isEqualTo("OK");
    }

    @SuppressWarnings("null")
    @Test
    void crearUsuario_RetornaUsuario() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Ana", "Lopez", "Gomez", "12345678-9", "ana@gmail.com", "clave", 1L);
        UsuarioRegistroDTO esperado = new UsuarioRegistroDTO(
            1L, "Ana", "Lopez", "Gomez", "12345678-9", "ana@gmail.com",
            LocalDate.now(), "ADMIN", Set.of("USUARIO VER", "ROL VER")
        );

        when(usuarioService.crearUsuario(any())).thenReturn(esperado);

        var response = controller.crear(dto);

       UsuarioRegistroDTO result = Objects.requireNonNull(response.getContent(), "Contenido no debe ser null");

       assertThat(result.getId()).isEqualTo(1L);
       assertThat(result.getNombre()).isEqualTo("Ana");
       assertThat(result.getApellidoPaterno()).isEqualTo("Lopez");
       assertThat(result.getApellidoMaterno()).isEqualTo("Gomez");
       assertThat(result.getRut()).isEqualTo("12345678-9");
       assertThat(result.getEmail()).isEqualTo("ana@gmail.com");
       assertThat(result.getRol()).isEqualTo("ADMIN");
       assertThat(result.getPermisos()).contains("USUARIO VER", "ROL VER");
    }

    @Test
    void crearUsuario_DatosInvalidos_LanzaExcepcion() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("", "", "", "", "", "", 1L);

        when(usuarioService.crearUsuario(any()))
            .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Datos inválidos"));

        assertThatThrownBy(() -> controller.crear(dto))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Datos inválidos");
    }

    @Test
    void login_Exitoso() {
        LoginRequest login = new LoginRequest("ana@gmail.com", "clave");
        LoginResponse esperado = new LoginResponse("Usuario ingresado", "Inicio exitoso");

        when(usuarioService.login(any())).thenReturn(esperado);

        LoginResponse resultado = controller.login(login);
        assertThat(resultado.getMensaje()).isEqualTo("Inicio exitoso");
        assertThat(resultado.getToken()).isEqualTo("Usuario ingresado");
    }

    @Test
    void login_CredencialesInvalidas_LanzaExcepcion() {
        LoginRequest login = new LoginRequest("ana@gmail.com", "malaclave");

        when(usuarioService.login(any()))
            .thenThrow(new ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        assertThatThrownBy(() -> controller.login(login))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void obtenerUsuario_RetornaUsuario() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO(
                1L, "Ana", "Lopez", "Gomez", "123", "ana@gmail.com",
                LocalDate.now(), "ADMIN", Set.of("USUARIO VER")
        );

        when(usuarioService.obtenerUsuario(1L)).thenReturn(dto);

        var response = controller.obtener(1L);
        assertThat(response.getContent()).isNotNull();
        assertThat(response.getContent().getId()).isEqualTo(1L);
        assertThat(response.getContent().getNombre()).isEqualTo("Ana");
        assertThat(response.getContent().getPermisos()).contains("USUARIO VER");
    }

    @Test
    void actualizarUsuario_RetornaActualizado() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Ana", "Actualizado", "Gomez", "123", "ana@gmail.com", "clave", 1L);
        UsuarioRegistroDTO actualizado = new UsuarioRegistroDTO(
                1L, "Ana", "Actualizado", "Gomez", "123", "ana@gmail.com",
                LocalDate.now(), "ADMIN", Set.of("USUARIO VER")
        );

        when(usuarioService.actualizarUsuario(eq(1L), any())).thenReturn(actualizado);

        var response = controller.actualizar(1L, dto);
        assertThat(response.getContent().getApellidoPaterno()).isEqualTo("Actualizado");
    }

    @Test
    void eliminarUsuario_LlamaAlServicio() {
        controller.eliminar(1L);
        verify(usuarioService).eliminarUsuario(1L);
    }
}

