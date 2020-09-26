package com.nr.stringville.models;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Formula {
    private List<Rule> ruleList = new ArrayList<>();
    public Formula() {
        // Load the config
        readConfig();
    }

    private void readConfig() {
    }

    public int getScore(List<Character> charList) {
        return charList.size();
    }

    class Rule {
        private Character letter;
        private String frequency;
        private int count;
        private int points;


        public Character getLetter() {
            return letter;
        }

        public void setLetter(Character letter) {
            this.letter = letter;
        }

        public String getFrequency() {
            return frequency;
        }

        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }
}

