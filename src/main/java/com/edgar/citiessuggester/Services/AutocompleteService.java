package com.edgar.citiessuggester.Services;

import com.edgar.citiessuggester.DataStructures.CityProperties;
import com.edgar.citiessuggester.DataStructures.Trie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AutocompleteService {

    @Autowired Trie trie;
    @Autowired ScoringService scoringService;

    public Optional<ArrayList<HashMap<String, Object>>> getSuggestions(String word,
                                                                       Optional<Double> optionalLatitude,
                                                                       Optional<Double> optionalLongitude,
                                                                       Optional<String> optionalState,
                                                                       Optional<String> optionalCountry){
        ArrayList<HashMap<String, Object>> suggestions = trie.getSuggestions(word);
        if(suggestions == null)
            return Optional.empty();

        HashMap<String, String> inputData = getOptionalData(optionalLatitude, optionalLongitude,
                optionalState, optionalCountry);
        inputData.put(CityProperties.NAME.getKey(), word);

        return Optional.of(formatSuggestions(suggestions, inputData));
    }

    private ArrayList<HashMap<String, Object>> formatSuggestions(ArrayList<HashMap<String, Object>> suggestions,
                                                                 HashMap<String, String> inputData){
        ArrayList<HashMap<String, Object>> results = new ArrayList<>();

        suggestions.forEach(hashMap ->{
            ArrayList<String> countries = (ArrayList<String>) hashMap.get(CityProperties.COUNTRY.getKey());
            ArrayList<String> states = (ArrayList<String>) hashMap.get(CityProperties.STATE.getKey());
            ArrayList<String> latitudes = (ArrayList<String>) hashMap.get(CityProperties.LATITUDE.getKey());
            ArrayList<String> longitudes = (ArrayList<String>) hashMap.get(CityProperties.LONGITUDE.getKey());

            for (int i = 0; i < countries.size(); i++) {
                HashMap<String, Object> resultsHashMap = new HashMap<>();
                resultsHashMap.put(CityProperties.NAME.getKey(), hashMap.get(CityProperties.NAME.getKey()));
                resultsHashMap.put(CityProperties.LATITUDE.getKey(), latitudes.get(i));
                resultsHashMap.put(CityProperties.LONGITUDE.getKey(), longitudes.get(i));
                resultsHashMap.put(CityProperties.COUNTRY.getKey(), countries.get(i));
                resultsHashMap.put(CityProperties.STATE.getKey(), states.get(i));

                double score = scoringService.getScore(inputData, resultsHashMap);
                resultsHashMap.put("score", score);
                resultsHashMap.remove(CityProperties.COUNTRY.getKey());
                resultsHashMap.remove(CityProperties.STATE.getKey());

                StringBuilder sbName = new StringBuilder(hashMap.get(CityProperties.NAME.getKey()) + ", ");
                sbName.append(states.get(i) + ", ");
                sbName.append(countries.get(i).toUpperCase());
                resultsHashMap.replace(CityProperties.NAME.getKey(), sbName.toString());

                results.add(resultsHashMap);
            }
        });

        sortResults(results);
        return results;
    }

    private HashMap<String, String> getOptionalData(Optional<Double> optionalLatitude, Optional<Double> optionalLongitude,
                                 Optional<String> optionalState, Optional<String> optionalCountry){

        HashMap<String, String> result = new HashMap<>();
        optionalLatitude.ifPresent(latitude -> result.put(CityProperties.LATITUDE.getKey(), String.valueOf(latitude)));
        optionalLongitude.ifPresent(longitude -> result.put(CityProperties.LONGITUDE.getKey(), String.valueOf(longitude)));
        optionalState.ifPresent(state -> result.put(CityProperties.STATE.getKey(), state));
        optionalCountry.ifPresent(country -> result.put(CityProperties.COUNTRY.getKey(), country.toLowerCase()));
        return result;
    }

    private void sortResults(ArrayList<HashMap<String, Object>> results){
        Collections.sort(results, new Comparator<HashMap<String, Object>>() {
            @Override
            public int compare(HashMap<String, Object> result1, HashMap<String, Object> result2) {
                Double score1 = (double)result1.get("score");
                Double score2 = (double)result2.get("score");
                return score1.compareTo(score2);
            }
        });
        Collections.reverse(results);
    }
}