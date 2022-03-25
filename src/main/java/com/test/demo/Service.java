package com.test.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import static com.test.demo.Currency.ValueComporator;

@org.springframework.stereotype.Service
public class Service{

    private final Repos repos;

    @Autowired
    public Service(Repos repos){
        this.repos = repos;
    }

    public Currency getTheSmallest(String currencyType){
        if(repos.findItemByCurrencyTypeUnSorted(currencyType).size() == 0)
            throw new Exeptions.NotFoundException();

        return repos.findItemByCurrencyType(currencyType,Sort.by(Sort.Direction.ASC, "value")).get(0);
    }

    public Currency getTheBiggest(String currencyType){
        if(repos.findItemByCurrencyTypeUnSorted(currencyType).size() == 0)
            throw new Exeptions.NotFoundException();

        return repos.findItemByCurrencyType(currencyType,Sort.by(Sort.Direction.DESC, "value")).get(0);
    }

    public List<Currency> getPages(String currencyType,int count, int size){
        if(repos.findItemByCurrencyTypeUnSorted(currencyType).size() == 0)
            throw new Exeptions.NotFoundException();
        try{

            int size2 = repos.findItemByCurrencyTypeUnSorted(currencyType).size();
            int from = count*size;
            int to = count*size+size;
            if (from>size2)
                throw new RuntimeException();
            if (to>size2)
                to = size2;

            List<Currency> obj = repos.findItemByCurrencyTypeUnSorted(currencyType).subList(from,to);
            obj.sort(ValueComporator);
            return obj;
        }catch (RuntimeException ex){
            throw new Exeptions.ThereIsNoSuchPage();
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void timer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                req("https://cex.io/api/last_price/BTC/USD");
                req("https://cex.io/api/last_price/ETH/USD");
                req("https://cex.io/api/last_price/XRP/USD");
            }
        }, 0, (1000 * 60) * 60);
    }



    public List<String> HighestAndLowest(String currency){
        List<String> list = new ArrayList<>();

        List<Currency> toWorkWith = repos.findItemByCurrencyType(currency,Sort.by(Sort.Direction.ASC, "value"));

        list.add(toWorkWith.get(0).getCurrencyType());
        list.add(toWorkWith.get(0).getValue().toString());
        list.add(toWorkWith.get(toWorkWith.size()-1).getValue().toString());
       return list;
    }


    public void req(String input){
        try {
            URL obj = new URL(input);
            HttpURLConnection con = null;
            BufferedReader br = null;
            con = (HttpURLConnection) obj.openConnection();
            String price = "";
            String type = "";
            con.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String out = br.readLine();
            int counter = 0;
            for (int i = 0; i < out.length(); i++) {
                if (out.charAt(i) == '"') {
                    counter++;
                    continue;
                }
                if (counter == 3) {
                    price += out.charAt(i);
                }
                if (counter == 7){
                    type += out.charAt(i);
                }
            }
            Currency curr = new Currency(type,Double.parseDouble(price));
            repos.save(curr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
