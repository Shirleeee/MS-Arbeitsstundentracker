package de.vfh.workhourstracker.ui.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/hello") //TODO austauschen!!
    public String hello() {
        return "Hello from Java Backend!";
    }
}