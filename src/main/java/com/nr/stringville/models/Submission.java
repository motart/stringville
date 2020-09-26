package com.nr.stringville.models;

import com.sun.corba.se.spi.ior.ObjectId;

import javax.persistence.*;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private String string;

    public Submission(){
    }

    public Submission(String name, String string){
        this.name = name;
        this.string = string;
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

}
