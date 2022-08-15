package com.example.TelegramBotSpringBoot;

import com.example.TelegramBotSpringBoot.service.CurrencyCorversionService;
import com.example.TelegramBotSpringBoot.service.CurrencyModeService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TestBot extends TelegramLongPollingBot {

    private final CurrencyModeService currecyModeService = CurrencyModeService.getInstance();
    private final CurrencyCorversionService currencyCorversionService = (CurrencyCorversionService) CurrencyCorversionService.getInstance();


    @Value("${telegramBotName}")
    private String userName;

    @Value("${telegramBotToken}")
    private String token;

    @Override
    public String getBotUsername() {
        return userName;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {    //mesaj güncellendiğinde ne olacak metodu
        if(update.hasCallbackQuery()){
            handleCallback(update.getCallbackQuery());
        }
        else if (update.hasMessage()){
            handleMessage(update.getMessage());
        }
    }

    @SneakyThrows
    private void handleCallback(CallbackQuery callbackQuery) {   //kullanıcı döviz cinsi seçimi yapma
        Message message = callbackQuery.getMessage();
        String[] param = callbackQuery.getData().split(":");
        String action = param[0];
        Currency newCurrency = Currency.valueOf(param[1]);
        switch (action){
            case "ORIGINAL":                                                                //1. sütun seçimi
                currecyModeService.setOriginalCurrency(message.getChatId(),newCurrency);
                break;
            case "TARGET":                                                                  //2. sütun seçimi
                currecyModeService.setTargetCurrency(message.getChatId(),newCurrency);
                break;
        }
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        Currency originalCurrency = currecyModeService.getOriginalCurrency(message.getChatId());  //seçilen döviz cinsinin id'sini alma
        Currency targetCurrency = currecyModeService.getTargetCurrency(message.getChatId());


        for (Currency currency: Currency.values()) {   // seçilen dövizleri buttons listesine ekler
            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(getCurrencyButton(originalCurrency,currency)).callbackData("ORIGINAL:"+currency).build(),
                    InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency,currency)).callbackData("TARGET:"+currency).build()));

        }
        execute(EditMessageReplyMarkup.builder()
                .chatId(message.getChatId().toString())
                .messageId(message.getMessageId())
                .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                .build());

    }

    @SneakyThrows
    private void handleMessage(Message message) {  //mesaj gösterimi
        //handle command
        if(message.hasText()&&message.hasEntities()){
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();

            if (commandEntity.isPresent()){
                String command = message.getText().substring(commandEntity.get().getOffset(),commandEntity.get().getLength());

                switch (command){
                    case "/set_currency":  //seçilen metod set_currency ise
                        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

                        Currency originalCurrency = currecyModeService.getOriginalCurrency(message.getChatId());
                        Currency targetCurrency = currecyModeService.getTargetCurrency(message.getChatId());

                        for (Currency currency: Currency.values()) {
                            buttons.add(Arrays.asList(InlineKeyboardButton.builder().text(getCurrencyButton(originalCurrency,currency)).callbackData("ORIGINAL:"+currency).build(),
                                                      InlineKeyboardButton.builder().text(getCurrencyButton(targetCurrency,currency)).callbackData("TARGET:"+currency).build()));
                        }

                            execute(SendMessage.builder() //set_currency metodunda başlık gösterimi
                                    .text("Please choose Original and Target currencies")
                                    .chatId(message.getChatId().toString())
                                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                                    .build());
                }
            }
        }

        if(message.hasText()){  //metoddan sonra yazılan değer için karşılık gelen döviz değeri
            String messageText = message.getText();
            Optional<Double> value = parseDouble(messageText);

            Currency originalCurrency = currecyModeService.getOriginalCurrency(message.getChatId());
            Currency targetCurrency = currecyModeService.getTargetCurrency(message.getChatId());
            BigDecimal ratio = currencyCorversionService.getConversionRatio(originalCurrency, targetCurrency);

            if (value.isPresent()){
                execute(SendMessage.builder()
                        .chatId(message.getChatId().toString())
                        .text(String.format("%4.2f %s is %4.2f %s",value.get(),originalCurrency,(value.get()*ratio.doubleValue()),targetCurrency))
                        .build());
                return;
            }
        }
    }

    private Optional<Double> parseDouble(String messageText){ //metni double sayıya çevirme
        try {
            return Optional.of(Double.parseDouble(messageText));
        }catch (Exception e){
            return Optional.empty();
        }
    }

    private String getCurrencyButton(Currency saved, Currency current){ //seçilen dövizlerin yanında tik işareti gösterir
        return saved == current ? current+ "✅" : current.name();
    }


    public static void main(String[] args) {
        TestBot bot = new TestBot();
        TelegramBotsApi telegramBotsApi = null;
        try {
            telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

}
