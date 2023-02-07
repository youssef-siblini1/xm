package com.xm.codeexercise.controller;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.controller.dto.TradeStatusDto;
import org.springframework.stereotype.Component;

@Component
public class TradeStatusConverter {

	public TradeStatusDto convert(TradeStatus tradeStatus) {
		return TradeStatusDto.builder().status(tradeStatus.name()).build();
	}
}
