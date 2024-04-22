package org.example.model;

import org.example.dataaccess.FileManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KeyCard {
    private int cardNumber;
    private String cardWord;
    private CardType cardType;
    private boolean revealed;

    public KeyCard(int cardNumber, String cardWord, CardType cardType) {
        this.cardNumber = cardNumber;
        this.cardWord = cardWord;
        this.cardType = cardType;
        this.revealed = false;
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
    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public static List<KeyCard> generateKeyCards(String difficulty) {
        // Read words from file based on the specified difficulty
        String filePath = "C:\\Users\\Blue's Clues\\IdeaProjects\\Codename\\src\\main\\java\\org\\example\\dataaccess\\words.json";
        List<String> words = FileManagement.getWordsByDifficulty(filePath, difficulty);

        // Ensure the number of words does not exceed 25
        int numWords = Math.min(25, words.size());
        words = words.subList(0, numWords);

        // Generate keycards based on the words
        List<KeyCard> keyCards = new ArrayList<>();
        Random random = new Random();

        // Initialize counts for red, blue, and neutral cards
        int redCount = 0;
        int blueCount = 0;
        int neutralCount = 0;

        // Add 8 red, 8 blue, 1 assassin, and the rest neutral cards
        for (String word : words) {
            int cardNumber = random.nextInt(1000); // Generate a random card number
            CardType cardType;

            // Determine the card type
            if (redCount < 8) {
                cardType = CardType.RED;
                redCount++;
            } else if (blueCount < 8) {
                cardType = CardType.BLUE;
                blueCount++;
            } else if (neutralCount < numWords - 17) { // 17 cards are already accounted for
                cardType = CardType.NEUTRAL;
                neutralCount++;
            } else {
                cardType = CardType.ASSASSIN;
            }

            keyCards.add(new KeyCard(cardNumber, word, cardType));
        }

        // Shuffle the keyCards list
        Collections.shuffle(keyCards);

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
