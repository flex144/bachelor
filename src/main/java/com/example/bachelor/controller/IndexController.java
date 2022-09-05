package com.example.bachelor.controller;

import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {



    @RequestMapping(value= {"/index"})
    public String getIndex() {
        return HtmlConstants.INDEX;
    }

    @RequestMapping(value = {"/login", "/"})
    public String login() {

        return HtmlConstants.LOGIN_PAGE;
    }

    @RequestMapping(value= {"/registration"})
    public String registration() {

        return HtmlConstants.REGISTRATION;
    }

}
