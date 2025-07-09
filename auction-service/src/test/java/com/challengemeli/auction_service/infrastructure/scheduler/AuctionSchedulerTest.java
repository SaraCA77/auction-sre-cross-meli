package com.challengemeli.auction_service.infrastructure.scheduler;

import com.challengemeli.auction_service.application.AuctionUseCase;
import com.challengemeli.auction_service.domain.model.Auction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.*;

class AuctionSchedulerTest {

    private AuctionUseCase auctionUseCase;
    private AuctionScheduler auctionScheduler;

    @BeforeEach
    void setUp() {
        auctionUseCase = mock(AuctionUseCase.class);
        auctionScheduler = new AuctionScheduler(auctionUseCase);
    }

    @Test
    void shouldCloseExpiredAuctions() {
        // Arrange
        Auction expiredAuction = new Auction();
        expiredAuction.setId(1L);
        expiredAuction.setActive(true);
        expiredAuction.setEndTime(LocalDateTime.now().minusMinutes(1));

        when(auctionUseCase.findAllActiveAuctions()).thenReturn(Collections.singletonList(expiredAuction));

        // Act
        auctionScheduler.closeExpiredAuctions();

        // Assert
        verify(auctionUseCase).closeAuction(1L);
    }

    @Test
    void shouldNotCloseActiveAuctionsThatAreNotExpired() {
        // Arrange
        Auction activeAuction = new Auction();
        activeAuction.setId(2L);
        activeAuction.setActive(true);
        activeAuction.setEndTime(LocalDateTime.now().plusMinutes(10));

        when(auctionUseCase.findAllActiveAuctions()).thenReturn(Collections.singletonList(activeAuction));

        // Act
        auctionScheduler.closeExpiredAuctions();

        // Assert
        verify(auctionUseCase, never()).closeAuction(anyLong());
    }

    @Test
    void shouldNotCloseInactiveAuctions() {
        // Arrange
        Auction inactiveAuction = new Auction();
        inactiveAuction.setId(3L);
        inactiveAuction.setActive(false);
        inactiveAuction.setEndTime(LocalDateTime.now().minusMinutes(5));

        when(auctionUseCase.findAllActiveAuctions()).thenReturn(Collections.singletonList(inactiveAuction));

        // Act
        auctionScheduler.closeExpiredAuctions();

        // Assert
        verify(auctionUseCase, never()).closeAuction(anyLong());
    }
}
