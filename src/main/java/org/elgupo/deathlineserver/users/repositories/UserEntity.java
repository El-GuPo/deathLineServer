package org.elgupo.deathlineserver.users.repositories;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", schema = "public")
@Entity
public class UserEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    public UserEntity(
            String email,
            String password
    ) {
        this.email = email;
        this.password = password;
    }
}
