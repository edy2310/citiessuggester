package com.edgar.citiessuggester.DataStructures;

public enum CityProperties {
    NAME("name"), LATITUDE("latitude"), LONGITUDE("longitude"), COUNTRY("country"), STATE("state");

    private String key;

    public String getKey(){
        return this.key;
    }

    private CityProperties(String key){
        this.key = key;
    }
}