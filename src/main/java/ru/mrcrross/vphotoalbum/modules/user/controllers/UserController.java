package ru.mrcrross.vphotoalbum.modules.user.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/user")
public class UserController extends ControllerWrapper{
    @GetMapping("")
    public String index(Model model, HttpSession session) {

        return "views/user/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model, HttpSession session) {
        return null;
    }

    @GetMapping("add")
    public String add(@RequestParam(value = "success", required = false) String success, Model model, HttpSession session) {
        return null;
    }

    @GetMapping("{id}/edit")
    public String edit(@RequestParam(value = "success", required = false) String success, @PathVariable("id") int id, Model model, HttpSession session) {
        return null;
    }

    @PostMapping("")
    public String insert(@ModelAttribute("user") @Valid User user, BindingResult result, Model model, HttpSession session) {
        return null;
    }

    @PostMapping("{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") @Valid User user, BindingResult result, Model model, HttpSession session) {
        return null;
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        return null;
    }
}
