package com.nr.stringville.controllers;

import com.google.common.base.CharMatcher;
import com.nr.stringville.models.Formula;
import com.nr.stringville.models.Submission;
import com.nr.stringville.repositories.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SubmissionController {
    public static final int TOP_LIST_SIZE = 10;
    private final AtomicInteger validSubsCount = new AtomicInteger(0);
    private final AtomicInteger invalidSubsCount = new AtomicInteger(0);
    private final LocalDateTime startTime = LocalDateTime.now();
    private final Formula formula = new Formula();
    private volatile Submission lastSubmission = null;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss  dd-MM-yyyy ");

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
            getInvalidSubsCount().getAndIncrement();
            return ResponseEntity.status(400).body("Input too long");
        }
        if (!CharMatcher.ascii().matchesAllOf(body)) {
            getInvalidSubsCount().getAndIncrement();
            return ResponseEntity.status(400).body("Input contains invalid characters");
        }

        String[] parts = body.split(",");

        // Validate string as having the comma, that the string.length>=1 and that name.length>=1
        if (parts.length == 1) {
            getInvalidSubsCount().getAndIncrement();
            return ResponseEntity.status(400).body("Missing information");
        }

        if (parts[0].trim().length() < 1) {
            getInvalidSubsCount().getAndIncrement();
            return ResponseEntity.status(400).body("Invalid submission");
        }

        if (parts[1].trim().length() < 1) {
            getInvalidSubsCount().getAndIncrement();
            return ResponseEntity.status(400).body("Invalid name");
        }
        // Valid input. Proceed to save in the DB
        getValidSubsCount().getAndIncrement();
        Submission submission = new Submission(parts[1],parts[0],calculateScore(parts[0]));
        try {
            submissionRepository.save(submission);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseEntity.status(400).body("Unable to persist submission. Verify there is no submission already recorded for this name");
        }

        // Record as last submission
        setLastSubmission(submission);
        return ResponseEntity.status(200).body("submission accepted");
    }

    @GetMapping("/results")
    public ResponseEntity results() {
        StringBuilder response = new StringBuilder("submission accepted");
        List<Submission> submissions = submissionRepository.findAll();

        // Sort results to be able to display top TOP_LIST_SIZE only
        submissions.sort(Collections.reverseOrder());
        if (submissions.size() > 0) {
            response = new StringBuilder();
            // Limit the result size to the least of TOP_LIST_SIZE or records count
            int maxRecords = Math.min(submissions.size(),TOP_LIST_SIZE);
            for (int i = 0; i < maxRecords; i++) {
                response.append(submissions.get(i).getName()).append(",").append(submissions.get(i).getScore());
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
        StringBuilder response = new StringBuilder();
        response.append("System uptime : ").append(calculateTimeSince(startTime)).append("\n");
        response.append("Valid submissions : ").append(getValidSubsCount()).append("\n");
        response.append("Invalid submissions : ").append(getInvalidSubsCount()).append("\n");
        if (getValidSubsCount().intValue() > 0  && getLastSubmission() != null) {
            response.append("Last submission : ").append(getLastSubmission().getName()).append(" at ")
                    .append(getLastSubmission().getTime().format(formatter)).append("(")
                    .append(calculateTimeSince(getLastSubmission().getTime()))
                    .append(" ago)").append("\n");
        } else {
            response.append("Last submission : No submissions yet." + "\n");
        }
        return ResponseEntity.status(200).body(response.toString());
    }

    @GetMapping("/reset")
    public ResponseEntity reset() {
        getValidSubsCount().set(0);
        getInvalidSubsCount().set(0);
        setLastSubmission(null);
        submissionRepository.deleteAll();
        return ResponseEntity.status(200).body("system reset");
    }

    public AtomicInteger getValidSubsCount() {
        return validSubsCount;
    }
    public AtomicInteger getInvalidSubsCount() {
        return invalidSubsCount;
    }

    public void setValidSubsCount(int value) {
        this.validSubsCount.set(value);
    }

    public void setInvalidSubsCount(int value) {
        this.invalidSubsCount.set(value);
    }

    public void setSubmissionRepository(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    public int calculateScore(String string) {
        Map<Character,Integer> charCountMap = new HashMap<>();
        // Update count of occurrences
        for (Character character : string.toCharArray()) {
            charCountMap.merge(character, 1, Integer::sum);
        }
        return formula.getScore(charCountMap);
    }

    public String calculateTimeSince(LocalDateTime startTime) {
        StringBuilder result = new StringBuilder();
        long secondsElapsed = startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS);
        long diffSeconds = secondsElapsed / 1000 % 60;
        long diffMinutes = secondsElapsed / (60 * 1000) % 60;
        long diffHours = secondsElapsed / (60 * 60 * 1000);
        long diffDays = secondsElapsed / (24 * 60 * 60 * 1000);
        if (diffDays != 0) {
            result.append(diffDays).append(" days, ");
        }

        if (diffHours != 0) {
            result.append(diffHours).append(" hours, ");
        }

        if (diffMinutes > 1) {
            result.append(diffMinutes).append(" minutes, ");
        } else if (diffMinutes > 0) {
            result.append(diffMinutes).append(" minute, ");
        }

        if (diffSeconds != 0) {
            result.append(diffSeconds).append(" seconds");
        }
        if (result.toString().isEmpty()) {
            result.append("Just now");
        }
        return result.toString();
    }

    public Submission getLastSubmission() {
        return lastSubmission;
    }

    public void setLastSubmission(Submission lastSubmission) {
        this.lastSubmission = lastSubmission;
    }
}
