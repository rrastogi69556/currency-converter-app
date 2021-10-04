package org.project.currencyconverter.ui.dto;

import org.project.currencyconverter.ui.model.ExchangeRates;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/*_________________________________________________________________________________
 | This class is used for transmission of Currency conversion state via REST API   |
 |_________________________________________________________________________________|
*/
@JsonInclude(NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrencyConversionResponseDTO
{
    private String sourceCurrency;
    private ExchangeRates targetCurrency;
    private BigDecimal monetaryValue;
    private String convertedAmount;
}
