package com.test.demo;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class Exeptions {
    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no such currency")
    public static class NotFoundException extends RuntimeException {}

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "There is no such page")
    public static class ThereIsNoSuchPage extends RuntimeException {}

}
