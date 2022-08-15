package com.example.TelegramBotSpringBoot.service;

import com.example.TelegramBotSpringBoot.Currency;

import java.math.BigDecimal;

public interface CurrencyCorversionService { //apiden değer almak için instance
    static ConsumeWebServise getInstance() {
        return new ConsumeWebServise();
    }

    BigDecimal getConversionRatio(Currency original, Currency target);
}
