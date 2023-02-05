package com.example.bachelor.controller;

import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"errorMessage"})
public class IndexController {

    @Autowired
    UserService userService;

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


    @RequestMapping(value = "/confirm", method = {RequestMethod.GET, RequestMethod.POST})
    public String confirmUserAccount(RedirectAttributes redirectAttributes, @RequestParam("token") String
            confirmationToken, Model model) {

        ConfirmationTokenEntity token;
        try {
            token = userService.findConfirmationToken(confirmationToken);
        } catch (IllegalStateException e) {
            model.addAttribute("error", "Link is invalid or broken!");
            return "error";
        }

        userService.confirmUser(token.getEmailUser());
        redirectAttributes.addFlashAttribute("verified", "Account wurde verifiziert");
        userService.deleteConfirmationTokenById(token.getTokenId());
        return "redirect:/login";
    }

}
