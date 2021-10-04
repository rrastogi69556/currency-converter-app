package org.project.currencyconverter.ui.controller;

import org.project.currencyconverter.ui.dto.CurrencyConversionRequestDTO;
import org.project.currencyconverter.ui.dto.CurrencyConversionResponseDTO;
import org.project.currencyconverter.ui.dto.SupportedSymbolsDTO;
import org.project.currencyconverter.ui.exception.CurrencyConversionException;
import org.project.currencyconverter.ui.exception.SupportedSymbolsException;
import org.project.currencyconverter.ui.webclient.WebClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConversionController
{
    private final WebClient webClient;


    @Autowired
    public ConversionController(WebClient webClient)
    {
        this.webClient = webClient;
    }

    public CurrencyConversionResponseDTO getConvertAPI(CurrencyConversionRequestDTO currencyConversionRequestDTO, String region) throws SupportedSymbolsException,
                                                                                                                         CurrencyConversionException, JsonProcessingException
    {
        return webClient.convertCurrency(currencyConversionRequestDTO, region) ;
    }

    public SupportedSymbolsDTO getSupportedSymbols() throws SupportedSymbolsException
    {
        try {

            return webClient.getCurrencies();

        }catch (Exception e){
            throw new SupportedSymbolsException("Unable to fetch Symbols API");
        }
    }

}
