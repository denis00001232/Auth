package org.savchenko.auth.keycloak;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.savchenko.auth.dto.UserRegisterDto;
import org.savchenko.auth.exception.*;
import org.savchenko.auth.service.PathManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class KeycloakRegisterService {
    private final Keycloak keycloak;
    private final RestTemplate restTemplate;
    private final PathManager pathManager;

    public void createUser(UserRegisterDto userRegisterDto) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userRegisterDto.getUsername());
        user.setFirstName(userRegisterDto.getName());
        user.setLastName(userRegisterDto.getSurname());
        user.setEmail(userRegisterDto.getEmail());
        user.setEnabled(true);
        //user.setRequiredActions(Collections.singletonList("VERIFY_EMAIL"));
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(userRegisterDto.getPassword());
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));
        try (Response response = keycloak.realm("axolotl")
                .users()
                .create(user)) {
//            if (response.getStatus() == 201) {
//                System.out.println("Пользователь создан");
//                keycloak.realm("axolotl")
//                        .users()
//                        .get(CreatedResponseUtil.getCreatedId(response))
//                        .sendVerifyEmail();
//            }
        }
    }

    public void recoverPassword(String email) {
        Optional<UserRepresentation> userRepresentation = findUserByEmail(email);
        if (userRepresentation.isEmpty()) {
            throw new RecoverPasswordException();
        }
        keycloak.realm("axolotl")
                .users()
                .get(userRepresentation.get().getId())
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
    }

    private Optional<UserRepresentation> findUserByUsername(String username) {
        List<UserRepresentation> users = keycloak.
                realm("axolotl").
                users().
                search(username, true);
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    private Optional<UserRepresentation> findUserByEmail(String email) {
        List<UserRepresentation> users = keycloak.
                realm("axolotl").
                users().
                search(null, null, null, email, 0, 1);
        return users.stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    private boolean deleteUserById(String userId) {
        try {
            keycloak.realm("axolotl")
                    .users()
                    .delete(userId);
            return true;
        } catch (NotFoundException e) {
            System.out.println("Пользователь не найден: " + userId);
            return false;
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            return false;
        }
    }

    private void deleteUserIfEmailNotVerified(Optional<UserRepresentation> user, RuntimeException runtimeException) {
        if (user.isEmpty()) return;
        UserRepresentation userRepresentation = user.get();
        if (userRepresentation.isEmailVerified()) {
            throw runtimeException;
        } else {
            deleteUserById(userRepresentation.getId());
        }
    }

    //Оба метода проверяют есть ли кто-то с неподтвержденным email и вот такой информацией и если есть нафиг его удаляют
    public void checkByUsername(String username) {
        Optional<UserRepresentation> user = findUserByUsername(username);
        deleteUserIfEmailNotVerified(user, new UsernameAlreadyExistsException());
    }


    public void checkByEmail(String email) {
        Optional<UserRepresentation> user = findUserByEmail(email);
        deleteUserIfEmailNotVerified(user, new EmailAlreadyExistsException());
    }




}