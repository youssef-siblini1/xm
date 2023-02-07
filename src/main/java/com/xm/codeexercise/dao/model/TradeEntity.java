package com.xm.codeexercise.dao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.xm.codeexercise.dao.TradeStatus;
import com.xm.codeexercise.external.BrokerTradeSide;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "trade")
public class TradeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "uuid")
	private UUID id;

	private Integer quantity;
	private String  symbol;

	private BigDecimal price;

	@Enumerated(EnumType.STRING)
	private TradeStatus status;

	private String reason;

	@CreationTimestamp
	private LocalDateTime creationDate;

	@UpdateTimestamp
	private LocalDateTime updatedDate;

	@Enumerated(EnumType.STRING)
	private BrokerTradeSide brokerTradeSide;

	public TradeEntity() {
	}

	public TradeEntity(UUID id, Integer quantity, String symbol, BigDecimal price, String reason, TradeStatus status,
		BrokerTradeSide brokerTradeSide) {
		this.id = id;
		this.quantity = quantity;
		this.symbol = symbol;
		this.price = price;
		this.reason = reason;
		this.status = status;
		this.brokerTradeSide = brokerTradeSide;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public TradeStatus getStatus() {
		return status;
	}

	public void setStatus(TradeStatus status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public BrokerTradeSide getSide() {
		return brokerTradeSide;
	}

	public void setSide(BrokerTradeSide brokerTradeSide) {
		this.brokerTradeSide = brokerTradeSide;
	}
}
