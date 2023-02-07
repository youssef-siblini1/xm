package com.xm.codeexercise.controller;

import com.xm.codeexercise.dao.model.TradeEntity;
import com.xm.codeexercise.controller.dto.TradeDetailDto;
import org.springframework.stereotype.Component;

@Component
public class TradeDetailConverter {

	public TradeDetailDto convert(TradeEntity tradeEntity) {
		return TradeDetailDto.builder()
			.id(tradeEntity.getId())
			.quantity(tradeEntity.getQuantity())
			.symbol(tradeEntity.getSymbol())
			.side(tradeEntity.getSide())
			.price(tradeEntity.getPrice())
			.status(tradeEntity.getStatus())
			.reason(tradeEntity.getReason())
			.timestamp(tradeEntity.getCreationDate())
			.build();
	}
}
