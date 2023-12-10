package com.vicheak.coreapp.api.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Integer id;

    @Column(name = "authority_name", length = 80, unique = true, nullable = false)
    private String name;

    @Column(name = "authority_description")
    private String description;

    @ManyToMany(mappedBy = "authorities")
    private Set<Role> roles;

}
