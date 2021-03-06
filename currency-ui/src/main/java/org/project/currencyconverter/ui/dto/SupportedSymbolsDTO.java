package org.project.currencyconverter.ui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonInclude(NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class SupportedSymbolsDTO implements Serializable
{
    private static final long serialVersionUID = -6675181269443412469L;
    private boolean success;
    private Map<String, String> symbols;

    @Override
    public String toString()
    {
        return "SupportedSymbolsDTO{" +
            "success=" + success +
            ", symbols=" + symbols +
            '}';
    }
}
