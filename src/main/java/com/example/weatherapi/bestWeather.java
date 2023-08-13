package com.example.weatherapi;

public class bestWeather {

    private String time;
    private String temp;
    private String humidity;

    public bestWeather(){

    }

    public bestWeather(String time, String temp, String humidity) {
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
}

