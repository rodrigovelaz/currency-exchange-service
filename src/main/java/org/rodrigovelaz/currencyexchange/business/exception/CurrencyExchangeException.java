package org.rodrigovelaz.currencyexchange.business.exception;

public class CurrencyExchangeException extends Exception {

	private static final long serialVersionUID = 3551504535914807503L;
	private String errorCode;
	private String errorDescription;
	
    public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
	
//	public TopUpException(Throwable e) {
        //super(e);
        //this.errorCode = e.getClass().getSimpleName();
        //this.errorDescription = e.getLocalizedMessage();
        
        //if (e.getClass().equals(SOAPFaultException.class)) {
//        	this.errorDescription = e.getCause().getMessage();
        //}
        //else if (e.getClass().equals(WebServiceException.class)) {
//        	this.errorDescription = e.getCause().getClass().getSimpleName();
        //}
        
  /*      if (WebServiceException.class.isAssignableFrom(e.getClass())) {

        	SOAPFaultException casted = (SOAPFaultException) e;
//        	Throwable tt = casted.getCause();
        	this.errorCode = tt.getClass().getSimpleName();
        	this.errorDescription = tt.getMessage();
        }*/
    //}
	
    public CurrencyExchangeException(Exception e) {
    	super(e);
        this.errorCode = e.getClass().getSimpleName();
        this.errorDescription = e.getLocalizedMessage();
    
    }
	
    public CurrencyExchangeException(String errorDescription) {
    	super(errorDescription);
        this.errorCode = "BusinessException";
        this.errorDescription = errorDescription;
    
    }
    
    /*
	public TopUpException(String errorCode, String errorDescription) {
        //super(errorDescription);
        this.errorCode = errorCode; 
        this.errorDescription = errorDescription;
    }

	public TopUpException(String errorCode, String errorDescription, Throwable cause) {
        //super(errorDescription, cause);
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }*/
	
}
