package com.nr.stringville.controllers;

import com.google.common.base.CharMatcher;
import com.nr.stringville.models.Formula;
import com.nr.stringville.models.Submission;
import com.nr.stringville.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class SubmissionController {
    private static final int TOP_LIST_SIZE = 10;
    private Formula formula = new Formula();

    @Autowired
    private SubmissionRepository submissionRepository;

    @RequestMapping("/")
    public String index() {
        return "Welcome to Stringville!";
    }

    @PostMapping("/submission")
    public ResponseEntity submission(@RequestBody String body) {
        // Verify the length (10,000 characters) and format (ASCII) of the String
        if (body.length() > 10000) {
            return ResponseEntity.status(400).body("Input too long");
        }
        if (!CharMatcher.ascii().matchesAllOf(body)) {
            return ResponseEntity.status(400).body("Input contains invalid characters");
        }

        String[] parts = body.split(",");

        // Validate string as having the comma, that the string.length>=1 and that name.length>=1
        if (parts.length == 1) {
            return ResponseEntity.status(400).body("Missing information");
        }

        if (parts[0].trim().length() < 1) {
            return ResponseEntity.status(400).body("Invalid submission");
        }

        if (parts[1].trim().length() < 1) {
            return ResponseEntity.status(400).body("Invalid name");
        }
        // Valid input. Proceed to save in the DB
        Submission submission = new Submission(parts[1],parts[0]);
        submissionRepository.save(submission);
        return ResponseEntity.status(200).body("submission accepted");
    }

    @GetMapping("/results")
    public ResponseEntity results() {
        StringBuilder response = new StringBuilder();
        List<Submission> submissions = submissionRepository.findAll();
        if (submissions != null && submissions.size() > 0) {
            // Limit the result size to the least of TOP_LIST_SIZE or records count
            int maxRecords = Math.min(submissions.size(),TOP_LIST_SIZE);
            for (int i = 0; i < maxRecords; i++) {
                response.append(submissions.get(i).getName() + ","
                        + calculateScore(submissions.get(i).getString()));
                // Add a line break if not at the end of the list
                if (i < maxRecords - 1) {
                    response.append("\n");
                }
            }
        }
        return ResponseEntity.status(200).body(response.toString());
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        // Need
        return ResponseEntity.status(200).body("");
    }

    @GetMapping("/reset")
    public ResponseEntity reset() {
        submissionRepository.deleteAll();
        return ResponseEntity.status(200).body("");
    }

    private int calculateScore(String string) {
        List<Character> charList = new ArrayList<>();
        for (Character character : string.toCharArray()) {
            charList.add(character);
        }
        // Sort to group characters together
        Collections.sort(charList);
        return formula.getScore(charList);
    }
}
