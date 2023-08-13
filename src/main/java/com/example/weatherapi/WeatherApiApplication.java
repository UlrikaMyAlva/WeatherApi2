package com.example.weatherapi;

import com.example.weatherapi.rep.com.met.Met;
import com.example.weatherapi.repository.com.smhi.Parameter;
import com.example.weatherapi.repository.com.smhi.Smhi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@SpringBootApplication
public class WeatherApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}
