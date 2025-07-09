package com.challengemeli.auction_service.infrastructure.controller;

import com.challengemeli.auction_service.application.AuctionUseCase;
import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.infrastructure.dto.BidMessage;
import com.challengemeli.auction_service.infrastructure.dto.AuctionResponseDto;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller

public class AuctionWebSocketController {

    private final AuctionUseCase auctionUseCase;
    private final MeterRegistry meterRegistry;

    @Autowired
    public AuctionWebSocketController(AuctionUseCase auctionUseCase, MeterRegistry meterRegistry) {
        this.auctionUseCase = auctionUseCase;
        this.meterRegistry = meterRegistry;
    }
    @MessageMapping("/bid")
    @SendTo("/topic/updates")
    public AuctionResponseDto placeBid(BidMessage bidMessage) throws Exception {
        // Incrementar métrica de pujas recibidas
        meterRegistry.counter("websocket.bids.received").increment();

        // Timer para medir el tiempo de procesamiento de la puja
        Timer timer = Timer.builder("websocket.bids.process.time")
                .description("Tiempo en procesar una puja")
                .register(meterRegistry);

        Auction auction = timer.record(() ->
                auctionUseCase.placeBid(bidMessage.getAuctionId(), bidMessage.toBid())
        );
        return new AuctionResponseDto(auction);
    }
}
