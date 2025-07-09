package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataBidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionId(Long auctionId);
}