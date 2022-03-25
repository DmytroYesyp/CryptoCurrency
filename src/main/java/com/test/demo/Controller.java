package com.test.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping()
public class Controller {

    private final Service service;
    private final Repos repos;

    @Autowired
    public Controller(Service service, Repos repos) {
        this.service = service;
        this.repos = repos;
    }

    @GetMapping(path = "cryptocurrencies/minprice{name}")
    public ResponseEntity<Currency> theSmallest(@RequestParam("name") String currencyType) {

        return ResponseEntity.ok().body(service.getTheSmallest(currencyType));
    }

    @GetMapping(path = "cryptocurrencies/maxprice{name}")
    public ResponseEntity<Currency> theBiggest(@RequestParam("name") String currencyType) {
        return ResponseEntity.ok().body(service.getTheBiggest(currencyType));
    }

    @GetMapping(path = {"cryptocurrencies{name}{page}{size}", "cryptocurrencies{name}"})
    public ResponseEntity<List<Currency>> getPages(@RequestParam String name, @RequestParam(required = false) String page, @RequestParam(required = false) String size) {
        if (page == null)
            page = "0";
        if (size == null)
            size = "10";
        return ResponseEntity.ok().body(service.getPages(name, Integer.parseInt(page), Integer.parseInt(size)));
    }

    @GetMapping(value = "cryptocurrencies/csv", produces = "text/csv")
    public ResponseEntity<Currency> exportCSV() {
        String[] csvHeader = {"Cryptocurrency Name", "Min Price", "Max Price"};

        List<List<String>> csvBody = new ArrayList<>();

        List<String> currenc = new ArrayList<>();
        currenc.add("BTC");
        currenc.add("ETH");
        currenc.add("XRP");

        for (int i = 0; i < currenc.size(); i++) {

            if(repos.findItemByCurrencyTypeUnSorted(currenc.get(i)).size() == 0)
                continue;
            csvBody.add(service.HighestAndLowest(currenc.get(i)));
        }

        ByteArrayInputStream byteArrayOutputStream;

        try (
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                CSVPrinter csvPrinter = new CSVPrinter(
                        new PrintWriter(out),
                        CSVFormat.DEFAULT.withHeader(csvHeader)
                )
        ) {
            for (List<String> record : csvBody)
                csvPrinter.printRecord(record);
            csvPrinter.flush();
            byteArrayOutputStream = new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        InputStreamResource fileInputStream = new InputStreamResource(byteArrayOutputStream);

        String csvFileName = "currencies.csv";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + csvFileName);
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");

        return new ResponseEntity(
                fileInputStream,
                headers,
                HttpStatus.CREATED
        );
    }


}
