package com.example.TelegramBotSpringBoot.service;

import com.example.TelegramBotSpringBoot.Currency;

public interface CurrencyModeService {
    static CurrencyModeService getInstance() {return new HasMapCurrencyModeService();}

    Currency getOriginalCurrency(long chatId);

    Currency getTargetCurrency(long chatId);

    void setOriginalCurrency(long chatId, Currency currency);

    void setTargetCurrency(long chatId, Currency currency);

}
