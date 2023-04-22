package org.rodrigovelaz.currencyexchange.persistence.entity;

import java.time.LocalDateTime;
import java.util.Currency;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class CurrencyExchange {

	@Id
	@GeneratedValue
	protected Long id;

	private LocalDateTime lastUpdateDate;
	
	private Currency currencyFrom;
	
	private Currency currencyTo;
	
	private Double rate;

	public Long getId() { 
		return id; 
	}
	
	public void setId(Long id) { 
		this.id = id; 
	}


	public LocalDateTime getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Currency getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(Currency currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public Currency getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(Currency currencyTo) {
		this.currencyTo = currencyTo;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}
	
}
