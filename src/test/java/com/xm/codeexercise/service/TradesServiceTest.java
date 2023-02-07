package com.xm.codeexercise.service;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.xm.codeexercise.controller.request.TradeRequest;
import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.TradesRepo;
import com.xm.codeexercise.dao.model.TradeEntity;
import com.xm.codeexercise.external.BrokerTradeSide;
import com.xm.codeexercise.external.ExternalBroker;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TradesServiceTest {

	private static final UUID TRADE_UUID = UUID.fromString("e80f2d62-3f0c-4a1d-9a47-000000008376");

	@Mock
	private TradesRepo tradesRepo;

	@Mock
	private ExternalBroker externalBroker;

	@InjectMocks
	private TradesService tradesService;

	@Test
	void testExecuteTrade_buy() {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 1000, BigDecimal.valueOf(1.123));
		doReturn(createTradeEntity(BrokerTradeSide.SELL)).when(tradesRepo).saveAndFlush(any());

		// when
		var result = tradesService.executeTrade(tradeRequest, BrokerTradeSide.BUY);

		// then
		assertThat(result).isEqualTo(TRADE_UUID.toString());
	}

	@Test
	void testExecuteTrade_sell() {

		// given
		var tradeRequest = new TradeRequest("EUR/USD", 10, BigDecimal.valueOf(1000));
		doReturn(createTradeEntity(BrokerTradeSide.SELL)).when(tradesRepo).saveAndFlush(any());

		// when
		var result = tradesService.executeTrade(tradeRequest, BrokerTradeSide.SELL);

		// then
		assertThat(result).isEqualTo(TRADE_UUID.toString());
	}

	@Test
	void testFindTradeStatus() {

		// given
		doReturn(Optional.of(createTradeEntity(BrokerTradeSide.SELL))).when(tradesRepo).findById(TRADE_UUID);

		// when
		var result = tradesService.findTradeStatus(TRADE_UUID);

		// then
		assertThat(result).isPresent().get().isEqualTo(TradeStatus.PENDING_EXECUTION);
	}

	@Test
	void testFindTradeDetail() {

		// given
		var tradeEntity = createTradeEntity(BrokerTradeSide.SELL);
		doReturn(Optional.of(tradeEntity)).when(tradesRepo).findById(TRADE_UUID);

		// when
		var result = tradesService.findTradeDetails(TRADE_UUID);

		// then
		assertThat(result).isPresent().get().usingRecursiveComparison().isEqualTo(tradeEntity);
	}

	@Test
	void testAllTrades() {

		// given
		var tradeEntity = createTradeEntity(BrokerTradeSide.SELL);
		doReturn(List.of(tradeEntity)).when(tradesRepo).findAll();

		// when
		var result = tradesService.findAllTrades();

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(List.of(tradeEntity));
	}

	private TradeEntity createTradeEntity(BrokerTradeSide side) {

		return new TradeEntity(UUID.fromString("e80f2d62-3f0c-4a1d-9a47-8376"),
			10,
			"EUR/US",
			BigDecimal.valueOf(1000),
			"",
			TradeStatus.PENDING_EXECUTION,
			side);
	}

}