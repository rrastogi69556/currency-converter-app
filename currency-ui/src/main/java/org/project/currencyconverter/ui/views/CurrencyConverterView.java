package org.project.currencyconverter.ui.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.project.currencyconverter.ui.controller.ConversionController;
import org.project.currencyconverter.ui.dto.CurrencyConversionRequestDTO;
import org.project.currencyconverter.ui.dto.CurrencyConversionResponseDTO;
import org.project.currencyconverter.ui.dto.SupportedSymbolsDTO;
import org.project.currencyconverter.ui.exception.ApiResponse;
import org.project.currencyconverter.ui.exception.CurrencyConversionException;
import org.project.currencyconverter.ui.exception.SupportedSymbolsException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@PageTitle("Currency Converter")
@Route(value = "currency-converter", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Slf4j
public class CurrencyConverterView extends HorizontalLayout {

    private static final String amountHelperText = "Please enter the amount (in digits) to convert";
    private static final long serialVersionUID = 8380387526763418830L;
    protected static final String SPACE = " ";
    protected static final String EQUALS = "= ";
    protected static final int DURATION = 5000;
    private BigDecimalField amount;
    private Button convert;
    private final ComboBox<String> sourceCurrencyList = new ComboBox<>();
    private final ComboBox<String> targetCurrencyList = new ComboBox<>();
    private final ComboBox<String> acceptedLanguageList = new ComboBox<>();
    private String source;
    private String target;

    private String region;
    private final Div result = new Div();
    private Set<String> currencies;
    private ObjectMapper jsonMapper;

    @Autowired
    public CurrencyConverterView(ConversionController conversionController, @Value("#{${locale.map}}") Map<String,String> localesMap, ObjectMapper jsonMapper) {
        addClassName("currency-converter-view");
        this.jsonMapper = jsonMapper;

        addAmountUIComponent();

        addSourceCurrencyUIComponent(conversionController);

        addTargetCurrencyUIComponent(conversionController);

        addLocaleUIComponent(localesMap);

        addConvertButtonUI(conversionController);

        add(amount, sourceCurrencyList, targetCurrencyList, acceptedLanguageList, convert, result);
        setVerticalComponentAlignment(Alignment.END, amount, result);
        setAlignItems(Alignment.CENTER);
    }


    private void addConvertButtonUI(ConversionController conversionController)
    {
        convert = new Button("Convert");
        convert.addClickListener(e -> {
            if(uiComponentNotInitialized()) {
                Notification.show("Please select/enter valid values", DURATION, Notification.Position.TOP_CENTER);
            } else
            {
                try
                {
                    CurrencyConversionResponseDTO currencyConversionResponseDTO = conversionController.getConvertAPI(new CurrencyConversionRequestDTO(source, target,
                        amount.getValue()), region);
                    if (nonNull(currencyConversionResponseDTO))
                    {
                        String finalResult =
                            String.valueOf(currencyConversionResponseDTO.getMonetaryValue())
                                .concat(SPACE)
                                .concat(currencyConversionResponseDTO.getSourceCurrency())
                                .concat(EQUALS)
                                .concat(currencyConversionResponseDTO.getConvertedAmount())
                                .concat(SPACE)
                                .concat(currencyConversionResponseDTO.getTargetCurrency().getTargetCurrency());

                        result.setText(finalResult);
                    }
                    else
                    {
                        Notification.show("Unable to fetch the converted amount. Please check backend app is running", DURATION, Notification.Position.TOP_CENTER);
                    }
                } catch(SupportedSymbolsException | CurrencyConversionException | JsonProcessingException | RestClientException exception) {
                    log.error(exception.getLocalizedMessage(), exception);
                    Notification.show(exception.getMessage(), DURATION, Notification.Position.TOP_CENTER);

                }
            }
        });
    }


    private boolean uiComponentNotInitialized()
    {
        return isNull(source)
            || isNull(target)
            || isNull(amount.getValue())
            || isNull(region)
            || BigDecimal.ZERO.equals(amount.getValue());
    }


    private void addLocaleUIComponent(Map<String, String> localesMap)
    {
        acceptedLanguageList.setPlaceholder("Choose region");
        acceptedLanguageList.setLabel("Region");
        acceptedLanguageList.setHelperText("Please select your region for currency format");
        acceptedLanguageList.setItems(localesMap.keySet());
        acceptedLanguageList.addValueChangeListener(event -> {
            acceptedLanguageList.setClearButtonVisible(true);
            if (!isNull(event.getValue())) {
                region = localesMap.get(event.getValue());
            }else {
                region = null;
            }
        });
    }


    private void fetchCurrencyListOnClick(ConversionController conversionController, ComboBox<String> currencyList, boolean isTarget)
    {
        currencyList.addFocusListener(event -> {
            try
            {
                fetchSymbolAPI(conversionController);
                currencyList.setItems(currencies);
            } catch(SupportedSymbolsException supportedSymbolsException) {
                Notification.show(supportedSymbolsException.getMessage(), DURATION, Notification.Position.TOP_CENTER);
            }
        });
        currencyList.addValueChangeListener(event -> {
            currencyList.setClearButtonVisible(true);
            if(!isNull(event.getValue())) {
                setCurrency(isTarget, event.getValue());
            }
            else {
                setCurrency(isTarget, null);
            }
        });
    }


    private void setCurrency(boolean isTarget, String currencyValue)
    {
        if (isTarget)
        {
            this.target = currencyValue;
        }
        else
        {
            this.source = currencyValue;
        }
    }


    private void addTargetCurrencyUIComponent(ConversionController conversionController)
    {
        targetCurrencyList.setPlaceholder("Choose Currency");
        targetCurrencyList.setLabel("To");
        targetCurrencyList.setHelperText("Please choose target currency");
        fetchCurrencyListOnClick(conversionController, targetCurrencyList, true);
    }

    private void addSourceCurrencyUIComponent(ConversionController conversionController)
    {
        sourceCurrencyList.setPlaceholder("Choose Currency");
        sourceCurrencyList.setHelperText("Please choose base currency");
        sourceCurrencyList.setLabel("From");
        fetchCurrencyListOnClick(conversionController, sourceCurrencyList, false);
    }


    private void addAmountUIComponent()
    {
        amount = new BigDecimalField("Amount");
        amount.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        amount.setHelperText(amountHelperText);
        amount.addValueChangeListener(listener -> {
            amount.setClearButtonVisible(true);
           if(isNull(listener.getValue())) {
               amount.setValue(BigDecimal.ZERO);
           }
        });
    }


    private void fetchSymbolAPI(ConversionController conversionController)
    {
        if(CollectionUtils.isEmpty(currencies))
        {
            SupportedSymbolsDTO supportedSymbolsDTO = conversionController.getSupportedSymbols();
            this.currencies = supportedSymbolsDTO.getSymbols().keySet();
        }
    }
}
