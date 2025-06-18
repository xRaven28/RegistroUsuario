package com.Registro.vero.Service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Models.*;
import com.Registro.registroUsuario.Repository.*;
import com.Registro.registroUsuario.Service.UsuarioService;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RegistroServiceTest {
    @Mock
    private UsuarioRespo usuarioRepository;
    @Mock
    private UsuarioRolRespo rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUsuario() {
        UsuarioCreateDTO usuariodto = new UsuarioCreateDTO("Juan", "Bernal", "Alarcon", "11222333-4", "juan@gmail.com",
                "123", 1);
        RolModel usuariorol = new RolModel(null, "ADMIN", new HashSet<>());
        when(rolRepository.findById(1L)).thenReturn(Optional.of(usuariorol));
        when(passwordEncoder.encode("123")).thenReturn("123");

        UsuarioModel guardado = new UsuarioModel(1L, "Juan", "Bernal",
                "Alarcon", "11222333-4", "juan@gmail.com", "123",
                null, true, usuariorol);
        when(usuarioRepository.save(any(UsuarioModel.class))).thenReturn(guardado);
        UsuarioRegistroDTO result = usuarioService.crearUsuario(usuariodto);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNombre()).isEqualTo("Juan");
        verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @Test
    void testObtenerUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.obtenerUsuario(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void testEliminarUsuario() {
        RolModel rol = new RolModel(null, "ADMIN", new HashSet<>());
        UsuarioModel usuario = new UsuarioModel(1L, "Juan", "Bernal", "Alarcon", "11222333-4",
                "juan@gmail.com", "123", null, true, rol);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.eliminarUsuario(1L);

        assertThat(usuario.isActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }
}
