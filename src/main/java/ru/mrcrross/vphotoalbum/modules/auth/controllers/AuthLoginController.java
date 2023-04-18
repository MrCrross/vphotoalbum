package ru.mrcrross.vphotoalbum.modules.auth.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;


@Controller
public class AuthLoginController {

    public AuthService authService;

    @Autowired
    public AuthLoginController(AuthService authService)
    {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "views/auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, HttpSession session) {
        if (result.hasFieldErrors("login") || result.hasFieldErrors("password")) {
            return "views/auth/login";
        }
        int userId = authService.loginValidate(user.getLogin(), user.getPassword());
        if (userId == 0) {
            model.addAttribute("error", "Неверный логин или пароль.");
            model.addAttribute("user", new User());
            return "views/auth/login";
        }
        User sessionUser = authService.getUserSession(userId);
        session.setAttribute("user", sessionUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session)
    {
        session.invalidate();
        return "redirect:/";
    }
}
