package com.mercurys.encryption;

import java.util.*;

public class Decryption {

    private String chars;
    private String key;
    private int actualMessageLength = 0;

    public Decryption() {
        this.chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        this.key = "";
    }

    /**
     * Runs the entire decryption algorithm in order
     */
    public String decrypt(final String msg) {

        if (msg == null) {
            return null;
        }

        String[] words = msg.split(" +");

        key = this.extractKeyAndNullifyEmbeddedKeyFragments(words);
        words = Decryption.removeAllNullElements(words);
        this.reTranslateMessageUsingKey(words);
        words = this.perfectSquareReSwap(words);
        words = this.unReverseMessage(words);
        this.antiRotateMessageBy7(words);
        return Decryption.getFinalDecryptedMessage(words);
    }

    /**
     * Displays the final words message
     */
    private static String getFinalDecryptedMessage(final String[] words) {
        final StringBuilder wordsMessage = new StringBuilder();

        for (final String word : words) {
            wordsMessage.append(word).append(" ");
        }

        return wordsMessage.toString();
    }

    private static String[] removeAllNullElements(final String[] words) {
        final StringBuilder notNullWords = new StringBuilder();
        for (final String word : words) {
            if (word != null) {
                notNullWords.append(word).append(" ");
            }
        }
        return notNullWords.toString().split(" ");
    }

    /**
     * Extracts the key from the given message, updates the KEY variable.
     * Removes key from the message and updates the words[] array
     */
    private String extractKeyAndNullifyEmbeddedKeyFragments(final String[] words) {
        int l = words.length - 1;
        final StringBuilder keyBuilder = new StringBuilder();
        this.chars += words[0];
        words[0] = null;
        for (int i = 1; i < words.length; i++) {
            if (words[i].charAt(0) == '~' && words[i].charAt(words[i].length() - 1) == '~') {
                for (final char c : words[i].toCharArray()) {
                    keyBuilder.append((c != '~')? c : "");
                }
                words[i] = null;
                l--;
            }
        }
        this.actualMessageLength = l;
        return keyBuilder.toString();
    }

    /**
     * Uses the KEY to re-translate the message
     */
    private void reTranslateMessageUsingKey(final String[] words) {
        for (int i = 0; i < this.actualMessageLength; i++) {
            final char[] letters = words[i].toCharArray();
            for (int j = 0; j < letters.length; j++) {
                if (letters[j] == '|') {
                    continue;
                }
                letters[j] = this.chars.charAt(this.key.indexOf(letters[j]));
            }
            words[i] = String.valueOf(letters);
        }
    }

    /**
     * Re-swaps the words at the perfect square positions with the ones just before them
     */
    private String[] perfectSquareReSwap(String[] words) {
        int k = 1;
        final List<String> ls = new ArrayList<>(Arrays.asList(words));
        for (int i = 1; i < this.actualMessageLength; i += 2 * (k++) + 1) {
            Collections.swap(ls, i, i - 1);
        }
        words = ls.toArray(words);
        return words;
    }

    /**
     * Un-reverses the message
     */
    private String[] unReverseMessage(String[] words) {
        final StringBuilder msgBuilder = new StringBuilder();
        for (int i = this.actualMessageLength - 1; i >= 0; i--) {
            words[i] = new StringBuilder(words[i]).reverse().toString();
            msgBuilder.append(words[i]).append(" ");
        }
        words = msgBuilder.toString().split(" ");
        return words;
    }

    /**
     * Rotates the letters back by 7
     */
    private void antiRotateMessageBy7(final String[] words) {
        for (int i = 0; i < words.length; i++) {
            final StringBuilder encodedWord = new StringBuilder();

            for (int j = 0; j < words[i].length(); j++) {
                final char c = words[i].charAt(j);
                if (this.chars.indexOf(c) != -1) {
                    encodedWord.append(this.antiRotateLetterBy7(c));
                } else if (c == '|') {
                    encodedWord.append(c);
                }
            }
            words[i] = encodedWord.toString();
        }
    }

    private char antiRotateLetterBy7(char c) {
        if (this.chars.indexOf(c) < 26) {

            if (c > 102) {
                c -= 7;
            } else {
                c += 19;
            }

        } else if (this.chars.indexOf(c) < 52) {

            if (c > 71) {
                c -= 7;
            } else {
                c += 19;
            }

        }

        return c;
    }

    public String getKeyFromMessage(String[] words) {
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 1; i < words.length; i++) {
            if (words[i].charAt(0) == '~' && words[i].charAt(words[i].length() - 1) == '~') {
                for (final char c : words[i].toCharArray()) {
                    keyBuilder.append((c != '~')? c : "");
                }
            }
        }
        return keyBuilder.toString();
    }
}