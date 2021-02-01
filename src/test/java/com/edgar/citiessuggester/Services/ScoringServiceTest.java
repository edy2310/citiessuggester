package com.edgar.citiessuggester.Services;

import com.edgar.citiessuggester.DataStructures.CityProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class ScoringServiceTest {

    ScoringService scoringService = new ScoringService();

    private static HashMap<String, Object> suggestedData = new HashMap<>();
    private HashMap<String, String> inputData = new HashMap<>();

    @BeforeAll
    public static void setup(){
        suggestedData.put(CityProperties.NAME.getKey(), "montreal");
        suggestedData.put(CityProperties.STATE.getKey(), "QC");
        suggestedData.put(CityProperties.COUNTRY.getKey(), "canada");
        suggestedData.put(CityProperties.LONGITUDE.getKey(), -14.85);
        suggestedData.put(CityProperties.LATITUDE.getKey(), 12.10);
    }

    @BeforeEach
    public void beforeEachTest(){
        inputData = new HashMap<>();
    }

    @Test
    public void testNotEqualState(){
        inputData.put(CityProperties.NAME.getKey(), "mont");
        inputData.put(CityProperties.STATE.getKey(), "BC");
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(0, score);
    }

    @Test
    public void testNotEqualCountry(){
        inputData.put(CityProperties.NAME.getKey(), "mont");
        inputData.put(CityProperties.COUNTRY.getKey(), "USA");
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(0, score);
    }

    @Test
    public void testOnlySameCityNoCoordinates(){
        inputData.put(CityProperties.NAME.getKey(), "montreal");
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(0.2, score);
    }

    @Test
    public void testOnlySameCountryNoCoordinates(){
        inputData.put(CityProperties.NAME.getKey(), "montreal");
        inputData.put(CityProperties.COUNTRY.getKey(), "canada");
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(0.4, score);
    }

    @Test
    public void testOnlySameStateNoCoordinates(){
        inputData.put(CityProperties.NAME.getKey(), "montreal");
        inputData.put(CityProperties.STATE.getKey(), "QC");
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(0.6, score);
    }

    @Test
    public void testAllEquals(){
        inputData.put(CityProperties.NAME.getKey(), "montreal");
        inputData.put(CityProperties.STATE.getKey(), "QC");
        inputData.put(CityProperties.COUNTRY.getKey(), "canada");
        inputData.put(CityProperties.LONGITUDE.getKey(), String.valueOf(-14.85));
        inputData.put(CityProperties.LATITUDE.getKey(), String.valueOf(12.10));
        double score = scoringService.getScore(inputData, suggestedData);
        assertEquals(1, score);
    }
}
