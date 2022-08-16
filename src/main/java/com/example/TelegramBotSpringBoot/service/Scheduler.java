package com.example.TelegramBotSpringBoot.service;

import com.example.TelegramBotSpringBoot.Currency;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Component
@EnableScheduling
public class Scheduler {
    @Scheduled(cron = "* * * * * *")
    public void sendMessage() {
        System.out.println("hello");
    }
}
