package com.nr.stringville;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SubmissionController {
    @Autowired
    private SubmissionRepository submissionRepository;

    @RequestMapping("/")
    public String index() {
        return "Welcome to Stringville!";
    }

    @PostMapping("/submission")
    public ResponseEntity submission(@RequestBody String body) {
        if (body.length() > 10000) {
            return ResponseEntity.status(415).body("rejected");
        }
        String[] parts = body.split(",");
        if (parts.length == 1) {
            return ResponseEntity.status(415).body("rejected");
        } else {
            Submission submission = new Submission(parts[1],parts[0]);
            submission.setId(System.nanoTime());
            submissionRepository.save(submission);
        }
        return ResponseEntity.status(200).body("accepted");
    }

    @GetMapping("/submission")
    public List<Submission> getSubmissions() {
        return submissionRepository.findAll();
    }


    @GetMapping("/results")
    public ResponseEntity results() {
        StringBuilder response = new StringBuilder();
        List<Submission> submissions = submissionRepository.findAll();
        for (Submission submission : submissions) {
            response.append(submission.getName() + "," + calculateScore(submission.getString()) ).append("\n");
        }
        return ResponseEntity.status(200).body(response.toString());
    }

    @GetMapping("/health")
    public ResponseEntity health() {
        return ResponseEntity.status(200).body("");
    }

    @GetMapping("/reset")
    public ResponseEntity reset() {
        submissionRepository.deleteAll();
        return ResponseEntity.status(200).body("");
    }


    private int calculateScore(String string) {
        return string.length();
    }

}
