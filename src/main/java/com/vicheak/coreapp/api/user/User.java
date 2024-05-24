package com.vicheak.coreapp.api.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_uuid", unique = true, nullable = false)
    private String uuid;

    @Column(name = "user_username", length = 80, unique = true, nullable = false)
    private String username;

    @Column(name = "user_email", length = 80, unique = true, nullable = false)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_nickname", length = 30)
    private String nickname;

    @Column(name = "user_verified_code", length = 10)
    private String verifiedCode;

    @Column(name = "user_is_verified")
    private Boolean isVerified;

    @Column(name = "user_is_deleted")
    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns =
            @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns =
            @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> roles;

}
