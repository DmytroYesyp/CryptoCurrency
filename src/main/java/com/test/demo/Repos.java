package com.test.demo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface Repos extends MongoRepository<Currency, String> {
    @Query("{currencyType:'?0'}")
     List<Currency> findItemByCurrencyType(String currencyType, Sort sort);

    @Query("{currencyType:'?0'}")
    List<Currency> findItemByCurrencyTypeUnSorted(String currencyType);

}
