package com.mercurys.encryption;

public class Key {
    private static final int FRAGMENT_LENGTH = 8;
    private final int numberOfFragments;
    private char[] keyValue;

    public Key(int keyLength) {
        keyValue = new char[keyLength];
        numberOfFragments = keyLength / FRAGMENT_LENGTH;
    }

    public void setKey(char[] key) {
        if (this.isAlreadyAssigned()) {
            this.keyValue = key;
        }
    }

    private boolean isAlreadyAssigned() {
        boolean isAssigned = false;
        for (char c : keyValue) {
            if (c < 32 || c > 127) {
                isAssigned = true;
                break;
            }
        }
        return isAssigned;
    }

    public String getKeyFragment(int fragmentNumber) {
        if (fragmentNumber < numberOfFragments - 1) {
            return this.getKeyAsString().substring(
                    fragmentNumber * FRAGMENT_LENGTH, (fragmentNumber + 1) * FRAGMENT_LENGTH);
        } else if (fragmentNumber == numberOfFragments - 1) {
            return this.getKeyAsString().substring(fragmentNumber * FRAGMENT_LENGTH);
        } else
            return null;
    }

    public String getKeyAsString() {
        return String.valueOf(keyValue);
    }

    public void setRandom() {
        for (int i = 0; i < keyValue.length; i++) {
            char c = (char) (92 * Math.random() + 33);
            while (this.getKeyAsString().indexOf(c) != -1 || c == '|') {
                c = (char) (92 * Math.random() + 33);
            }
            this.add(c, i);
        }
    }

    public void add(char c, int index) {
        keyValue[index] = c;
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

    public char[] getKeyAsCharArray() {
        return keyValue;
    }

    public int getNumberOfFragments() {
        return numberOfFragments;
    }

    public void erase() {
        keyValue = null;
    }
}
