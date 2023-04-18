package ru.mrcrross.vphotoalbum.modules.auth.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;
import org.springframework.ui.Model;

@Controller
public class AuthRegistrationController {

    public AuthService authService;

    @Autowired
    public AuthRegistrationController(AuthService authService)
    {
        this.authService = authService;
    }

    @GetMapping("/registration")
    public String index(Model model)
    {
        model.addAttribute("user", new User());
        return "views/auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, HttpSession session)
    {
        if (result.hasFieldErrors()) {
            return "views/auth/registration";
        }
        int userId = authService.loginValidate(user.getLogin(), user.getPassword());
        if (userId != 0) {
            model.addAttribute("error", "Такой пользователь уже зарегистрирован.");
            model.addAttribute("user", new User());
            return "views/auth/registration";
        }
        int userID = authService.registration(user);
        session.setAttribute("user", authService.getUserSession(userID));
        return "redirect:/";
    }
}
