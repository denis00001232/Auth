package org.savchenko.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.savchenko.auth.dto.UserRegisterDto;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_username", columnList = "username", unique = true)
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true, nullable = false)
    private String username;

    @Column(length = 100, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String surname;

    public User(UserRegisterDto userRegisterDto) {
        this.username = userRegisterDto.getUsername();
        this.email = userRegisterDto.getEmail();
        this.name = userRegisterDto.getName();
        this.surname = userRegisterDto.getSurname();
    }
}

