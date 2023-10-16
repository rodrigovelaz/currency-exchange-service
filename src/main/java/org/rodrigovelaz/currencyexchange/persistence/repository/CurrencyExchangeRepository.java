package org.rodrigovelaz.currencyexchange.persistence.repository;

import java.util.Currency;

import org.rodrigovelaz.currencyexchange.persistence.entity.CurrencyExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
	
	CurrencyExchange findByCurrencyFromAndCurrencyTo(Currency currencyFrom, Currency currencyTo);
	
}
