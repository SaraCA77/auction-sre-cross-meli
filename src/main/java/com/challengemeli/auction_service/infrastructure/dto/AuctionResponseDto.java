package com.challengemeli.auction_service.infrastructure.dto;

import com.challengemeli.auction_service.domain.model.Auction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AuctionResponseDto {
    private Long id;
    private String productName;
    private Double startPrice;
    private Double currentPrice;
    private Double basePrice;
    private Boolean active;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime suspendedUntil;

    public AuctionResponseDto(Auction auction) {
        this.id = auction.getId();
        this.productName = auction.getProductName();
        this.startPrice = auction.getStartPrice();
        this.currentPrice = auction.getCurrentPrice();
        this.basePrice = auction.getBasePrice();
        this.active = auction.getActive();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
        this.suspendedUntil = auction.getSuspendedUntil();
    }

}
