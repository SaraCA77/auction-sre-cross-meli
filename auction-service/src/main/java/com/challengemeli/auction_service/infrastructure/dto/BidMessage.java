package com.challengemeli.auction_service.infrastructure.dto;

import com.challengemeli.auction_service.domain.model.Bid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BidMessage {
    private Long auctionId;
    private Double amount;
    private String bidderName;

    public Bid toBid() {
        Bid bid = new Bid();
        bid.setAmount(this.amount);
        bid.setBidder(this.bidderName);
        return bid;
    }
}