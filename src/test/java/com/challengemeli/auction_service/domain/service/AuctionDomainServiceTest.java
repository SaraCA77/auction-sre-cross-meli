package com.challengemeli.auction_service;

import com.challengemeli.auction_service.domain.exception.AuctionNotFoundException;
import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.model.Bid;
import com.challengemeli.auction_service.domain.repository.AuctionRepository;
import com.challengemeli.auction_service.domain.repository.BidRepository;
import com.challengemeli.auction_service.domain.service.AuctionDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuctionDomainServiceTest {

    private AuctionRepository auctionRepository;
    private BidRepository bidRepository;
    private AuctionDomainService auctionDomainService;

    @BeforeEach
    void setUp() {
        auctionRepository = mock(AuctionRepository.class);
        bidRepository = mock(BidRepository.class);
        auctionDomainService = new AuctionDomainService(auctionRepository, bidRepository);
    }

    @Test
    void shouldCloseAuctionWithoutBidsAndSuspend() {
        Auction auction = new Auction();
        auction.setId(1L);
        auction.setStartPrice(500.0);
        auction.setCurrentPrice(500.0);
        auction.setActive(true);

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuctionId(1L)).thenReturn(Collections.emptyList());

        auctionDomainService.closeAuction(1L);

        ArgumentCaptor<Auction> captor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(captor.capture());

        Auction saved = captor.getValue();
        assertThat(saved.getActive()).isFalse();
        assertThat(saved.getBasePrice()).isEqualTo(auction.getStartPrice());
        assertThat(saved.getCurrentPrice()).isEqualTo(auction.getStartPrice());
        assertThat(saved.getSuspendedUntil()).isAfter(LocalDateTime.now());
    }

    @Test
    void shouldCloseAuctionWithOneBidWithoutSuspend() {
        Auction auction = new Auction();
        auction.setId(2L);
        auction.setStartPrice(500.0);
        auction.setCurrentPrice(510.0);
        auction.setActive(true);

        Bid bid = new Bid();
        bid.setAmount(510.0);

        when(auctionRepository.findById(2L)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuctionId(2L)).thenReturn(List.of(bid));

        auctionDomainService.closeAuction(2L);

        ArgumentCaptor<Auction> captor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(captor.capture());

        Auction saved = captor.getValue();
        assertThat(saved.getBasePrice()).isEqualTo(Math.max(auction.getStartPrice(), bid.getAmount() - 1));
        assertThat(saved.getCurrentPrice()).isEqualTo(saved.getBasePrice());
        assertThat(saved.getSuspendedUntil()).isNull();
    }

    @Test
    void shouldCloseAuctionWithMultipleBidsWithoutSuspend() {
        Auction auction = new Auction();
        auction.setId(3L);
        auction.setStartPrice(500.0);
        auction.setCurrentPrice(520.0);
        auction.setActive(true);

        Bid bid1 = new Bid();
        bid1.setAmount(520.0);

        Bid bid2 = new Bid();
        bid2.setAmount(515.0);

        Bid bid3 = new Bid();
        bid3.setAmount(510.0);

        List<Bid> bids = Arrays.asList(bid1, bid2, bid3);

        when(auctionRepository.findById(3L)).thenReturn(Optional.of(auction));
        when(bidRepository.findByAuctionId(3L)).thenReturn(bids);

        auctionDomainService.closeAuction(3L);

        ArgumentCaptor<Auction> captor = ArgumentCaptor.forClass(Auction.class);
        verify(auctionRepository).save(captor.capture());

        Auction saved = captor.getValue();
        assertThat(saved.getBasePrice()).isEqualTo(515.0);
        assertThat(saved.getCurrentPrice()).isEqualTo(saved.getBasePrice());
        assertThat(saved.getSuspendedUntil()).isNull();
    }

    @Test
    void shouldThrowIfAuctionNotFound() {
        when(auctionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> auctionDomainService.closeAuction(99L))
                .isInstanceOf(AuctionNotFoundException.class)
                .hasMessage("Auction not found");
    }
}
