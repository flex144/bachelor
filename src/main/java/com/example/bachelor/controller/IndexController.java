package com.example.bachelor.controller;

import com.example.bachelor.data.dto.UserDto;
import com.example.bachelor.data.entities.ConfirmationTokenEntity;
import com.example.bachelor.services.EmailSenderService;
import com.example.bachelor.services.UserService;
import com.example.bachelor.utility.constants.HtmlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes({"errorMessage"})
public class IndexController {

    @Autowired
    UserService userService;

    @Autowired
    EmailSenderService emailSenderService;

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
        return HtmlConstants.REDIRECT + "login";
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
            redirectAttributes.addFlashAttribute("errorMessage", "Link is invalid or broken!");
            return HtmlConstants.REDIRECT + "login";
        }

        userService.confirmUser(token.getEmailUser());
        redirectAttributes.addFlashAttribute("verified", "Account wurde verifiziert! Wenn sie bereits aktiviert wurden können sie sich jetzt anmelden.");
        userService.deleteConfirmationTokenById(token.getTokenId());
        return HtmlConstants.REDIRECT + "login";
    }

    @RequestMapping(value="/forgotPassword", method = RequestMethod.GET)
    public String getForgotPassword(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("userDto", userDto);
        return HtmlConstants.FORGOT_PASSWORD;
    }

    @RequestMapping(value="/forgotPassword", method = RequestMethod.POST)
    public String postForgotPassword(RedirectAttributes redirectAttributes,
                                     @ModelAttribute("userDto") UserDto userDto) {
        String errorMessage = "";
        UserDto checkUser = userService.findUserDtoByEmail(userDto.getEmail());
        if (userDto.getEmail() == null || userDto.getEmail().equals("")) {
            errorMessage = "E-Mail Feld muss ausgefüllt werden";
            redirectAttributes.addFlashAttribute("errorMessagePW", errorMessage);
            return HtmlConstants.REDIRECT + "forgotPassword";
        } else if (checkUser == null) {
            errorMessage = "Nutzer nicht registriert!";
            redirectAttributes.addFlashAttribute("errorMessagePW", errorMessage);
            return HtmlConstants.REDIRECT + "forgotPassword";
        } else {
            String newPassword = userService.setRandomUserPassword(userDto.getEmail());

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userDto.getEmail());
            mailMessage.setSubject("Neues Passwort!");
            String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
            String url = uri + "/login";
            mailMessage.setText("Ihr neues Passwort ist '"+newPassword+"' . Bitte melden Sie sich an " +
                    "unter '"+url+"' und ändern Sie es schnellst möglich!");
            emailSenderService.sendEmail(mailMessage);

            redirectAttributes.addFlashAttribute("verified", "Passwort wurde zurückgesetzt!");
            return HtmlConstants.REDIRECT + "login";
        }
    }

}
