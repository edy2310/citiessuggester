package com.edgar.citiessuggester.Controller;

import com.edgar.citiessuggester.Services.AutocompleteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@RestController
@Api(value = "Autocomplete controller", description = "Get city suggestions", produces = "JSON")
public class AutocompleteController {

    @Autowired
    AutocompleteService autocompleteService;

    @GetMapping("suggestions")
    @ApiOperation(value = "Find cities with score value", notes = "Returns all possible cities, descending order, no matter if its value is 0" )
    public ResponseEntity<HashMap<String, ArrayList>> getSuggestions(@RequestParam String q,
                                                                     @RequestParam Optional<Double> latitude,
                                                                     @RequestParam Optional<Double> longitude,
                                                                     @RequestParam Optional<String> state,
                                                                     @RequestParam Optional<String> country){
        HashMap<String, ArrayList> resultHashMap = new HashMap<>();
        resultHashMap.put("suggestions", new ArrayList<>());

        Optional<ArrayList<HashMap<String, Object>>> optionalResults = autocompleteService.getSuggestions(q,
                latitude, longitude, state, country);

        if(!optionalResults.isEmpty())
            resultHashMap.replace("suggestions", optionalResults.get());

        return ResponseEntity.ok(resultHashMap);
    }
}
