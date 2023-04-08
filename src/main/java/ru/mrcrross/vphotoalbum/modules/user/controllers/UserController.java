package ru.mrcrross.vphotoalbum.modules.user.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.roles.services.RoleService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserAvatarService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(path = "/user")
public class UserController extends ControllerWrapper {

    private final UserService userService;
    private final RoleService roleService;
    private final String UPLOAD_DIR = "/img/users/";
    private final UserAvatarService userAvatarService;
    private final String success = "Пользователь сохранен успешно";

    @Autowired
    public UserController(UserService userService, RoleService roleService, UserAvatarService userAvatarService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userAvatarService = userAvatarService;
    }

    @GetMapping("")
    public String index(Model model, HttpSession session) {
        if (this.paramsControl(session, "user_viewer")) {
            return "redirect:/";
        }
        model.addAttribute("users", userService.getAll());
        return "views/user/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model, HttpSession session) {
        if (this.paramsControl(session, "user_viewer")) {
            return "redirect:/";
        }
        model.addAttribute("user", userService.get(id));
        return "views/user/show";
    }

    @GetMapping("add")
    public String add(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            HttpSession session
    ) {
        if (this.paramsControl(session, "user_changed")) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAll());
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", this.success);
        }
        return "views/user/add";
    }

    @GetMapping("{id}/edit")
    public String edit(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            @PathVariable("id") int id,
            Model model,
            HttpSession session
    ) {
        if (this.paramsControl(session, "user_changed")) {
            return "redirect:/";
        }
        model.addAttribute("user", userService.get(id));
        model.addAttribute("roles", roleService.getAll());
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", this.success);
        }
        return "views/user/edit";
    }

    @PostMapping("")
    public String insert(
            @RequestParam(value = "file", required = false) MultipartFile avatar,
            @RequestParam(value = "roles", required = false) int[] roles,
            @ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model,
            HttpSession session
    ) {
        if (this.paramsControl(session, "user_changed")) {
            return "redirect:/";
        }
        model.addAttribute("roles", roleService.getAll());
        if (result.hasErrors()) {
            return "views/user/add";
        }
        if (!avatar.isEmpty()) {
            String checkExtensions = userAvatarService.validateExtension(avatar);
            if (!checkExtensions.equals("")) {
                return "redirect:/user/add?error=" + URLEncoder.encode(checkExtensions, StandardCharsets.UTF_8);
            }
        }
        int id = userService.add(user, roles);
        if (!avatar.isEmpty()) {
            userAvatarService.save(id, avatar);
        }
        return "redirect:/user/add?success=1";
    }

    @PostMapping("{id}")
    public String update(
            @PathVariable("id") int id,
            @RequestParam("avatar") MultipartFile avatar,
            @ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model,
            HttpSession session
    ) {
        return null;
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        return null;
    }
}
