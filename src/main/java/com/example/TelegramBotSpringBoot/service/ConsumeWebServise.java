package com.example.TelegramBotSpringBoot.service;

import com.example.TelegramBotSpringBoot.Currency;
import com.example.TelegramBotSpringBoot.model.PairCurrenciesResponsModel;
import com.example.TelegramBotSpringBoot.service.CurrencyCorversionService;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class ConsumeWebServise implements CurrencyCorversionService {

    RestTemplate restTemplate = new RestTemplate();
    String url = "https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c";

    public String getCurrencyInfo() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String result = restTemplate.getForObject(url+"/latest/USD",String.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }


    public PairCurrenciesResponsModel currienciesRatio(@PathVariable String input1, @PathVariable String input2 ) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        PairCurrenciesResponsModel result = restTemplate.getForObject(url+"/pair/"+input1+"/"+input2,PairCurrenciesResponsModel.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }

    @Override
    public BigDecimal getConversionRatio(Currency original, Currency target) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        PairCurrenciesResponsModel result = restTemplate.getForObject(url+"/pair/"+original.name()+"/"+target.name(),PairCurrenciesResponsModel.class);
        return result.getConversionRate();
    }
}
