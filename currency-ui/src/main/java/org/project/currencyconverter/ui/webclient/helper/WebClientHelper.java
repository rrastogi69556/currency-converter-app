package org.project.currencyconverter.ui.webclient.helper;

import org.project.currencyconverter.ui.dto.CurrencyConversionRequestDTO;
import org.project.currencyconverter.ui.exception.CurrencyConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class WebClientHelper
{
    private static final String ERROR_NOT_OK_RESPONSE = "Response code %s received. Not a 2xx response type";
    private static final String ERROR_RESPONSE_NULL = "No Response from Api";

    public ResponseEntity<String> getAndFetchConvertApiResponse(String url, HttpEntity<CurrencyConversionRequestDTO> httpEntityRequest,
        RestTemplate restTemplate) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntityRequest, String.class);
    }

    public void throwErrorIfInvalidResponse(ResponseEntity<String> responseEntity) throws CurrencyConversionException,JsonProcessingException {
        CurrencyConversionException exception = null;
        if(isNull(responseEntity)) {
            exception =  new CurrencyConversionException(ERROR_RESPONSE_NULL);
            log.error(String.format(ERROR_RESPONSE_NULL),exception);
            throw exception;
        }

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            exception = new CurrencyConversionException(responseEntity.getBody());
            log.error(String.format(ERROR_NOT_OK_RESPONSE, responseEntity.getStatusCode()), exception);
            throw exception;
        }
    }

    public HttpEntity<CurrencyConversionRequestDTO> convertJsonRequestToHttpEntityRequest(CurrencyConversionRequestDTO jsonRequest, String region) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptLanguage(Collections.singletonList(new Locale.LanguageRange(region)));
        return new HttpEntity<>(jsonRequest, headers);
    }

}
