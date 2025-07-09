package com.challengemeli.auction_service.domain.repository;

import com.challengemeli.auction_service.domain.model.Bid;

import java.util.List;

public interface  BidRepository {
    void save(Bid bid);
    List<Bid> findByAuctionId(Long auctionId);
}
