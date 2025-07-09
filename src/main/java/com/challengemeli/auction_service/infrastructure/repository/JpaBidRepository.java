package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Bid;
import com.challengemeli.auction_service.domain.repository.BidRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JpaBidRepository implements BidRepository {

    private final SpringDataBidRepository repo;

    public JpaBidRepository(SpringDataBidRepository repo) {
        this.repo = repo;
    }

    @Override
    public void save(Bid bid) {
        repo.save(bid);
    }

    @Override
    public List<Bid> findByAuctionId(Long auctionId) {
        return repo.findByAuctionId(auctionId);
    }
}
