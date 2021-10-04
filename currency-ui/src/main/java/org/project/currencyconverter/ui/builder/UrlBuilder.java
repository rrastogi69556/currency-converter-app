package org.project.currencyconverter.ui.builder;

public class UrlBuilder
{
    protected static final String URL_PARAMETER_DELIMITER = "&";
    protected static final String URL_PARAMETER_EQUAL_OPERATOR = "=";
    protected static final String URL_PARAMETER_FIRST_DELIMITER = "?";
    // Using initial size reduces the re-allocation of copying the whole string to new one. This is relatively more soothing to memory. 130 is roughly :
    // http://localhost:8092/v1/currency-converter/convert?sourceCurrency=EUR&targetCurrency=INR&monetaryValue=12345678901234567890
    private final StringBuilder url = new StringBuilder(130);

    public UrlBuilder withBaseUrl(String value) {
        url.append(value);
        return this;
    }

    public UrlBuilder withRequestParameter1(String key, String value) {
        url.append(new StringBuilder()
            .append(URL_PARAMETER_FIRST_DELIMITER)
            .append(key)
            .append(URL_PARAMETER_EQUAL_OPERATOR)
            .append(value));
        return this;
    }

    public UrlBuilder withOtherRequestParameter(String key, Object value) {
        url.append(new StringBuilder()
            .append(URL_PARAMETER_DELIMITER)
            .append(key)
            .append(URL_PARAMETER_EQUAL_OPERATOR)
            .append(value));
        return this;
    }

    public UrlBuilder withPathParameter(String value) {
        url.append(value);
        return this;
    }

    public String build() {
        return url.toString();
    }

    public static UrlBuilder builder() {
        return new UrlBuilder();
    }
}
