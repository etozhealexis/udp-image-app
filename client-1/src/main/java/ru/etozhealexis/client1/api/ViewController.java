package ru.etozhealexis.client1.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping
    public String getMainPage() {
        return "main.html";
    }
}
