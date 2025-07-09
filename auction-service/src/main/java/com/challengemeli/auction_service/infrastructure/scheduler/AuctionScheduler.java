package com.challengemeli.auction_service.infrastructure.scheduler;

import com.challengemeli.auction_service.application.AuctionUseCase;
import com.challengemeli.auction_service.domain.model.Auction;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class AuctionScheduler {
    private final AuctionUseCase auctionUseCase;
    public AuctionScheduler(AuctionUseCase auctionUseCase) {
        this.auctionUseCase = auctionUseCase;
    }

    // Se ejecuta cada minuto
    @Scheduled(fixedRate = 60000)
    public void closeExpiredAuctions() {
        List<Auction> activeAuctions = auctionUseCase.findAllActiveAuctions();

        for (Auction auction : activeAuctions) {
            if (Boolean.TRUE.equals(auction.getActive()) && LocalDateTime.now().isAfter(auction.getEndTime())) {
                auctionUseCase.closeAuction(auction.getId());
                log.info("Auction closed automatically: {}", auction.getId());
            }
        }
    }
}
