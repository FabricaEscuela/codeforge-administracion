package com.codefactory.dev_social_network.usuarios.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.codefactory.dev_social_network.usuarios.dto.RegistrationData;
import com.codefactory.dev_social_network.usuarios.interfaces.CredencialCreationPort;
import com.codefactory.dev_social_network.usuarios.interfaces.PasswordHasher;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrarUsuarioUseCase;
import com.codefactory.dev_social_network.usuarios.interfaces.RegistrationRule;
import com.codefactory.dev_social_network.usuarios.interfaces.UserRegistrationPort;

/**
 * Registra un usuario y le crea su credencial en el servicio de autenticacion.
 *
 * Este caso de uso NO es transaccional a proposito: la credencial se crea en
 * otro servicio (otra conexion a la BD), y la llave foranea
 * credenciales.usuario_id -> usuarios(id) exige que el usuario ya este
 * confirmado. Por eso el usuario se guarda en su propia transaccion corta, se
 * llama a autenticacion y, si esa llamada falla, se compensa eliminando el
 * usuario recien creado para no dejarlo sin credencial.
 */
@Service
public class RegistrarUsuarioUseCaseImpl implements RegistrarUsuarioUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarUsuarioUseCaseImpl.class);

    private final UserRegistrationPort userRegistrationPort;
    private final CredencialCreationPort credencialService;
    private final PasswordHasher passwordHasher;
    private final List<RegistrationRule> rules;

    public RegistrarUsuarioUseCaseImpl(UserRegistrationPort userRegistrationPort,
                                       CredencialCreationPort credencialService,
                                       PasswordHasher passwordHasher,
                                       List<RegistrationRule> rules) {
        this.userRegistrationPort = userRegistrationPort;
        this.credencialService = credencialService;
        this.passwordHasher = passwordHasher;
        this.rules = rules;
    }

    @Override
    public UUID registrar(RegistrationData data) {
        for (RegistrationRule rule : rules) {
            rule.validate(data);
        }
        String passwordHash = passwordHasher.hashear(data.contrasena());

        UUID userId = userRegistrationPort.registrar(
                data.username(), data.nombre(), data.apellido(), data.email());
        try {
            credencialService.crearCredencialLocal(userId, passwordHash);
        } catch (RuntimeException causa) {
            revertirRegistro(userId, causa);
            throw causa;
        }
        return userId;
    }

    private void revertirRegistro(UUID userId, RuntimeException causa) {
        try {
            userRegistrationPort.eliminar(userId);
        } catch (RuntimeException falloAlRevertir) {
            log.error("No se pudo revertir el usuario {} tras fallar la creacion de su credencial",
                    userId, falloAlRevertir);
            causa.addSuppressed(falloAlRevertir);
        }
    }
}
