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

    @Autowired
    RestTemplate restTemplate;


    public String getProductList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String result = restTemplate.getForObject("https://v6.exchangerate-api.com/v6/yourapi/latest/USD",String.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }


    public PairCurrenciesResponsModel currienciesRatio(@PathVariable String input1, @PathVariable String input2 ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        PairCurrenciesResponsModel result = restTemplate.getForObject("https://v6.exchangerate-api.com/v6/yourapikey/pair/"+input1+"/"+input2,PairCurrenciesResponsModel.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }






    @Override
    public BigDecimal getConversionRatio(Currency original, Currency target) {

        BigDecimal originalRate = getRate(original);
        BigDecimal targetRate = getRate(target);
        return originalRate.divide(targetRate);
    }
@SneakyThrows
    private BigDecimal getRate(Currency currency) {
        if(currency == Currency.USD){
            return BigDecimal.valueOf(1);
        }
        URL url = new URL("https://v6.exchangerate-api.com/v6/yourapi key/latest/"+currency.name());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputline;
        StringBuilder response = new StringBuilder();
        while ((inputline = in.readLine())!= null){
            response.append(inputline);
        }
        in.close();
        JSONObject json = new JSONObject(response.toString());
        BigDecimal rate = json.getBigDecimal("conversion_rates"); //oranı
        BigDecimal scale = json.getBigDecimal("Cur_Scale"); //kur ölçeği
        return rate.divide(scale);
    }


   /* @Autowired
    RestTemplate restTemplate;

    @GetMapping (value = "getcurrencies")
    public String getProductList() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        String result = restTemplate.getForObject("https://v6.exchangerate-api.com/v6/yourapikey/latest/USD",String.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }

    @GetMapping(value = "paircurrencies/{input1}/{input2}")
    public PairCurrenciesResponsModel getProductListtwo(@PathVariable String input1, @PathVariable String input2 ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        //HttpEntity<String> entity = new HttpEntity<String>(headers);

        PairCurrenciesResponsModel result = restTemplate.getForObject("https://v6.exchangerate-api.com/v6/yourapikey/pair/"+input1+"/"+input2,PairCurrenciesResponsModel.class);//exchange("https://v6.exchangerate-api.com/v6/2d6e1210b20ea181c5a6f08c/latest/USD", HttpMethod.GET, entity, String.class).getBody();

        return result;
    }*/

}
