package org.project.currencyconverter.ui.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRates implements Serializable
{
    private static final long serialVersionUID = -889680701091813811L;
    private String targetCurrency;
    private BigDecimal exchangeRate;


    @Override
    public String toString()
    {
        return "ExchangeRates{" +
            "currency='" + targetCurrency + '\'' +
            ", exchangeRate=" + exchangeRate +
            '}';
    }
}
