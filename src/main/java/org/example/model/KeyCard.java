package org.example.model;

import org.example.dataaccess.FileManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KeyCard {
    private int cardNumber;
    private String cardWord;
    private CardType cardType;

    public KeyCard(int cardNumber, String cardWord, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardWord = cardWord;
        this.cardType = cardType;
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardWord() {
        return cardWord;
    }

    public void setCardWord(String cardWord) {
        this.cardWord = cardWord;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public static List<KeyCard> generateKeyCards(String difficulty) {
        // Read words from file based on the specified difficulty
        String filePath = "C:\\Users\\Blue's Clues\\IdeaProjects\\Codename\\src\\main\\java\\org\\example\\dataaccess\\words.json";
        List<String> words = FileManagement.getWordsByDifficulty(filePath, difficulty);

        // Generate keycards based on the words
        List<KeyCard> keyCards = new ArrayList<>();
        Random random = new Random();
        for (String word : words) {
            int cardNumber = random.nextInt(1000); // Generate a random card number
            CardType cardType = CardType.NEUTRAL; // Assuming all cards are normal for now
            keyCards.add(new KeyCard(cardNumber, word, cardType));
        }

        return keyCards;
    }



    @Override
    public String toString() {
        return "KeyCard{" +
                "cardNumber=" + cardNumber +
                ", cardWord='" + cardWord + '\'' +
                ", cardType=" + cardType +
                '}';
    }
}
