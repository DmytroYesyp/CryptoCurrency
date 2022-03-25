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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Currency{" +
                "id='" + id + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", value=" + value +
                '}';
    }
    public Double compareTo(Currency currency) {

        Double compareQuantity = ((Currency) currency).getValue();

        return this.value - compareQuantity;

    }

    public static Comparator<Currency> ValueComporator
            = new Comparator<Currency>() {

        public int compare(Currency first, Currency second) {

            return first.getValue().compareTo(second.getValue());
        }
    };
}

