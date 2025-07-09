package com.challengemeli.auction_service.infrastructure.controller;

import com.challengemeli.auction_service.application.AuctionUseCase;
import com.challengemeli.auction_service.domain.model.Auction;
import com.challengemeli.auction_service.domain.model.Bid;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuctionController.class)
class AuctionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuctionUseCase auctionUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private Auction auction;

    @BeforeEach
    void setUp() {
        auction = new Auction();
        auction.setId(1L);
        auction.setProductName("PlayStation 6");
        auction.setStartPrice(500.0);
        auction.setCurrentPrice(500.0);
        auction.setActive(true);
        auction.setStartTime(LocalDateTime.now());
        auction.setEndTime(LocalDateTime.now().plusMinutes(30));
    }

    @Test
    void shouldCreateAuction() throws Exception {
        when(auctionUseCase.saveAuction(any(Auction.class))).thenReturn(auction);

        mockMvc.perform(post("/auctions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(auction)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productName").value("PlayStation 6"));
    }

    @Test
    void shouldPlaceBid() throws Exception {
        Bid bid = new Bid();
        bid.setAmount(510.0);

        when(auctionUseCase.placeBid(eq(1L), any(Bid.class))).thenReturn(auction);

        mockMvc.perform(post("/auctions/1/bid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bid)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldGetAuctionById() throws Exception {
        when(auctionUseCase.findById(1L)).thenReturn(auction);

        mockMvc.perform(get("/auctions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.productName").value("PlayStation 6"));
    }

    @Test
    void shouldGetAllAuctions() throws Exception {
        when(auctionUseCase.findAllAuctions()).thenReturn(List.of(auction));

        mockMvc.perform(get("/auctions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetActiveAuctions() throws Exception {
        when(auctionUseCase.findAllActiveAuctions()).thenReturn(List.of(auction));

        mockMvc.perform(get("/auctions/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldGetInactiveAuctions() throws Exception {
        Auction inactiveAuction = new Auction();
        inactiveAuction.setId(2L);
        inactiveAuction.setProductName("Xbox Series Z");
        inactiveAuction.setActive(false);

        when(auctionUseCase.findAllInactiveAuctions()).thenReturn(List.of(inactiveAuction));

        mockMvc.perform(get("/auctions/inactive"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2L))
                .andExpect(jsonPath("$[0].active").value(false));
    }

    @Test
    void shouldCloseAuction() throws Exception {
        doNothing().when(auctionUseCase).closeAuction(1L);

        mockMvc.perform(post("/auctions/1/close"))
                .andExpect(status().isOk());

        verify(auctionUseCase).closeAuction(1L);
    }

    @Test
    void shouldReactivateAuction() throws Exception {
        auction.setActive(true);
        auction.setSuspendedUntil(null);

        when(auctionUseCase.reactivateAuction(1L)).thenReturn(auction);

        mockMvc.perform(post("/auctions/1/reactivate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("PlayStation 6"))
                .andExpect(jsonPath("$.active").value(true));

        verify(auctionUseCase).reactivateAuction(1L);
    }
}
