package com.example.bachelor.controller;

import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"errorMessage"})
public class IndexController {



    @RequestMapping(value= {"/index"})
    public String getIndex() {
        return HtmlConstants.INDEX;
    }

    @RequestMapping(value = {"/login", "/"})
    public String login() {

        return HtmlConstants.LOGIN_PAGE;
    }

    @GetMapping("/login-error")
    public String login(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                if (ex.getMessage().contains("Bad credentials")) {
                    errorMessage = "E-Mail oder Passwort falsch!";
                } else if (ex.getMessage().contains("disabled")) {
                    errorMessage = "Nutzer noch nicht aktiviert!";
                } else {
                    errorMessage = ex.getMessage();
                }
            }
        }
        model.addAttribute("errorMessage", errorMessage);
        return "redirect:/login";
    }

    @RequestMapping(value= {"/registration"})
    public String registration() {

        return HtmlConstants.REGISTRATION;
    }

}
