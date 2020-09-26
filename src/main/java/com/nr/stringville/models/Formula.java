package com.nr.stringville.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Formula {
    private final List<Rule> ruleList = new ArrayList<>();
    private static final String EXACT = "exactly";
    private static final String EVERY = "every";
    public Formula() {
        // This can be offloaded to json file, or a db for runtime customization not requiring compiling
        this.ruleList.add(new Rule('a',EXACT,3,1));
        this.ruleList.add(new Rule('e',EXACT,2,5));
        this.ruleList.add(new Rule('i',EVERY,2,2));
        this.ruleList.add(new Rule('g',EXACT,1,3));
        this.ruleList.add(new Rule('u',EVERY,1,1));
        this.ruleList.add(new Rule('z',EVERY,1,-10));
    }

    public int getScore(Map<Character,Integer> charCountMap) {
        int result = 0;
        for (Rule rule : ruleList) {
            Integer count = charCountMap.get(rule.character);
            if (count == null || count == 0) {
                // Don't do anything, just safety against null. Keeping this here for clarity
            } else if (EXACT.equals(rule.getFrequency()) &&   count.equals(rule.getCount())) {
                result = result + rule.getPoints();
            } else if (EVERY.equals(rule.getFrequency())) {
                result = result +  Math.floorDiv(count,rule.getCount()) * rule.getPoints();
            }
        }
        return result;
    }

    static class Rule {
        private final char character;
        private final String frequency;
        private final int count;
        private final int points;

        public Rule(char character, String frequency, int count, int points) {
            this.character = character;
            this.frequency = frequency;
            this.count = count;
            this.points = points;
        }

        public String getFrequency() {
            return frequency;
        }

        public int getCount() {
            return count;
        }

        public int getPoints() {
            return points;
        }
    }
}

