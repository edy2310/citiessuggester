package com.edgar.citiessuggester.DataStructures;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

public class TrieTest {

    Trie trie = new Trie();

    @Test
    public void testInsertAndGetSuggestionsFound(){
        Set<String> words = new HashSet<>(Arrays.asList("extremelylongwordtomatch", "thisisanotherlongword"));
        ArrayList<Double> latitude = new ArrayList<>(Arrays.asList(23.10, 12.10));
        ArrayList<Double> longitude = new ArrayList<>(Arrays.asList(14.20, 42.15));
        ArrayList<String> country = new ArrayList<>(Arrays.asList("ca", "usa"));
        ArrayList<String> state = new ArrayList<>(Arrays.asList("TEST", "TEST1"));

        words.forEach(word -> trie.insert(word, state.get(0), latitude.get(0), longitude.get(0), country.get(0)));

        ArrayList<HashMap<String, Object>> result = trie.getSuggestions("extremelylongw");
        result.forEach(hashMap -> {
            assertTrue(words.contains(hashMap.get("name")));
            assertTrue(latitude.containsAll((ArrayList) hashMap.get("latitude")));
            assertTrue(longitude.containsAll((ArrayList) hashMap.get("longitude")));
            assertTrue(country.containsAll((ArrayList) hashMap.get("country")));
            assertTrue(state.containsAll((ArrayList) hashMap.get("state")));
        });
    }

    @Test
    public void testGetSuggestionsNotFound(){
        ArrayList<HashMap<String, Object>> result = trie.getSuggestions("xyz");
        assertNull(result);
    }
}