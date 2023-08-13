package com.example.weatherapi;

import com.example.weatherapi.rep.com.met.Met;
import com.example.weatherapi.repository.com.smhi.Smhi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

@Service
public class ServiceWS {


    public String returnBestWeather () throws Exception {
        String smhi = getWeather("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json");
        String met = getWeather("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300");

        Smhi smhiOb = new ObjectMapper().readerFor(Smhi.class).readValue(smhi);
        Met metOb = new ObjectMapper().readerFor(Met.class).readValue(met);

        String smhiTime = smhiOb.getTimeSeries().get(26).getValidTime();
        String smhiTemp = smhiOb.getTimeSeries().get(24).getParameters().get(1).getValues().toString();
        //Otherwise the format is [26] example
        smhiTemp = smhiTemp.charAt(1) + "" + smhiTemp.charAt(2);
        String smhiHumidity = smhiOb.getTimeSeries().get(24).getParameters().get(5).getValues().toString();
        smhiHumidity = smhiHumidity.charAt(1) + "" + smhiHumidity.charAt(2);

        String metTime = metOb.getProperties().getTimeseries().get(26).getTime();
        Double metTemp = metOb.getProperties().getTimeseries().get(4).getData().getInstant().getDetails().getAirTemperature();
        Double metHumidity = metOb.getProperties().getTimeseries().get(24).getData().getInstant().getDetails().getRelativeHumidity();

        printToTerminal(smhiTime, smhiTemp, smhiHumidity, metTime, metTemp, metHumidity);

        if (parseDouble(smhiTemp) >= metTemp) {
            return "The best weather is SMHI Time: " + smhiTime + " TEMP: " + smhiTemp + " HUMIDITY: " + smhiHumidity;
        } else {
            return "The best weather is MET: " + metTime + " TEMP: " + metTemp + " HUMIDITY: " + metHumidity;
        }

    }

    public bestWeather returnJsonWeather () throws Exception {
        String smhi = getWeather("https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/18.0300/lat/59.3110/data.json");
        String met = getWeather("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=59.3110&lon=18.0300");

        Smhi smhiOb = new ObjectMapper().readerFor(Smhi.class).readValue(smhi);
        Met metOb = new ObjectMapper().readerFor(Met.class).readValue(met);

        String smhiTime = smhiOb.getTimeSeries().get(26).getValidTime();
        String smhiTemp = smhiOb.getTimeSeries().get(24).getParameters().get(1).getValues().toString();
        //Otherwise the format is [26] example
        smhiTemp = smhiTemp.charAt(1) + "" + smhiTemp.charAt(2);
        String smhiHumidity = smhiOb.getTimeSeries().get(24).getParameters().get(5).getValues().toString();
        smhiHumidity = smhiHumidity.charAt(1) + "" + smhiHumidity.charAt(2);

        String metTime = metOb.getProperties().getTimeseries().get(26).getTime();
        Double metTemp = metOb.getProperties().getTimeseries().get(4).getData().getInstant().getDetails().getAirTemperature();
        Double metHumidity = metOb.getProperties().getTimeseries().get(24).getData().getInstant().getDetails().getRelativeHumidity();

        printToTerminal(smhiTime, smhiTemp, smhiHumidity, metTime, metTemp, metHumidity);

        if (parseDouble(smhiTemp) >= metTemp) {
            bestWeather b = new bestWeather(smhiTime, smhiTemp, smhiHumidity);
            return b;
        } else {
            bestWeather c = new bestWeather(metTime, metTemp.toString(), metHumidity.toString());
            return c;
        }
    }

    public String getWeather (String url) throws Exception {
        return turnJsonFromUrlToString(new URL(url));
    }

    public String turnJsonFromUrlToString (URL url) throws IOException {

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

    public void printToTerminal (String smhiTime, String smhiTemp, String smhiHumidity, String metTime, Double metTemp, Double metHumidity) {
        System.out.println(smhiTime);
        System.out.println(smhiTemp);
        System.out.println(smhiHumidity);

        System.out.println(metTime);
        System.out.println(metTemp);
        System.out.println(metHumidity);
    }

}
