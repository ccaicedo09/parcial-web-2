package org.usco.parcialdos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("forbidden")
    public String forbidden() {
        return "forbidden";
    }
}
