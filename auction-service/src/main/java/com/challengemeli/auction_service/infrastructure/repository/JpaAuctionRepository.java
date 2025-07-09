package com.challengemeli.auction_service.infrastructure.repository;

import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.repository.AuctionRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaAuctionRepository implements AuctionRepository {
    private final SpringDataAuctionRepository springDataRepository;

    public JpaAuctionRepository(SpringDataAuctionRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Auction save(Auction auction) {
        return springDataRepository.save(auction);
    }

    @Override
    public Optional<Auction> findById(Long id) {
        return springDataRepository.findById(id);
    }
    @Override
    public List<Auction> findAll() {
        return springDataRepository.findAll();
    }
}
