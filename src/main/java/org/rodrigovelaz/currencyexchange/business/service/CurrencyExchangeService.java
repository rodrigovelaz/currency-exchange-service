package org.rodrigovelaz.currencyexchange.business.service;

import java.time.LocalDateTime;
import java.util.Currency;

import org.apache.log4j.Logger;
import org.rodrigovelaz.currencyexchange.business.exception.CurrencyExchangeException;
import org.rodrigovelaz.currencyexchange.persistence.repository.CurrencyExchangeRepository;
import org.rodrigovelaz.currencyexchange.persistence.entity.CurrencyExchange;
import org.rodrigovelaz.currencyexchange.presentation.json.response.CurrencyExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class CurrencyExchangeService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeRepository repository;
	
	private CurrencyExchangeResponse create(Currency currencyFrom, Currency currencyTo, Double amountFrom, Double rate) {
		
		Double amountToRounded = this.roundUp(amountFrom * rate, currencyTo);
		
		CurrencyExchangeResponse response = new CurrencyExchangeResponse();
		response.setAmountFrom(amountFrom);
		response.setCurrencyFrom(currencyFrom);
		response.setAmountTo(amountToRounded);
		response.setCurrencyTo(currencyTo);
		response.setRate(rate);
		return response;
	}
	
	public CurrencyExchange findByFromAndTo(Currency currencyFrom, Currency currencyTo) throws CurrencyExchangeException {
		return this.repository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
	}
	
	public CurrencyExchangeResponse exchange(Currency currencyFrom, Currency currencyTo, Double amount) throws CurrencyExchangeException {

		// Simple rate
		Double rate = this.getSimpleRate(currencyFrom, currencyTo);
		if (rate != null) return this.create(currencyFrom, currencyTo, amount, rate);
		
		// Chain rate
		rate = this.getChainRate(currencyFrom, currencyTo);
		return this.create(currencyFrom, currencyTo, amount, rate);
	}
	
	private Double getSimpleRate(Currency currencyFrom, Currency currencyTo) {
	
		// Rate 1.0 (Same currency from/to) 
		if (currencyFrom.equals(currencyTo)) {
			return 1.0;
		}
		
		// Direct rate
		CurrencyExchange currencyExchange = this.repository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);
		if (currencyExchange != null) {
			return currencyExchange.getRate();
		}
		
		// Inverse rate
		currencyExchange = this.repository.findByCurrencyFromAndCurrencyTo(currencyTo, currencyFrom);
		if (currencyExchange != null) {
			return 1/currencyExchange.getRate();
		}
		
		return null;
	}
	
	private Double getChainRate(Currency currencyFrom, Currency currencyTo) throws CurrencyExchangeException {
		
		// Chain exchange
		Currency currencyDollar = Currency.getInstance("USD");

		Double rate1 = this.getSimpleRate(currencyFrom, currencyDollar);
		if (rate1 == null) throw new CurrencyExchangeException("Couldn't find rate: " + currencyFrom + " -> " + currencyTo);
		
		Double rate2 = this.getSimpleRate(currencyDollar, currencyTo);
		if (rate2 == null) throw new CurrencyExchangeException("Couldn't find rate: " + currencyFrom + " -> " + currencyTo);

		return rate1 * rate2;
	}
	
	
	private Double roundUp(Double amount, Currency currency) {
		return this.roundUp(amount, currency.getDefaultFractionDigits());
	}
	
	private Double roundUp(Double amount, Integer decimals) {
		
		// 10 ^ 2 = 100
		Double decimalsPositions = Math.pow(10, decimals);
		
		// 1999.9999985228097 * 100
		Double amountRounded = amount * decimalsPositions;
		
		// 2000 / 100
		amountRounded = Math.round(amountRounded) / decimalsPositions;
		
		// 20
		return amountRounded;
		
	}

	public synchronized CurrencyExchange updateCurrencyExchange(Currency currencyFrom, Currency currencyTo, Double rate) {
		
		CurrencyExchange currencyExchange = this.repository.findByCurrencyFromAndCurrencyTo(currencyFrom, currencyTo);

		if (currencyExchange == null) {
			
			currencyExchange = new CurrencyExchange();
			currencyExchange.setCurrencyFrom(currencyFrom);
			currencyExchange.setCurrencyTo(currencyTo);
			currencyExchange.setLastUpdateDate(LocalDateTime.now());
			currencyExchange.setRate(rate);
			
			logger.info("Creating currencyExchange: " + new Gson().toJson(currencyExchange));
			currencyExchange = this.repository.save(currencyExchange);
		}
		else if (!currencyExchange.getRate().equals(rate)) {
			
			currencyExchange.setLastUpdateDate(LocalDateTime.now());
			currencyExchange.setRate(rate);
				
			currencyExchange = this.repository.save(currencyExchange);
			logger.info("Updating currencyExchange: " + new Gson().toJson(currencyExchange));
		}
		
		return currencyExchange;
	}
	

}