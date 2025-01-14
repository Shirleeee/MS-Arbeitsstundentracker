package de.vfh.workhourstracker.ui.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class HomeController {
    @RequestMapping(value = "/", produces = "text/html")
    public String index() {
        return "index"; // Liefert die Vue.js-Startseite aus
    }
}