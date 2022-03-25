package com.test.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "api/")
public class Controller {
    private final Service service;
    @Autowired
    public Controller(Service service) {
        this.service = service;
    }

    @PostMapping(path = "test")
    public ResponseEntity<Currency> test(@RequestBody Currency currency){
        service.addTo(currency);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/test").toUriString());
        return ResponseEntity.created(uri).body(null);
    }

    @GetMapping(path = "GetTheSmallet/{Type}")
    public ResponseEntity<Currency> theSmallest(@PathVariable("Type") String currencyType){
        return ResponseEntity.ok().body(service.getTheSmallest(currencyType));
    }

    @GetMapping(path = "GetTheBiggest/{Type}")
    public ResponseEntity<Currency> theBiggest(@PathVariable("Type") String currencyType){
        return ResponseEntity.ok().body(service.getTheBiggest(currencyType));
    }

    @GetMapping(path = {"GetPages/{count}/{size}","GetPages/"})
    public ResponseEntity<List<Currency>> getPages(@PathVariable(required = false)  String count,@PathVariable(required = false) String size){
        if (count == null)
            count = "0";
        if (size == null)
            size = "10";
        return ResponseEntity.ok().body(service.getPages(Integer.valueOf(count),Integer.valueOf(size)));
    }

}
