package org.rodrigovelaz.currencyexchangeservice.business.service.impl;

import java.time.LocalDateTime;
import java.util.Currency;

import org.apache.log4j.Logger;
import org.rodrigovelaz.currencyexchangeservice.business.exception.CurrencyExchangeException;
import org.rodrigovelaz.currencyexchangeservice.business.service.CurrencyExchangeService;
import org.rodrigovelaz.currencyexchangeservice.persistence.model.CurrencyExchange;
import org.rodrigovelaz.currencyexchangeservice.persistence.repository.CurrencyExchangeRepository;
import org.rodrigovelaz.currencyexchangeservice.presentation.json.response.CurrencyExchangeResponseJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;

@Service
@Transactional
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

	private Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private CurrencyExchangeRepository currencyExchangeRepository;
	
	private CurrencyExchangeResponseJson create(Currency currencyFrom, Currency currencyTo, Double amountFrom, Double rate) {
		
		Double amountToRounded = this.roundUp(amountFrom * rate, currencyTo);
		
		CurrencyExchangeResponseJson response = new CurrencyExchangeResponseJson();
		response.setAmountFrom(amountFrom);
		response.setCurrencyFrom(currencyFrom);
		response.setAmountTo(amountToRounded);
		response.setCurrencyTo(currencyTo);
		response.setRate(rate);
		return response;
	}
	
	
	@Override
	public CurrencyExchange findByFromAndTo(Currency currencyFrom, Currency currencyTo) throws CurrencyExchangeException {
		return this.currencyExchangeRepository.findByFromAndTo(currencyFrom, currencyTo);
	}
	
	@Override
	public CurrencyExchangeResponseJson exchange(Currency currencyFrom, Currency currencyTo, Double amount) throws CurrencyExchangeException {

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
		CurrencyExchange currencyExchange = this.currencyExchangeRepository.findByFromAndTo(currencyFrom, currencyTo);
		if (currencyExchange != null) {
			return currencyExchange.getRate();
		}
		
		// Inverse rate
		currencyExchange = this.currencyExchangeRepository.findByFromAndTo(currencyTo, currencyFrom);
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

	@Override
	public synchronized CurrencyExchange updateCurrencyExchange(Currency currencyFrom, Currency currencyTo, Double rate) {
		
		CurrencyExchange currencyExchange = this.currencyExchangeRepository.findByFromAndTo(currencyFrom, currencyTo);

		if (currencyExchange == null) {
			
			currencyExchange = new CurrencyExchange();
			currencyExchange.setCurrencyFrom(currencyFrom);
			currencyExchange.setCurrencyTo(currencyTo);
			currencyExchange.setLastUpdateDate(LocalDateTime.now());
			currencyExchange.setRate(rate);
			
			logger.info("Creating currencyExchange: " + new Gson().toJson(currencyExchange));
			currencyExchange = this.currencyExchangeRepository.save(currencyExchange);
		}
		else if (!currencyExchange.getRate().equals(rate)) {
			
			currencyExchange.setLastUpdateDate(LocalDateTime.now());
			currencyExchange.setRate(rate);
				
			currencyExchange = this.currencyExchangeRepository.save(currencyExchange);
			logger.info("Updating currencyExchange: " + new Gson().toJson(currencyExchange));
		}
		
		return currencyExchange;
	}
	

}