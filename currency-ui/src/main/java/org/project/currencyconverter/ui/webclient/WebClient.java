package org.project.currencyconverter.ui.webclient;

import org.project.currencyconverter.ui.builder.UrlBuilder;
import org.project.currencyconverter.ui.dto.CurrencyConversionRequestDTO;
import org.project.currencyconverter.ui.dto.CurrencyConversionResponseDTO;
import org.project.currencyconverter.ui.dto.SupportedSymbolsDTO;
import org.project.currencyconverter.ui.exception.CurrencyConversionException;
import org.project.currencyconverter.ui.webclient.helper.WebClientHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class WebClient
{
    private final RestTemplate restTemplate;
    private final String backendUrl;
    private final WebClientHelper webClientHelper;
    private final ObjectMapper jsonMapper;

    private static final String CONVERT_API_URL = "/v1/currency-converter/convert";
    private static final String SYMBOLS_API_URL = "/v1/currency-converter/symbols";

    @Autowired
    public WebClient(RestTemplate restTemplate,
        @Value("${currency.backend.url:http://localhost:8080}") String backendUrl,
        WebClientHelper webClientHelper,
        ObjectMapper jsonMapper
        )
    {
        this.restTemplate = restTemplate;
        this.backendUrl = backendUrl;
        this.webClientHelper = webClientHelper;
        this.jsonMapper = jsonMapper;
    }

    public SupportedSymbolsDTO getCurrencies() {
        String url = UrlBuilder.builder()
            .withBaseUrl(backendUrl)
            .withPathParameter(SYMBOLS_API_URL)
            .build();

        ResponseEntity<SupportedSymbolsDTO> returnedSymbols = restTemplate.getForEntity(url , SupportedSymbolsDTO.class);
        return returnedSymbols.getBody();

    }


    public CurrencyConversionResponseDTO convertCurrency(CurrencyConversionRequestDTO currencyConversionRequestDTO, String region) throws CurrencyConversionException,
                                                                                                                                 JsonProcessingException
    {
        HttpEntity<CurrencyConversionRequestDTO> httpEntityRequest = webClientHelper.convertJsonRequestToHttpEntityRequest(currencyConversionRequestDTO, region);
        String url = UrlBuilder.builder()
            .withBaseUrl(backendUrl)
            .withPathParameter(CONVERT_API_URL)
            .withRequestParameter1("sourceCurrency", currencyConversionRequestDTO.getSourceCurrency())
            .withOtherRequestParameter("targetCurrency", currencyConversionRequestDTO.getTargetCurrency())
            .withOtherRequestParameter("monetaryValue", currencyConversionRequestDTO.getMonetaryValue())
            .build();

        ResponseEntity<String> convertResponse = webClientHelper.getAndFetchConvertApiResponse(url,
            httpEntityRequest, restTemplate);
        webClientHelper.throwErrorIfInvalidResponse(convertResponse);
        return convertJsonStringTODTO(convertResponse);
    }

    private CurrencyConversionResponseDTO convertJsonStringTODTO(ResponseEntity<String> responseEntity) throws JsonProcessingException
    {
        return jsonMapper.readValue(responseEntity.getBody(), CurrencyConversionResponseDTO.class);
    }
}
