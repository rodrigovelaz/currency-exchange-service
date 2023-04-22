package org.rodrigovelaz.currencyexchangeservice.presentation.controller;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.rodrigovelaz.currencyexchangeservice.business.exception.CurrencyExchangeException;
import org.rodrigovelaz.currencyexchangeservice.business.service.CurrencyExchangeService;
import org.rodrigovelaz.currencyexchangeservice.persistence.entity.CurrencyExchange;
import org.rodrigovelaz.currencyexchangeservice.presentation.json.request.UpdateRateRequest;
import org.rodrigovelaz.currencyexchangeservice.presentation.json.response.CurrencyExchangeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange")
public class CurrencyExchangeController {
	
	@Autowired
	private CurrencyExchangeService currencyService;
	
	@RequestMapping(method=RequestMethod.GET, value="/from/{currencyFrom}/to/{currencyTo}/{amountFrom:.+}")
    public CurrencyExchangeResponse exchange(@PathVariable Currency currencyFrom, @PathVariable Currency currencyTo, @PathVariable Double amountFrom) throws CurrencyExchangeException {
	
		return this.currencyService.exchange(currencyFrom, currencyTo, amountFrom);
    }
	
	@RequestMapping(method=RequestMethod.GET, value="/from/{currencyFrom}/to/{currencyTo}")
    public List<CurrencyExchangeResponse> exchange(@PathVariable Currency currencyFrom, @PathVariable Currency currencyTo, @RequestParam("amounts") List<Double> amounts) throws CurrencyExchangeException {
	
		List<CurrencyExchangeResponse> response = new ArrayList<CurrencyExchangeResponse>();
		
		for (Double amountFrom : amounts) {
			CurrencyExchangeResponse exc = this.currencyService.exchange(currencyFrom, currencyTo, amountFrom);
			response.add(exc);
		}
		
		return response;
		
    }
	
	@RequestMapping(method=RequestMethod.PUT, value="/from/{currencyFrom}/to/{currencyTo}")
	public CurrencyExchange updateCurrencyExchange(@PathVariable Currency currencyFrom, @PathVariable Currency currencyTo, @RequestBody(required=true) UpdateRateRequest request) {

		CurrencyExchange exc = this.currencyService.updateCurrencyExchange(currencyFrom, currencyTo, request.getRate());
		return exc;
	}

}