package com.xm.codeexercise.dao;

import java.util.List;
import java.util.UUID;

import com.xm.codeexercise.dao.model.TradeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TradesRepo extends JpaRepository<TradeEntity, UUID> {

	@Query("SELECT m FROM TradeEntity m WHERE m.status = 'PENDING_EXECUTION' AND m.creationDate < SQL(\"(? - interval" +
		" '1' minute)\", CURRENT_TIMESTAMP)")
	List<TradeEntity> findExpiredTrades();
}
