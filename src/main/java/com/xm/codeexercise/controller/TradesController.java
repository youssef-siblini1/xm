package com.xm.codeexercise.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import com.xm.codeexercise.external.BrokerTradeSide;
import com.xm.codeexercise.controller.dto.TradeDetailDto;
import com.xm.codeexercise.controller.dto.TradeStatusDto;
import com.xm.codeexercise.controller.request.TradeRequest;
import com.xm.codeexercise.service.TradesService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/api")
@AllArgsConstructor
public class TradesController {

	private final TradesService tradesService;
	private final TradeStatusConverter tradeStatusConverter;
	private final TradeDetailConverter tradeDetailConverter;

	@PostMapping("/buy")
	public ResponseEntity<String> buyTrade(@Valid @RequestBody TradeRequest tradeRequest) {

		return executeTrade(tradeRequest, BrokerTradeSide.BUY);
	}

	@PostMapping("/sell")
	public ResponseEntity<String> sellTrade(@Valid @RequestBody TradeRequest tradeRequest) {

		return executeTrade(tradeRequest, BrokerTradeSide.SELL);
	}

	@GetMapping("/trades/{tradeId}/status")
	public ResponseEntity<TradeStatusDto> findTradeStatus(@PathVariable String tradeId) {

		return ResponseEntity.of(tradesService.findTradeStatus(UUID.fromString(tradeId))
			.map(tradeStatusConverter::convert));
	}

	@GetMapping("/trades/{tradeId}")
	public ResponseEntity<TradeDetailDto> findTradeDetails(@PathVariable UUID tradeId) {

		return ResponseEntity.of(tradesService.findTradeDetails(tradeId)
			.map(tradeDetailConverter::convert));
	}

	@GetMapping("/trades")
	public ResponseEntity<List<TradeDetailDto>> findAllTrades() {

		var tradeDetailDtos = tradesService.findAllTrades().stream()
			.map(tradeDetailConverter::convert)
			.toList();
		return ResponseEntity.ok(tradeDetailDtos);
	}

	private ResponseEntity<String> executeTrade(TradeRequest tradeRequest, BrokerTradeSide brokerTradeSide) {

		var savedTradeId = tradesService.executeTrade(tradeRequest, brokerTradeSide);
		return ResponseEntity.created(buildTradeStatusUri(savedTradeId)).build();

	}

	private URI buildTradeStatusUri(String savedTradeId) {

		return ServletUriComponentsBuilder
			.fromCurrentContextPath()
			.path("/api/trades/{tradeId}/status")
			.buildAndExpand(savedTradeId)
			.toUri();
	}

}
