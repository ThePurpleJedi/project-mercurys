package com.mercurys.encryption;

import java.util.*;

public class Decryption {

    private String chars;
    private Key key;
    private int actualMessageLength;

    public Decryption() {
        this.chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    }

    public String decrypt(final String message) {
        if (message == null) {
            return null;
        }
        String[] messageAsWords = message.split(" +");
        messageAsWords = preProcessTheMessage(messageAsWords);
        messageAsWords = runDecryptionAlgorithm(messageAsWords);
        String finalDecryptedMessage = this.getFinalDecryptedMessage(messageAsWords);
        key.erase();
        return finalDecryptedMessage;
    }

    private String[] runDecryptionAlgorithm(String[] messageAsWords) {
        this.reTranslateMessageUsingKey(messageAsWords);
        messageAsWords = this.perfectSquareReSwap(messageAsWords);
        messageAsWords = this.unReverseMessage(messageAsWords);
        this.antiRotateMessageBy7(messageAsWords);
        return messageAsWords;
    }

    private void reTranslateMessageUsingKey(final String[] words) {
        for (int i = 0; i < this.actualMessageLength; i++) {
            words[i] = reTranslateWord(words[i].toCharArray());
        }
    }

    private String reTranslateWord(char[] letters) {
        String keyString = this.key.getKeyAsString();
        for (int j = 0; j < letters.length; j++) {
            if (letters[j] != '|') {
                letters[j] = this.chars.charAt(keyString.indexOf(letters[j]));
            }
        }
        return String.valueOf(letters);
    }

    private String[] perfectSquareReSwap(String[] words) {
        int k = 1;
        final List<String> ls = new ArrayList<>(Arrays.asList(words));
        for (int i = 1; i < this.actualMessageLength; i += 2 * (k++) + 1) {
            Collections.swap(ls, i, i - 1);
        }
        words = ls.toArray(words);
        return words;
    }

    private String[] unReverseMessage(String[] words) {
        final StringBuilder msgBuilder = new StringBuilder();
        for (int i = this.actualMessageLength - 1; i >= 0; i--) {
            words[i] = new StringBuilder(words[i]).reverse().toString();
            msgBuilder.append(words[i]).append(" ");
        }
        words = msgBuilder.toString().split(" ");
        return words;
    }

    private void antiRotateMessageBy7(final String[] words) {
        for (int i = 0; i < words.length; i++) {
            words[i] = antiRotateWord(words[i]);
        }
    }

    private String antiRotateWord(String word) {
        StringBuilder encodedWord = new StringBuilder();
        for (int j = 0; j < word.length(); j++) {
            if (this.chars.indexOf(word.charAt(j)) != -1) {
                encodedWord.append(this.antiRotateLetterBy7(word.charAt(j)));
            }
        }
        return encodedWord.toString();
    }

    private char antiRotateLetterBy7(char c) {
        if (this.chars.indexOf(c) < 26) {
            c += (c > 102)? -7 : 19;
        } else if (this.chars.indexOf(c) < 52) {
            c += (c > 71)? -7 : 19;
        }
        return c;
    }

    private String[] preProcessTheMessage(String[] words) {
        this.extractSpecialCharacters(words);
        this.key = new Key(88);
        key.setKey(key.getKeyFromMessage(words).toCharArray());
        this.nullifyEmbeddedKeyFragments(words);
        words = this.removeAllNullElements(words);
        return words;
    }

    private String[] removeAllNullElements(final String[] words) {
        final StringBuilder notNullWords = new StringBuilder();
        for (final String word : words) {
            if (word != null) {
                notNullWords.append(word).append(" ");
            }
        }
        return notNullWords.toString().split(" ");
    }

    private void extractSpecialCharacters(final String[] words) {
        this.chars += words[0];
        words[0] = null;
    }

    public void nullifyEmbeddedKeyFragments(String[] words) {
        int l = words.length - 1;
        for (int i = 1; i < words.length; i++) {
            if (words[i].charAt(0) == '~' && words[i].charAt(words[i].length() - 1) == '~') {
                words[i] = null;
                l--;
            }
        }
        this.actualMessageLength = l;
    }

    private String getFinalDecryptedMessage(final String[] words) {
        final StringBuilder wordsMessage = new StringBuilder();
        for (final String word : words) {
            wordsMessage.append(word).append(" ");
        }
        return wordsMessage.toString();
    }
}