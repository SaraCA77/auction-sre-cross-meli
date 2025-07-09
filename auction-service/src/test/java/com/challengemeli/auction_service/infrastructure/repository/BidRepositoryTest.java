package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Bid;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BidRepositoryTest {

    @Autowired
    private SpringDataBidRepository springDataBidRepository;

    @Test
    void shouldSaveAndFindByAuctionId() {
        Bid bid = new Bid();
        bid.setAuctionId(1L);
        bid.setAmount(150.0);
        bid.setBidder("Tester");
        bid.setTimestamp(LocalDateTime.now());

        springDataBidRepository.save(bid);

        List<Bid> bids = springDataBidRepository.findByAuctionId(1L);
        assertThat(bids).isNotEmpty();
        assertThat(bids.getFirst().getAmount()).isEqualTo(150.0);
    }
}

