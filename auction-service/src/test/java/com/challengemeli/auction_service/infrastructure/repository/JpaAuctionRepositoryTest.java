package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Auction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaAuctionRepositoryTest {

    @Autowired
    private SpringDataAuctionRepository springDataAuctionRepository;

    @Test
    void shouldSaveAndFindAuction() {
        Auction auction = new Auction();
        auction.setProductName("Test Console");
        auction.setStartPrice(100.0);
        auction.setCurrentPrice(100.0);
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusMinutes(30));
        auction.setActive(true);

        Auction saved = springDataAuctionRepository.save(auction);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getProductName()).isEqualTo("Test Console");
        assertThat(springDataAuctionRepository.findById(saved.getId())).isPresent();
    }
}
