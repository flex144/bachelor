package com.example.bachelor.controller;

import com.example.bachelor.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {



    @RequestMapping(value= {"/index"})
    public String getIndex() {
        return "index";
    }

    @RequestMapping(value = {"/login", "/"})
    public String login() {
        return "login_page";
    }

    @RequestMapping(value= {"/registration"})
    public String registration() {
        return "registration";
    }

}
