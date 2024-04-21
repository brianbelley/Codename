package org.example.dataaccess;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FileManagement {

    public static List<Word> readWordsFromFile(String filePath) {
        filePath = "C:\\Users\\Blue's Clues\\IdeaProjects\\Codename\\src\\main\\java\\org\\example\\dataaccess\\words.json";
        List<Word> words = new ArrayList<>();

        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader(filePath)) {
            Object obj = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray wordArray = (JSONArray) jsonObject.get("words");

            for (Object o : wordArray) {
                JSONObject wordObject = (JSONObject) o;
                String word = (String) wordObject.get("word");
                String difficulty = (String) wordObject.get("difficulty");
                Word newWord = new Word(word, difficulty);
                words.add(newWord);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return words;
    }


    public static List<String> getWordsByDifficulty(String filePath, String difficulty) {
        List<Word> words = readWordsFromFile(filePath);
        List<String> matchingWords = new ArrayList<>();

        for (Word word : words) {
            if (word.getDifficulty().equalsIgnoreCase(difficulty)) {
                matchingWords.add(word.getWord());
            }
        }

        if (matchingWords.isEmpty()) {
            throw new IllegalArgumentException("No words found for the specified difficulty: " + difficulty);
        }

        return matchingWords;
    }


    public static class Word {
        private String word;
        private String difficulty;

        public Word(String word, String difficulty) {
            this.word = word;
            this.difficulty = difficulty;
        }

        public String getWord() {
            return word;
        }

        public String getDifficulty() {
            return difficulty;
        }
    }
}
