package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataAuctionRepository  extends JpaRepository<Auction, Long> {
}
