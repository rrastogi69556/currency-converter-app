package org.project.currencyconverter.ui.exception;

import lombok.Getter;

@Getter
public class CurrencyConversionException extends RuntimeException
{
    public CurrencyConversionException(String message) {
        super(message);
    }
}
