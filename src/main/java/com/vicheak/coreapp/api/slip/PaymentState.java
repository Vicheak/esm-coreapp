package com.vicheak.coreapp.api.slip;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_states")
public class PaymentState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_state_id")
    private Integer id;

    @Column(name = "payment_state_status", length = 50, unique = true, nullable = false)
    private String status;

}
