package com.xm.codeexercise.controller.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import lombok.NonNull;
import lombok.Value;

@Value
public class TradeRequest {


	@Pattern(regexp = "USD\\/JPY|EUR\\/USD", message = "Symbol valid values: USD/JPY, EUR/USD")
	@NotBlank
	private String symbol;

	@Min(value = 0, message = "quantity must be greater than 0 and less than or equal to 1M")
	@Max(value = 1000000, message = "quantity must be greater than 0 and less than or equal to 1M")
	private Integer quantity;

	@Positive(message = "price must be greater than 0")
	private BigDecimal price;

}
