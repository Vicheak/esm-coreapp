package com.vicheak.coreapp.api.salarygross;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "gross_types")
public class GrossType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gross_type_id")
    private Integer id;

    @Column(name = "gross_type_name", length = 50, unique = true, nullable = false)
    private String name;

    @Column(name = "gross_type_description", columnDefinition = "TEXT")
    private String description;

}
