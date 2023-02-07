package com.xm.codeexercise.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.model.TradeEntity;
import com.xm.codeexercise.external.BrokerTradeSide;
import com.xm.codeexercise.controller.dto.TradeDetailDto;
import com.xm.codeexercise.controller.dto.TradeStatusDto;
import com.xm.codeexercise.controller.request.TradeRequest;
import com.xm.codeexercise.service.TradesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = TradesController.class)
class TradesControllerTest {

	private static final String TRADE_ID = "e80f2d62-3f0c-4a1d-9a47-8376";
	private static final String TRADE_STATUS_URL = "http://localhost/api/trades/e80f2d62-3f0c-4a1d-9a47-8376/status";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TradesService tradesService;

	@MockBean
	private TradeStatusConverter tradeStatusConverter;

	@MockBean
	private TradeDetailConverter tradeDetailConverter;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testBuyTrade_success() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 1000, BigDecimal.valueOf(1.123));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.BUY));

		// when
		var result = mockMvc.perform(MockMvcRequestBuilders
			.post("/api/buy")
			.content(objectMapper.writeValueAsString(tradeRequest))
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isCreated()).andExpect(header().string("Location", TRADE_STATUS_URL));
	}

	@Test
	void testBuyTrade_notValidSymbol() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/LEB", 1000, BigDecimal.valueOf(1.123));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.BUY));

		// when/then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/buy")
				.content(objectMapper.writeValueAsString(tradeRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("{\"symbol\":\"Symbol valid values: USD/JPY, EUR/USD\"}"));
	}

	@Test
	void testBuyTrade_quantityLessThanZero() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", -1, BigDecimal.valueOf(1.123));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.BUY));

		// when/then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/buy")
				.content(objectMapper.writeValueAsString(tradeRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("{\"quantity\":\"quantity must be greater than 0 and less than " +
				"or equal to 1M\"}"));
	}

	@Test
	void testBuyTrade_quantityGreaterThanMillion() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 10000001, BigDecimal.valueOf(1.123));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.BUY));

		// when/then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/buy")
				.content(objectMapper.writeValueAsString(tradeRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("{\"quantity\":\"quantity must be greater than 0 and less than "
				+ "or equal to 1M\"}"));
	}

	@Test
	void testBuyTrade_negativePrice() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 1000, BigDecimal.valueOf(-1));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.BUY));

		// when/then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/buy")
				.content(objectMapper.writeValueAsString(tradeRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().string("{\"price\":\"price must be greater than 0\"}"));
	}

	@Test
	void testSellTrade_success() throws Exception {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 1000, BigDecimal.valueOf(1.123));
		doReturn(TRADE_ID).when(tradesService).executeTrade(any(), eq(BrokerTradeSide.SELL));

		// when/then
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/sell")
				.content(objectMapper.writeValueAsString(tradeRequest))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(header().string("Location", TRADE_STATUS_URL));
	}

	@Test
	void testFindTradeStatus() throws Exception {

		// given
		var tradeStatusDto = TradeStatusDto.builder().status(TradeStatus.NOT_EXECUTED.name()).build();
		var tradeStatus = Optional.of(TradeStatus.NOT_EXECUTED);
		doReturn(tradeStatus).when(tradesService).findTradeStatus(any());
		doReturn(tradeStatusDto).when(tradeStatusConverter).convert(any());

		// when
		var result =
			mockMvc.perform(get("/api/trades/" + TRADE_ID + "/status").contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(content().string("{\"status\":\"NOT_EXECUTED\"}"));
	}

	@Test
	void testFindTradeDetails() throws Exception {

		// given
		var tradeDetailDto = buildTradeDetailDto();
		doReturn(Optional.of(buildTradeEntity())).when(tradesService).findTradeDetails(any());
		doReturn(tradeDetailDto).when(tradeDetailConverter).convert(any());

		// when
		var result = mockMvc.perform(get("/api/trades/" + TRADE_ID).contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(content().string(objectMapper.writeValueAsString(tradeDetailDto)));
	}

	@Test
	void testFindAllTrade() throws Exception {

		// given
		var tradeDetailDto = buildTradeDetailDto();
		doReturn(List.of(buildTradeEntity())).when(tradesService).findAllTrades();
		doReturn(tradeDetailDto).when(tradeDetailConverter).convert(any());

		// when
		var result = mockMvc.perform(get("/api/trades").contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpect(status().isOk());
		result.andExpect(content().string(objectMapper.writeValueAsString(List.of(tradeDetailDto))));
	}

	private TradeDetailDto buildTradeDetailDto() {

		return TradeDetailDto.builder()
			.id(UUID.fromString(TRADE_ID))
			.symbol("EUR/USD")
			.price(BigDecimal.valueOf(10))
			.reason("reason")
			.status(TradeStatus.NOT_EXECUTED)
			.build();
	}

	private TradeEntity buildTradeEntity() {
		return new TradeEntity(UUID.fromString(TRADE_ID), 1000, "EUR/USD", BigDecimal.valueOf(10),
			"reason", TradeStatus.NOT_EXECUTED, BrokerTradeSide.BUY);
	}

}