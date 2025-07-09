package com.challengemeli.auction_service.domain.repository;
import com.challengemeli.auction_service.domain.model.Auction;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository {
    Optional<Auction> findById(Long id);
    Auction save(Auction auction);
    List<Auction> findAll();
}
