package org.project.currencyconverter.ui.exception;

import lombok.Getter;

@Getter
public class SupportedSymbolsException extends RuntimeException
{

    private static final long serialVersionUID = 1832533013860608970L;

    public SupportedSymbolsException(String message) {
        super(message);
    }

}
