package com.example.TelegramBotSpringBoot.service;

import com.example.TelegramBotSpringBoot.Currency;

import java.util.HashMap;
import java.util.Map;

public class HasMapCurrencyModeService implements CurrencyModeService {   //<String,BigDecimal>
    private final Map<Long, Currency> originalCurrency = new HashMap<>();
    private final Map<Long, Currency> targetCurrency = new HashMap<>();

    public HasMapCurrencyModeService(){
        System.out.println("HASHMAP MODE is created");
    }

    @Override
    public Currency getOriginalCurrency(long chatId) {
        return originalCurrency.getOrDefault(chatId,Currency.USD);
    }

    @Override
    public Currency getTargetCurrency(long chatId) {
        return targetCurrency.getOrDefault(chatId,Currency.EUR);
    }

    @Override
    public void setOriginalCurrency(long chatId, Currency currency) {
        originalCurrency.put(chatId,currency);
    }

    @Override
    public void setTargetCurrency(long chatId, Currency currency) {
        targetCurrency.put(chatId,currency);
    }
}
