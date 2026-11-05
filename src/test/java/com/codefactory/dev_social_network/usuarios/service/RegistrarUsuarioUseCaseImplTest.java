package com.codefactory.dev_social_network.usuarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.codefactory.dev_social_network.usuarios.dto.RegistrationData;
import com.codefactory.dev_social_network.usuarios.interfaces.CredencialCreationPort;
import com.codefactory.dev_social_network.usuarios.interfaces.PasswordHasher;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrationRule;
import com.codefactory.dev_social_network.usuarios.interfaces.UserRegistrationPort;

@ExtendWith(MockitoExtension.class)
class RegistrarUsuarioUseCaseImplTest {

    private static final UUID USER_ID = UUID.randomUUID();

    @Mock
    private UserRegistrationPort userRegistrationPort;
    @Mock
    private CredencialCreationPort credencialService;
    @Mock
    private PasswordHasher passwordHasher;
    @Mock
    private RegistrationRule rule;

    private RegistrarUsuarioUseCaseImpl useCase;
    private RegistrationData data;

    @BeforeEach
    void setUp() {
        useCase = new RegistrarUsuarioUseCaseImpl(
                userRegistrationPort, credencialService, passwordHasher, List.of(rule));
        data = new RegistrationData("LuisDev", "luis@test.com", "Luis", "Dev", "Clave123*");
    }

    @Test
    @DisplayName("guarda el usuario ANTES de crear su credencial y devuelve el id")
    void guardaElUsuarioAntesDeLaCredencial() {
        // Arrange
        when(passwordHasher.hashear("Clave123*")).thenReturn("hash");
        when(userRegistrationPort.registrar("LuisDev", "Luis", "Dev", "luis@test.com")).thenReturn(USER_ID);

        // Act
        UUID resultado = useCase.registrar(data);

        // Assert
        assertThat(resultado).isEqualTo(USER_ID);
        InOrder orden = inOrder(rule, userRegistrationPort, credencialService);
        orden.verify(rule).validate(data);
        orden.verify(userRegistrationPort).registrar("LuisDev", "Luis", "Dev", "luis@test.com");
        orden.verify(credencialService).crearCredencialLocal(USER_ID, "hash");
        verify(userRegistrationPort, never()).eliminar(any());
    }

    @Test
    @DisplayName("si falla la credencial, elimina el usuario recien creado y relanza el error original")
    void compensaSiFallaLaCredencial() {
        // Arrange
        RuntimeException error = new IllegalStateException("autenticacion caida");
        when(passwordHasher.hashear("Clave123*")).thenReturn("hash");
        when(userRegistrationPort.registrar(any(), any(), any(), any())).thenReturn(USER_ID);
        doThrow(error).when(credencialService).crearCredencialLocal(USER_ID, "hash");

        // Act y Assert
        assertThatThrownBy(() -> useCase.registrar(data)).isSameAs(error);
        verify(userRegistrationPort).eliminar(USER_ID);
    }

    @Test
    @DisplayName("si tambien falla la compensacion, relanza el error original y conserva el segundo como suprimido")
    void conservaElErrorOriginalSiFallaLaCompensacion() {
        // Arrange
        RuntimeException error = new IllegalStateException("autenticacion caida");
        RuntimeException falloAlBorrar = new IllegalStateException("bd caida");
        when(passwordHasher.hashear("Clave123*")).thenReturn("hash");
        when(userRegistrationPort.registrar(any(), any(), any(), any())).thenReturn(USER_ID);
        doThrow(error).when(credencialService).crearCredencialLocal(USER_ID, "hash");
        doThrow(falloAlBorrar).when(userRegistrationPort).eliminar(USER_ID);

        // Act y Assert
        assertThatThrownBy(() -> useCase.registrar(data))
                .isSameAs(error)
                .hasSuppressedException(falloAlBorrar);
    }

    @Test
    @DisplayName("si una regla de validacion falla, no guarda nada ni llama a autenticacion")
    void noGuardaSiUnaReglaFalla() {
        // Arrange
        RuntimeException error = new IllegalArgumentException("regla");
        doThrow(error).when(rule).validate(data);

        // Act y Assert
        assertThatThrownBy(() -> useCase.registrar(data)).isSameAs(error);
        verifyNoInteractions(userRegistrationPort, credencialService, passwordHasher);
    }

    @Test
    @DisplayName("no es transaccional: la llamada a autenticacion no puede ir dentro de una transaccion de BD")
    void noEsTransaccional() throws NoSuchMethodException {
        assertThat(RegistrarUsuarioUseCaseImpl.class.isAnnotationPresent(Transactional.class)).isFalse();
        assertThat(RegistrarUsuarioUseCaseImpl.class
                .getMethod("registrar", RegistrationData.class)
                .isAnnotationPresent(Transactional.class)).isFalse();
    }
}
