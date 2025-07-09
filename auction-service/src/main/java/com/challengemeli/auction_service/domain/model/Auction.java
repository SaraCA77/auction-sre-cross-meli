package com.challengemeli.auction_service.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    private Double startPrice;
    private Double currentPrice;
    private Double basePrice;

    private Boolean active;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime suspendedUntil;

    //Centralizamos las reglas de dominio (ej: validación de puja) para mantener independencia de infraestructura.
    public boolean isBidValid(Double bidAmount) {
        return bidAmount != null && bidAmount > currentPrice && bidAmount - currentPrice >= 1;
    }
}
