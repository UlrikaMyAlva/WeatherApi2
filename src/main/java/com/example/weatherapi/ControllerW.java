package com.example.weatherapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//Fungerar ej med restcontroller här?
@Controller
public class ControllerW {

    @Autowired
    ServiceWS serviceWS;

    @GetMapping("/weather")
    public String addWeatherSite (Model m) throws Exception {

        m.addAttribute("info", serviceWS.returnBestWeather());

        return "weather";
    }

}
