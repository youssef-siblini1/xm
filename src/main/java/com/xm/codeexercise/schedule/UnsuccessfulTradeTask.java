package com.xm.codeexercise.schedule;

import java.math.BigDecimal;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.TradesRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@AllArgsConstructor
@Slf4j
public class UnsuccessfulTradeTask {

	private final TradesRepo tradesRepo;

	@Scheduled(cron = "0 * * * * ?")
	public void handleUnsuccessfulTrades() {

		log.info("cron job is running to check for for trades that has PENDING_EXECUTION set for more than one minute");
		var result = tradesRepo.findExpiredTrades();
		result.stream().forEach(tradeEntity -> {
			tradeEntity.setStatus(TradeStatus.NOT_EXECUTED);
			tradeEntity.setReason("Expired Trade.");
		});
		tradesRepo.saveAll(result);
		log.info("Updated {} expired trades", result.size());
	}
}
