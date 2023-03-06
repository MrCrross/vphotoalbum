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
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        return "views/user/account";
    }
}
