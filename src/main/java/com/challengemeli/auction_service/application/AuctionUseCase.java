package com.challengemeli.auction_service.application;

import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.model.Bid;
import com.challengemeli.auction_service.domain.service.AuctionDomainService;
import com.challengemeli.auction_service.domain.repository.AuctionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuctionUseCase {

    private final AuctionDomainService domainService;
    private final AuctionRepository auctionRepository;

    public AuctionUseCase(AuctionDomainService domainService, AuctionRepository auctionRepository) {
        this.domainService = domainService;
        this.auctionRepository = auctionRepository;
    }

    public Auction placeBid(Long auctionId, Bid bid) {
        return domainService.placeBid(auctionId, bid);
    }

    public void closeAuction(Long auctionId) {
        domainService.closeAuction(auctionId);
    }

    public List<Auction> findAllActiveAuctions() {
        return auctionRepository.findAll()
                .stream()
                .filter(a -> !Boolean.FALSE.equals(a.getActive()))
                .toList();
    }

    public Auction saveAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    public Auction findById(Long id) {
        return auctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
    }

    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }

    public List<Auction> findAllInactiveAuctions() {
        return auctionRepository.findAll()
                .stream()
                .filter(a -> !Boolean.TRUE.equals(a.getActive()))
                .toList();
    }

    public Auction reactivateAuction(Long auctionId) {
        Auction auction = findById(auctionId);
        if (auction.getSuspendedUntil() == null || LocalDateTime.now().isBefore(auction.getSuspendedUntil())) {
            auction.setActive(true);
            auction.setSuspendedUntil(null);
            auction.setEndTime(LocalDateTime.now().plusMinutes(25));
            auctionRepository.save(auction);
        }
        return auction;
    }

}
