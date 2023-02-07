package com.xm.codeexercise.controller;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.model.TradeEntity;
import com.xm.codeexercise.external.BrokerTradeSide;
import com.xm.codeexercise.controller.dto.TradeDetailDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TradeDetailConverterTest {

	@InjectMocks
	private TradeDetailConverter converter;

	@Test
	void testConvert() {

		// given
		var tradeEntity = new TradeEntity(UUID.fromString("e80f2d62-3f0c-4a1d-9a47-8376"), 1000, "EUR/USD",
			BigDecimal.valueOf(10), "reason", TradeStatus.NOT_EXECUTED, BrokerTradeSide.BUY);

		// when
		var result = converter.convert(tradeEntity);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(TradeDetailDto.builder()
			.id(UUID.fromString("e80f2d62-3f0c-4a1d-9a47-8376"))
			.quantity(1000)
			.side(BrokerTradeSide.BUY)
			.symbol("EUR/USD")
			.price(BigDecimal.valueOf(10))
			.reason("reason")
			.status(TradeStatus.NOT_EXECUTED)
			.build());
	}

}