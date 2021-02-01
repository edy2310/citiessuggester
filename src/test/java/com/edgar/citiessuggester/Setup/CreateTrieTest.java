package com.edgar.citiessuggester.Setup;

import com.edgar.citiessuggester.DataStructures.CityProperties;
import com.edgar.citiessuggester.DataStructures.Trie;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
public class CreateTrieTest {

    @Autowired
    Trie trie;

    @Test
    public void testConstructTrie(){
        String torontoCity = "toronto";
        String miamisburgCity = "miamisburg";

        ArrayList<HashMap<String, Object>> result = trie.getSuggestions("toron");
        result.forEach(hashMap -> assertTrue(torontoCity.equals(hashMap.get(CityProperties.NAME.getKey()))));

        result = trie.getSuggestions("miamisbu");
        result.forEach(hashMap -> assertTrue(miamisburgCity.equals(hashMap.get(CityProperties.NAME.getKey()))));
    }
}
