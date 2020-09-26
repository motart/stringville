package com.nr.stringville;

import com.nr.stringville.controllers.SubmissionController;
import com.nr.stringville.models.Submission;
import com.nr.stringville.repositories.SubmissionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SubmissionControllerUnitTest {
    private  SubmissionController fixture;
    SubmissionRepository submissionRepositoryMock;
    
    @Before
    public void setup() {
        fixture = spy(SubmissionController.class);
        fixture = spy(SubmissionController.class);
        submissionRepositoryMock = mock(SubmissionRepository.class);
        MockitoAnnotations.initMocks(this);
        fixture.setSubmissionRepository(submissionRepositoryMock);
    }

    @Test
    public void submissionTest() {
        // Too long string test case
        String testString = generateString();
        ResponseEntity result = fixture.submission(testString);
        assertEquals("400 BAD_REQUEST",result.getStatusCode().toString());
        assertEquals("Input too long",result.getBody());
        assertEquals(1,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // Too long string test case
        testString = "£asdß";
        result = fixture.submission(testString);
        assertEquals("400 BAD_REQUEST",result.getStatusCode().toString());
        assertEquals("Input contains invalid characters",result.getBody());
        assertEquals(2,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // Too long string test case
        testString = "testing";
        result = fixture.submission(testString);
        assertEquals("400 BAD_REQUEST",result.getStatusCode().toString());
        assertEquals("Missing information",result.getBody());
        assertEquals(3,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // No string in submission, comma present
        testString = ",testing";
        result = fixture.submission(testString);
        assertEquals("400 BAD_REQUEST",result.getStatusCode().toString());
        assertEquals("Invalid submission",result.getBody());
        assertEquals(4,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // No name in the submission,only space
        testString = "testing, ";
        result = fixture.submission(testString);
        assertEquals("400 BAD_REQUEST",result.getStatusCode().toString());
        assertEquals("Invalid name",result.getBody());
        assertEquals(5,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // Input valid, should be able to persist data
        when(submissionRepositoryMock.save(any())).thenReturn(null);
        testString = "testing,my name";
        result = fixture.submission(testString);
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("submission accepted",result.getBody());
        assertEquals(5,fixture.getInvalidSubsCount().intValue());
        assertEquals(1,fixture.getValidSubsCount().intValue());
    }

    @Test
    public void resultsTest() {
        // Empty submissions list
        when(submissionRepositoryMock.findAll()).thenReturn(new ArrayList<>());
        ResponseEntity result = fixture.results();
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("submission accepted",result.getBody());
        assertEquals(0,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // List <= TOP_LIST_SIZE
        List<Submission> submissions = getSubmissions(4);
        when(submissionRepositoryMock.findAll()).thenReturn(submissions);
        result = fixture.results();
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("name,3\nname,3\nname,3\nname,3",result.getBody());
        assertEquals(0,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // List > TOP_LIST_SIZE
        submissions = getSubmissions(12);
        when(submissionRepositoryMock.findAll()).thenReturn(submissions);
        result = fixture.results();
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("name,3\nname,3\nname,3\nname,3\nname,3\nname,3\nname,3\nname,3\nname,3\nname,3",result.getBody());
        assertEquals(0,fixture.getInvalidSubsCount().intValue());
        assertEquals(0,fixture.getValidSubsCount().intValue());

        // testOrder of results
        Submission sub1 = new Submission("Ivaana Bello","aaaiibbbru",fixture.calculateScore("aaaiibbbru"));
        Submission sub2 = new Submission("Camille Diaz","eiiegiebeici",fixture.calculateScore("eiiegiebeici"));
        Submission sub3 = new Submission("Bob Bloch","eiiegbczu",fixture.calculateScore("eiiegbczu"));
        Submission sub4 = new Submission("Derek Lee","aaaluckycharmz",fixture.calculateScore("aaaluckycharmz"));
        Submission sub5 = new Submission("Bonnie Chang","bananaram",fixture.calculateScore("bananaram"));
        List<Submission> input = new ArrayList<>();
        input.add(sub1);
        input.add(sub2);
        input.add(sub3);
        input.add(sub4);
        input.add(sub5);
        when(submissionRepositoryMock.findAll()).thenReturn(input   );
        result = fixture.results();
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("Camille Diaz,7\nIvaana Bello,4\nBob Bloch,1\nBonnie Chang,0\nDerek Lee,-9",result.getBody());
    }

    @Test
    public void healthTest() {
        // No pre-recorded data
        SubmissionController submissionController = mock(SubmissionController.class);
        submissionController.setLastSubmission(null);
        ResponseEntity result = fixture.health();
        assertEquals("System uptime : Just now\nValid submissions : 0\nInvalid submissions : 0\nLast submission : No submissions yet.\n",result.getBody());
        assertEquals("200 OK",result.getStatusCode().toString());

        // lastSubmission non null and validCount > 0
        fixture.setLastSubmission(new Submission("last","last",1));
        fixture.setValidSubsCount(10);
        fixture.setInvalidSubsCount(10);
        when(submissionController.calculateTimeSince(any())).thenReturn("Beginning of time");
        result = fixture.health();
        assertEquals(10,fixture.getInvalidSubsCount().intValue());
        assertEquals(10,fixture.getValidSubsCount().intValue());
        assertTrue(result.getBody().toString().contains("System uptime : Just now\nValid submissions : 10\nInvalid submissions : 10\nLast submission : last at ") && result.getBody().toString().contains("(Just now ago)\n"));
        assertEquals("200 OK",result.getStatusCode().toString());
    }

    @Test
    public void resetTest() {
        doNothing().when(submissionRepositoryMock).deleteAll();
        fixture.setValidSubsCount(2);
        fixture.setInvalidSubsCount(3);
        fixture.setLastSubmission(new Submission("test","test",1));
        ResponseEntity result = fixture.reset();
        assertEquals(0,fixture.getValidSubsCount().intValue());
        assertEquals(0,fixture.getInvalidSubsCount().intValue());
        assertNull(fixture.getLastSubmission());
        assertEquals("200 OK",result.getStatusCode().toString());
        assertEquals("system reset",result.getBody());
    }

    @Test
    public void calculateScoreTest() {
        int result = fixture.calculateScore("aaaiibbbru");
        assertEquals(4,result);
        result = fixture.calculateScore("eiiegiebeici");
        assertEquals(7,result);
        result = fixture.calculateScore("eiiegbczu");
        assertEquals(1,result);
        result = fixture.calculateScore("aaaluckycharmz");
        assertEquals(-9,result);
        result = fixture.calculateScore("bananaram");
        assertEquals(0,result);
        result = fixture.calculateScore("");
        assertEquals(0,result);
        result = fixture.calculateScore("   ");
        assertEquals(0,result);
    }

    private String generateString() {
        String seed = "abcdefghijklmnopqrst0123456789";
        StringBuilder result = new StringBuilder();
        while (result.length() < 11000) {
            result.append(seed);
        }
        return result.toString();
    }

    private List<Submission> getSubmissions(int n) {
        List<Submission> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(new Submission("name","string", 2));
        }
        return result;
    }
}
