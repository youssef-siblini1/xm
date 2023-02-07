package com.xm.codeexercise.service;

import java.util.UUID;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.TradesRepo;
import com.xm.codeexercise.external.BrokerResponseCallback;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TradeHandler implements BrokerResponseCallback {

	private final TradesRepo tradesRepo;

	@Override
	public void successful(UUID tradeId) {

		tradesRepo.findById(tradeId)
			.ifPresent(tradeEntity -> {
				tradeEntity.setStatus(TradeStatus.EXECUTED);
				tradeEntity.setReason("");
				tradesRepo.save(tradeEntity);
			});
	}

	@Override
	public void unsuccessful(UUID tradeId, String reason) {

		tradesRepo.findById(tradeId)
			.ifPresent(tradeEntity -> {
				tradeEntity.setReason(reason);
				tradeEntity.setStatus(TradeStatus.NOT_EXECUTED);
				tradesRepo.save(tradeEntity);
			});
	}
}
