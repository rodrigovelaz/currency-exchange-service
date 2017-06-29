package org.rodrigovelaz.currencyexchangeservice.presentation.json.response;

import java.util.Currency;

public class CurrencyExchangeResponseJson {

	private Double amountFrom;
	private Currency currencyFrom;
	private Double amountTo;
	private Currency currencyTo;
	private Double rate;
	
	public Double getAmountFrom() {
		return amountFrom;
	}
	public void setAmountFrom(Double amountFrom) {
		this.amountFrom = amountFrom;
	}
	public Currency getCurrencyFrom() {
		return currencyFrom;
	}
	public void setCurrencyFrom(Currency currencyFrom) {
		this.currencyFrom = currencyFrom;
	}
	public Double getAmountTo() {
		return amountTo;
	}
	public void setAmountTo(Double amountTo) {
		this.amountTo = amountTo;
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
