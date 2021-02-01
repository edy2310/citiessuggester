package com.edgar.citiessuggester.Services;

import com.edgar.citiessuggester.DataStructures.CityProperties;
import com.edgar.citiessuggester.DataStructures.Coordinates;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@Service
public class ScoringService {

    private final int individualProbability = 20;
    private final int scoringDecimals = 2;
    private final int percentageInt = 100;

    public double getScore(HashMap<String, String> inputData, HashMap<String, Object> dataSuggested){
        double score = 0;

        if(
            inputData.containsKey(CityProperties.COUNTRY.getKey()) &&
                !inputData.get(CityProperties.COUNTRY.getKey()).equals(dataSuggested.get(CityProperties.COUNTRY.getKey())) ||
            inputData.containsKey(CityProperties.STATE.getKey()) &&
                !inputData.get(CityProperties.STATE.getKey()).equals(dataSuggested.get(CityProperties.STATE.getKey()))
        )
            return score;

        if(inputData.containsKey(CityProperties.LATITUDE.getKey())){
            double latitudeInput = Double.valueOf(inputData.get(CityProperties.LATITUDE.getKey()));
            double latitudeSuggested= (Double) dataSuggested.get(CityProperties.LATITUDE.getKey());
            score += getLatitudeLongitudePoints(latitudeInput, latitudeSuggested, CityProperties.LATITUDE.getKey());
        }

        if(inputData.containsKey(CityProperties.LONGITUDE.getKey())){
            double longitudeInput = Double.valueOf(inputData.get(CityProperties.LONGITUDE.getKey()));
            double longitudeSuggested= (Double) dataSuggested.get(CityProperties.LONGITUDE.getKey());
            score += getLatitudeLongitudePoints(longitudeInput, longitudeSuggested, CityProperties.LONGITUDE.getKey());
        }

        if(inputData.containsKey(CityProperties.STATE.getKey()) &&
                dataSuggested.get(CityProperties.STATE.getKey()).equals(inputData.get(CityProperties.STATE.getKey()))){
            score += (individualProbability * 2);
        }
        else if(inputData.containsKey(CityProperties.COUNTRY.getKey()) &&
                dataSuggested.get(CityProperties.COUNTRY.getKey()).equals(inputData.get(CityProperties.COUNTRY.getKey()))){
            score += individualProbability;
        }

        int cityInputLength = inputData.get(CityProperties.NAME.getKey()).length();
        int citySuggestedLength = dataSuggested.get(CityProperties.NAME.getKey()).toString().length();
        score += (cityInputLength * individualProbability / citySuggestedLength);

        score /= percentageInt;

        return BigDecimal.valueOf(score).setScale(scoringDecimals, RoundingMode.HALF_UP).doubleValue();
    }

    private double getLatitudeLongitudePoints(double coordInput, double coordSuggested, String coordinates){
        double coordDifference = 0;
        double max_difference = coordinates.equals(CityProperties.LATITUDE.getKey()) ?
                Coordinates.MAX_LATITUDE_DIFFERENCE : Coordinates.MAX_LONGITUDE_DIFFERENCE;

        if((coordInput < 0 && coordSuggested > 0) ||  (coordInput > 0 && coordSuggested < 0))
            coordDifference = Math.abs(coordInput) + Math.abs(coordSuggested);
        else
            coordDifference = Math.abs(Math.abs(coordInput) - Math.abs(coordSuggested));

        double pointsToDecrease = coordDifference * individualProbability / max_difference;
        return individualProbability - pointsToDecrease;
    }
}
