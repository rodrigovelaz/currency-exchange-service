package org.rodrigovelaz.currencyexchangeservice.business.service;

import java.util.Currency;

import org.rodrigovelaz.currencyexchangeservice.business.exception.CurrencyExchangeException;
import org.rodrigovelaz.currencyexchangeservice.persistence.model.CurrencyExchange;
import org.rodrigovelaz.currencyexchangeservice.presentation.json.response.CurrencyExchangeResponseJson;

public interface CurrencyExchangeService {

	public CurrencyExchange findByFromAndTo(Currency currencyFrom, Currency currencyTo) throws CurrencyExchangeException;
	public CurrencyExchange updateCurrencyExchange(Currency currencyFrom, Currency currencyTo, Double rate);
	public CurrencyExchangeResponseJson exchange(Currency currencyFrom, Currency currencyTo, Double amount) throws CurrencyExchangeException;

}
