package com.edgar.citiessuggester.DataStructures;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class TrieNode {
    private HashMap<Character, TrieNode> links = new HashMap<>();
    private char value;
    private boolean isWord;
    private List<String> states = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    private List<Double> latitudes = new ArrayList<>();
    private List<Double> longitudes = new ArrayList<>();

    public TrieNode(){}
    public TrieNode(char character){
        this.value = character;
    }
}