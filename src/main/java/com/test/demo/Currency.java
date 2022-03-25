package com.test.demo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Comparator;


@Document("currency")
public class Currency {
    @JsonIgnore
    @Id
    private String id;

    private String currencyType;
    private Double value;

    public Currency(String currencyType, Double value) {
        this.currencyType = currencyType;
        this.value = value;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }


    public static Comparator<Currency> ValueComporator
            = new Comparator<Currency>() {

        public int compare(Currency first, Currency second) {

            return first.getValue().compareTo(second.getValue());
        }
    };
}

