package com.example.TelegramBotSpringBoot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PairCurrenciesResponsModel {
    @JsonProperty
    String conversion_rate;

    public String getConversionRate() {
        return conversion_rate;
    }

    public void setConversionRate(String conversionRate) {
        this.conversion_rate = conversionRate;
    }
}
