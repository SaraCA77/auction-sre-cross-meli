package com.challengemeli.auction_service.infrastructure.controller;

import com.challengemeli.auction_service.application.AuctionUseCase;
import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.model.Bid;
import com.challengemeli.auction_service.infrastructure.dto.AuctionResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionUseCase auctionUseCase;

    public AuctionController(AuctionUseCase auctionUseCase) {
        this.auctionUseCase = auctionUseCase;
    }

    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(@RequestBody Auction auction) {
        Auction saved = auctionUseCase.saveAuction(auction);
        return ResponseEntity.ok(new AuctionResponseDto(saved));
    }
    @PostMapping("/{id}/bid")
    public ResponseEntity<AuctionResponseDto> placeBid(@PathVariable Long id, @RequestBody Bid bid) {
        Auction auction = auctionUseCase.placeBid(id, bid);
        return ResponseEntity.ok(new AuctionResponseDto(auction));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponseDto> getAuctionById(@PathVariable Long id) {
        Auction auction = auctionUseCase.findById(id);
        return ResponseEntity.ok(new AuctionResponseDto(auction));
    }
    @GetMapping
    public ResponseEntity<List<AuctionResponseDto>> getAllAuctions() {
        List<Auction> auctions = auctionUseCase.findAllAuctions();
        return ResponseEntity.ok(auctions.stream().map(AuctionResponseDto::new).toList());
    }

    @GetMapping("/active")
    public ResponseEntity<List<AuctionResponseDto>> getActiveAuctions() {
        List<Auction> auctions = auctionUseCase.findAllActiveAuctions();
        return ResponseEntity.ok(auctions.stream().map(AuctionResponseDto::new).toList());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<AuctionResponseDto>> getInactiveAuctions() {
        List<Auction> auctions = auctionUseCase.findAllInactiveAuctions();
        return ResponseEntity.ok(auctions.stream().map(AuctionResponseDto::new).toList());
    }

    @PostMapping("/{id}/close")
    public ResponseEntity<Void> closeAuction(@PathVariable Long id) {
        auctionUseCase.closeAuction(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/reactivate")
    public ResponseEntity<AuctionResponseDto> reactivateAuction(@PathVariable Long id) {
        Auction auction = auctionUseCase.reactivateAuction(id);
        return ResponseEntity.ok(new AuctionResponseDto(auction));
    }


}
