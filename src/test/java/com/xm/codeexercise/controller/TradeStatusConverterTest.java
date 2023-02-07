package com.xm.codeexercise.controller;


import static org.assertj.core.api.Assertions.assertThat;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.controller.dto.TradeStatusDto;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class TradeStatusConverterTest {

	@InjectMocks
	private TradeStatusConverter converter;

	@Test
	void testConvert() {

		// given / when
		var result = converter.convert(TradeStatus.NOT_EXECUTED);

		// then
		assertThat(result).usingRecursiveComparison().isEqualTo(TradeStatusDto.builder().status(TradeStatus.NOT_EXECUTED.name()).build());
	}

}