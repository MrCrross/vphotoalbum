package ru.mrcrross.vphotoalbum.modules.user.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/account")
public class UserAccountController extends ControllerWrapper {

    @GetMapping("/")
    public String account(HttpSession session)
    {
        if (this.loginControl(session)) return "redirect:/";
        return "views/user/account";
    }
}
