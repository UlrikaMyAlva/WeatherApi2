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

/*
PROJEKTPLANERING

ÖVERGRIPANDE MÅL

-- Skapa en väder API som visar det bästa vädret i Liljeholmen --



STORA MÅL

** Skapa en frontend **
    -- Skapa en Thymeleaf sida --


** VÄRDENA **
    -- Få ut rätt värden ur SMHI --
    -- Få ut rätt värden ur MET --

** SKAPA EN CONTROLLER **
    -- Controller är lagret mellan webben och din ditt service layer --
        .. Funktion "till Thymeleaf (se ditt tidigare projet) ..

** SKAPA EN SERVICE **
    -- Service är lagret mellan controllern och din applikation --
        .. Skapa en funktion som hämtar datan och skickar den till controller funktionern till thymeleaf ..


 */

@SpringBootApplication
public class WeatherApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        LocalDateTime l = gettime();
        roundUpTwelveHours(l);

        WebClient client = WebClient.create();


        //This down do not get the " " quotation marks that is needed for the
        //JSONParser to recognize JSON
        Mono<SmhiWeather> m3 = client
                .get()
                .uri("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json")
                .retrieve()
                .bodyToMono(SmhiWeather.class);

        List<SmhiWeather> w = client.get().uri("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json")
                .retrieve()
                .bodyToFlux(SmhiWeather.class)
                .collectList()
                .block();

        //Write to your file
        String stringWithAllData = w.stream()
                .map(n -> String.valueOf(n))
                .collect(Collectors.joining());



        //SENDS URL; RETURNS STRING
        String smhi2 = turnJsonFromUrlToString(new URL("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json"));
        String met = turnJsonFromUrlToString(new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300"));

        System.out.println(smhi2);
        System.out.println("BREAK");
        System.out.println(met);

        /*
        //Using the JSON Simple library to parse the string into a json object
        JSONParser parser = new JSONParser();

        JSONObject data_obj = (JSONObject) parser.parse(inline);

        JSONArray arr = (JSONArray) data_obj.get("timeSeries");
        //System.out.println(arr);

        for (int i = 0; i < arr.size(); i++) {
            JSONObject new_obj = (JSONObject) arr.get(i);
           // System.out.println(new_obj.toString());

            //MAKE SURE THE DATE IS TODAY
            if (new_obj.get("validTime").equals("2023-06-09T15:00:00Z")) {
                //Put this parameter in a json Object?
                JSONArray parr = (JSONArray) new_obj.get("parameters");
                for (int j = 0; j < parr.size(); j++) {
                    JSONObject pobj = (JSONObject) parr.get(j);

                    if (pobj.get("name").equals("t")) {
                        System.out.println(pobj.get("values"));
                    }

                }

                break;
            }

        }

        */

        //OBS stringwithalldata är en lista? Funkar kanske ej?
        Smhi smhi = new ObjectMapper().readerFor(Smhi.class).readValue(smhi2);

        Met met2 = new ObjectMapper().readerFor(Met.class).readValue(met);

        //GET TIME; TEMP; HUMIDITY
        System.out.println(smhi.getTimeSeries().get(24).getValidTime());
        System.out.println(smhi.getTimeSeries().get(24).getParameters().get(1).getValues());
        System.out.println(smhi.getTimeSeries().get(24).getParameters().get(5).getValues());


        System.out.println(met2.getProperties().getTimeseries().get(24).getTime());
        System.out.println(met2.getProperties().getTimeseries().get(4).getData().getInstant().getDetails().getAirTemperature());
        System.out.println(met2.getProperties().getTimeseries().get(24).getData().getInstant().getDetails().getRelativeHumidity());

    }

    public String turnJsonFromUrlToString (URL url) throws IOException {

        //URL url = new URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        String inline = "";
        Scanner scan = new Scanner(url.openStream());

        while (scan.hasNext()) {
            inline += scan.nextLine();
        }

        scan.close();

        return inline;
    }

    public LocalDateTime gettime () {
        return LocalDateTime.now();
    }

    public String roundUpTwelveHours (LocalDateTime l) {
        l = l.plusHours(12);
        l = l.withMinute(00);
        l = l.withSecond(00);
        String ls = l.toString();
        StringBuffer sb = new StringBuffer(l.toString());
        //Tar bara bort char -10 (:)
        sb.deleteCharAt(sb.length()-10);
        System.out.println(sb);
        return sb.toString();
    }


}
