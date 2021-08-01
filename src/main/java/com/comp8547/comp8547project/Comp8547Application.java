package com.comp8547.comp8547project;

import com.comp8547.comp8547project.webcrawler.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Comp8547Application {

    public static void main(String[] args) {
        SpringApplication.run(Comp8547Application.class, args
        );
        Test.main(args);
    }
}
