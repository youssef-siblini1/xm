package com.xm.codeexercise.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.external.BrokerTradeSide;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TradeDetailDto {

    private final UUID id;

    private final String symbol;

    private final Integer quantity;

    private final BrokerTradeSide side;

    private final BigDecimal price;

    private final TradeStatus status;

    private final String reason;

    private final LocalDateTime timestamp;

}
