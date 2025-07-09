package com.challengemeli.auction_service.domain.service;

import com.challengemeli.auction_service.domain.exception.AuctionClosedException;
import com.challengemeli.auction_service.domain.exception.AuctionNotFoundException;
import com.challengemeli.auction_service.domain.exception.InvalidBidException;
import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.model.Bid;
import com.challengemeli.auction_service.domain.repository.AuctionRepository;
import com.challengemeli.auction_service.domain.repository.BidRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class AuctionDomainService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    public AuctionDomainService(AuctionRepository auctionRepository, BidRepository bidRepository) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
    }

    @CircuitBreaker(name = "auctionService")
    public Auction placeBid(Long auctionId, Bid bid) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        if (!Boolean.TRUE.equals(auction.getActive()) || LocalDateTime.now().isAfter(auction.getEndTime())) {
            log.warn("Attempt to bid on closed auction with ID {}", auctionId);
            throw new AuctionClosedException("Auction is closed");
        }

        if (!auction.isBidValid(bid.getAmount())) {
            log.warn("Invalid bid amount {} for auction ID {}. Current price: {}", bid.getAmount(), auctionId, auction.getCurrentPrice());
            throw new InvalidBidException("Bid must be higher than current price + $1");
        }

        bid.setAuctionId(auctionId);
        bid.setTimestamp(LocalDateTime.now());
        bidRepository.save(bid);

        auction.setCurrentPrice(bid.getAmount());
        auctionRepository.save(auction);

        log.info("Bid of ${} accepted for auction ID {} by {}", bid.getAmount(), auctionId, bid.getBidder());
        processAsyncSideEffects(bid);
        return auction;
    }

    public void closeAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new AuctionNotFoundException("Auction not found"));

        log.info("Closing auction ID {}", auctionId);

        auction.setActive(false);

        List<Bid> bids = bidRepository.findByAuctionId(auctionId);
        List<Bid> mutableBids = new ArrayList<>(bids);
        mutableBids.sort(Comparator.comparingDouble(Bid::getAmount).reversed());

        Double newBasePrice = calculateNewBasePrice(mutableBids, auction);

        auction.setBasePrice(newBasePrice);
        auction.setCurrentPrice(newBasePrice);

        if (bids.isEmpty()) {
            auction.setSuspendedUntil(LocalDateTime.now().plusDays(30));
            log.info("Auction ID {} suspended for 30 days due to no bids", auctionId);
        } else {
            auction.setSuspendedUntil(null);
            log.info("Auction ID {} closed and ready for next round. New base price: ${}", auctionId, newBasePrice);
        }

        auctionRepository.save(auction);
    }

    /**
     * Calculates the new base price for the next auction round.
     */
    private Double calculateNewBasePrice(List<Bid> bids, Auction auction) {
        if (bids.size() >= 2) {
            log.debug("Calculating base price from second highest bid: ${}", bids.get(1).getAmount());
            return bids.get(1).getAmount();
        } else if (bids.size() == 1) {
            double price = Math.max(auction.getStartPrice(), bids.getFirst().getAmount() - 1);
            log.debug("Calculating base price from single bid: ${}", price);
            return price;
        } else {
            log.debug("No bids found, reverting base price to start price: ${}", auction.getStartPrice());
            return auction.getStartPrice();
        }
    }

    @Async
    public void processAsyncSideEffects(Bid bid) {
        log.info("Async side effects for bid: {}", bid.getAmount());
        // Enviar email, log externo, actualizar ranking, etc.
    }


}
