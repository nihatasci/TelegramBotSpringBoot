package com.example.TelegramBotSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class PairCurrenciesResponsModel {
    @JsonProperty
    BigDecimal conversion_rate;

    public BigDecimal getConversionRate() {
        return conversion_rate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversion_rate = conversionRate;
    }
}
