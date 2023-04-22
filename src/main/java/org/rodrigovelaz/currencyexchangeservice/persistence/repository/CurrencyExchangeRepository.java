package org.rodrigovelaz.currencyexchangeservice.persistence.repository;

import java.util.Currency;

import org.rodrigovelaz.currencyexchangeservice.persistence.entity.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
	
	public CurrencyExchange findByCurrencyFromAndCurrencyTo(Currency currencyFrom, Currency currencyTo);
	
}
