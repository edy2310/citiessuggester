package com.edgar.citiessuggester.Setup;

import com.edgar.citiessuggester.DataStructures.Trie;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Component
public class CreateTrie {

    @Autowired
    Trie trie;

    @PostConstruct
    public void constructTrie(){
        HashMap<String, String> cities =  new HashMap<>();
        cities.put("Canada", "src/main/resources/data/canadacities.csv");
        cities.put("USA", "src/main/resources/data/uscities.csv");

        cities.forEach((country, file) -> insertWordsInTrie(file, country));
    }

    private void insertWordsInTrie(String file, String country) {
        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            List<String[]> row = reader.readAll();
            row.remove(0);
            row.forEach(line -> trie.insert(line[1], line[2], Float.valueOf(line[4]), Float.valueOf(line[5]), country));
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }
}