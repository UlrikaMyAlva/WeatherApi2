package com.example.weatherapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RestControllerW {

    @Autowired
    ServiceWS serviceWS;

    @GetMapping("/api")
    bestWeather all () throws Exception {
        bestWeather b = serviceWS.returnJsonWeather();
        return b;
    }

}
