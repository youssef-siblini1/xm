package com.xm.codeexercise.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.dao.TradesRepo;
import com.xm.codeexercise.dao.model.TradeEntity;
import com.xm.codeexercise.external.BrokerTrade;
import com.xm.codeexercise.external.BrokerTradeSide;
import com.xm.codeexercise.external.ExternalBroker;
import com.xm.codeexercise.controller.request.TradeRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TradesService {

	private final TradesRepo tradesRepo;

	private final ExternalBroker externalBroker;

	public TradesService(TradesRepo tradesRepo, TradeHandler tradeHandler) {
		this.tradesRepo = tradesRepo;
		this.externalBroker = new ExternalBroker(tradeHandler);
	}

	public String executeTrade(TradeRequest tradeRequest, BrokerTradeSide brokerTradeSide) {

		log.info("Executing a {} trade.", brokerTradeSide.name());

		var tradeEntity = tradesRepo.saveAndFlush(buildTradeEntity(tradeRequest, brokerTradeSide));
		log.info("Trade {} has been saved in the database.", tradeEntity.getId());

		externalBroker.execute(buildBrokerTrade(brokerTradeSide, tradeEntity));
		return tradeEntity.getId().toString();
	}

	public Optional<TradeStatus> findTradeStatus(UUID uuid) {

		log.info("Fetching trade {} from the database", uuid.toString());
		return tradesRepo.findById(uuid).map(TradeEntity::getStatus);
	}

	public Optional<TradeEntity> findTradeDetails(UUID uuid) {

		log.info("Fetching trade {} from the database", uuid.toString());
		return tradesRepo.findById(uuid);
	}

	public List<TradeEntity> findAllTrades() {

		log.info("Fetching all trades from the database");
		return tradesRepo.findAll();
	}

	private BrokerTrade buildBrokerTrade(BrokerTradeSide brokerTradeSide, TradeEntity tradeEntity) {

		return new BrokerTrade(
			tradeEntity.getId(),
			tradeEntity.getSymbol(),
			tradeEntity.getQuantity(),
			brokerTradeSide,
			tradeEntity.getPrice());
	}

	private TradeEntity buildTradeEntity(TradeRequest tradeRequest, BrokerTradeSide brokerTradeSide) {

		return new TradeEntity(null,
			tradeRequest.getQuantity(),
			tradeRequest.getSymbol(),
			tradeRequest.getPrice(),
			null,
			TradeStatus.PENDING_EXECUTION,
			brokerTradeSide);
	}

}
