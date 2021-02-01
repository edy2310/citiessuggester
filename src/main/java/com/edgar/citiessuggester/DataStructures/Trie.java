package com.edgar.citiessuggester.DataStructures;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Scope("singleton")
public final class Trie {
    private TrieNode root;

    public Trie(){
        this.root = new TrieNode();
    }

    public void insert(String city, String state, double latitude, double longitude, String country){
        city = city.toLowerCase();
        HashMap<Character, TrieNode> links = root.getLinks();
        for(int i = 0; i < city.length(); i++){
            char character = city.charAt(i);
            TrieNode currentNode;
            if(links.containsKey(character))
                currentNode = links.get(character);
            else{
                currentNode = new TrieNode(character);
                links.put(character, currentNode);
            }
            links = currentNode.getLinks();
            if(i == city.length()-1){
                currentNode.setWord(true);
                currentNode.getStates().add(state);
                currentNode.getLatitudes().add(latitude);
                currentNode.getLongitudes().add(longitude);
                currentNode.getCountries().add(country.toLowerCase());
            }
        }
    }

    public ArrayList<HashMap<String, Object>> getSuggestions(String word){
        ArrayList<HashMap<String, Object>> result = new ArrayList<>();

        TrieNode baseNode = searchNode(word.toLowerCase());
        if(baseNode == null)
            return null;
        StringBuilder sb = new StringBuilder(word.substring(0, word.toLowerCase().length()-1));

        return dfsSuggestions(baseNode, result, sb);
    }

    private ArrayList<HashMap<String, Object>> dfsSuggestions(TrieNode trieNode, ArrayList<HashMap<String, Object>> result, StringBuilder sb){
        sb.append(trieNode.getValue());
        if(trieNode.isWord()){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("name", sb.toString());
            hashMap.put("latitude", trieNode.getLatitudes());
            hashMap.put("longitude", trieNode.getLongitudes());
            hashMap.put("state", trieNode.getStates());
            hashMap.put("country", trieNode.getCountries());
            result.add(hashMap);
        }
        HashMap<Character, TrieNode> links = trieNode.getLinks();
        for(TrieNode n : links.values())
            result = dfsSuggestions(n, result, sb);
        if(sb.length() > 0)
            sb.deleteCharAt(sb.length()-1);
        return result;
    }

    private TrieNode searchNode(String word){
        HashMap<Character, TrieNode> links = root.getLinks();
        TrieNode currentNode = null;
        char[] charArray = word.toCharArray();
        for(char character : charArray){
            if(links.containsKey(character)){
                currentNode = links.get(character);
                links = currentNode.getLinks();
            }
            else
                return null;
        }
        return currentNode;
    }
}
