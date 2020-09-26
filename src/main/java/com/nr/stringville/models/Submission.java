package com.nr.stringville.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Submission implements Comparable<Submission>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String string;
    private LocalDateTime time;
    private int score;

    public Submission(){
    }

    public Submission(String name, String string, int score){
        this.name = name;
        this.string = string;
        this.time = LocalDateTime.now();
        this.score = score;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getString() {
        return string;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int compareTo(Submission o) {
        return score - o.score;
    }
}
