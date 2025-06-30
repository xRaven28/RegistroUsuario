package com.Registro.vero.Service;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import com.Registro.registroUsuario.DTO.*;
import com.Registro.registroUsuario.Models.*;
import com.Registro.registroUsuario.Repository.*;
import com.Registro.registroUsuario.Service.UsuarioService;

import java.time.LocalDate;
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
    void loginExitoso() {
        LoginRequest request = new LoginRequest("ana@gmail.com", "clave");
        RolModel rol = new RolModel(1L, "ADMIN", Set.of(new PermisoModel(1, "USUARIO VER")));
        UsuarioModel usuario = new UsuarioModel(
        1L, "Ana", "Lopez", "Gomez", "12345678-9", "ana@gmail.com", "claveEncriptada",
        LocalDate.now(), true, rol
    );
        when(usuarioRepository.findByEmail("ana@gmail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave", "claveEncriptada")).thenReturn(true);
        LoginResponse response = usuarioService.login(request);
        assertThat(response.getMensaje()).isEqualTo("Inicio exitoso"); 
        assertThat(response.getToken()).isEqualTo("Usuario ingresado");
    }
    
    @Test
    void loginFallaCredenciales() {
        LoginRequest request = new LoginRequest("ana@gmail.com", "clave");
        UsuarioModel usuario = new UsuarioModel();
        usuario.setEmail("ana@gmail.com");
        usuario.setContrasenia("otraClave");
        when(usuarioRepository.findByEmail("ana@gmail.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("clave", "otraClave")).thenReturn(false);
        assertThatThrownBy(() -> usuarioService.login(request))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Credenciales inválidas");
    }
    
    @Test
    void loginUsuarioNoExiste() {
    LoginRequest request = new LoginRequest("inexistente@gmail.com", "clave");

    when(usuarioRepository.findByEmail("inexistente@gmail.com"))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> usuarioService.login(request))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Credenciales inválidas")
        .satisfies(ex -> {
            ResponseStatusException e = (ResponseStatusException) ex;
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        });
    }

    @Test
    void loginRequestInvalido() {
    assertThatThrownBy(() -> usuarioService.login(null))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Email y contraseña son obligatorios");

    LoginRequest sinEmail = new LoginRequest(null, "clave");
    assertThatThrownBy(() -> usuarioService.login(sinEmail))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Email y contraseña son obligatorios");

    LoginRequest sinPass = new LoginRequest("ana@gmail.com", null);
    assertThatThrownBy(() -> usuarioService.login(sinPass))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Email y contraseña son obligatorios");

    LoginRequest emailVacio = new LoginRequest("   ", "clave");
    assertThatThrownBy(() -> usuarioService.login(emailVacio))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Email y contraseña son obligatorios");

    LoginRequest passVacia = new LoginRequest("ana@gmail.com", "  ");
    assertThatThrownBy(() -> usuarioService.login(passVacia))
        .isInstanceOf(ResponseStatusException.class)
        .hasMessageContaining("Email y contraseña son obligatorios");
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
    void crearUsuario_DTO_Invalido() {
        UsuarioCreateDTO dtoInvalido = new UsuarioCreateDTO();

        assertThatThrownBy(() -> usuarioService.crearUsuario(dtoInvalido))
            .isInstanceOf(ResponseStatusException.class)
            .hasMessageContaining("Datos inválidos");
    }
    @Test
    void LosCamposSonValidos() {
    UsuarioCreateDTO dto = new UsuarioCreateDTO(
        "Ana", "Lopez", "Perez", "12345678-9", "ana@gmail.com", "clave", 1L
    );

    assertThat(dto.esValido()).isTrue();
    }

    @Test
    void CamposSonInvalidos() {
    UsuarioCreateDTO dto = new UsuarioCreateDTO(); 

    assertThat(dto.esValido()).isFalse();
    }

    @Test
    void testObtenerUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> usuarioService.obtenerUsuario(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void obtenerUsuario_Exitoso() {
        RolModel rol = new RolModel(1L, "ADMIN", new HashSet<>());
        UsuarioModel usuario = new UsuarioModel(
            1L, "Juan", "Bernal", "Alarcon", "11222333-4", "juan@gmail.com",
            "hashed", LocalDate.now(), true, rol
        );
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioRegistroDTO dto = usuarioService.obtenerUsuario(1L);

        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNombre()).isEqualTo("Juan");
    }

    @Test
    void actualizarUsuario() {
       UsuarioCreateDTO dto = new UsuarioCreateDTO(
        "Ana", "Actualizado", "Gomez", "123", "ana@gmail.com", "clave", 1L
       );
       Set<PermisoModel> permisos = Set.of(new PermisoModel(1, "USUARIO VER"));
       RolModel rol = new RolModel(1L, "ADMIN", permisos);
       UsuarioModel existente = new UsuarioModel(
        1L, "Ana", "Lopez", "Gomez", "123", "ana@gmail.com", "anterior",
        LocalDate.now(), true, rol
       );
       UsuarioModel actualizado = new UsuarioModel(
        1L, "Ana", "Actualizado", "Gomez", "123", "ana@gmail.com", "hashedNuevaClave",
        LocalDate.now(), true, rol
        );
       when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
       when(passwordEncoder.encode("clave")).thenReturn("hashedNuevaClave");
       when(usuarioRepository.save(any())).thenReturn(actualizado);

       UsuarioRegistroDTO result = usuarioService.actualizarUsuario(1L, dto);

       assertThat(result.getApellidoPaterno()).isEqualTo("Actualizado");
       assertThat(result.getPermisos()).contains("USUARIO VER");
       verify(usuarioRepository).save(any(UsuarioModel.class));
    }

    @Test
    void actualizarUsuario_NoExiste() {
        UsuarioCreateDTO dto = new UsuarioCreateDTO("Ana", "Actualizado", "Gomez", "123", "ana@gmail.com", "clave", 1L);
        when(usuarioRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.actualizarUsuario(999L, dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Usuario no encontrado");
    }

    @Test
    void testEliminarUsuario() {
        RolModel rol = new RolModel(1L, "ADMIN", new HashSet<>());
        UsuarioModel usuario = new UsuarioModel(1L, "Juan", "Bernal", "Alarcon", "11222333-4",
                "juan@gmail.com", "123", null, true, rol);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        usuarioService.eliminarUsuario(1L);

        assertThat(usuario.isActivo()).isFalse();
        verify(usuarioRepository).save(usuario);
    }
}
