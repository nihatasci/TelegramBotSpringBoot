package com.example.TelegramBotSpringBoot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Currency {
    USD(0),EUR(54),TRY(151);

    private final int id;
}
