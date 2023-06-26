package ru.mrcrross.vphotoalbum.modules.user.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.auth.services.AuthService;
import ru.mrcrross.vphotoalbum.modules.roles.services.RoleService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserAvatarService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(path = "/user")
public class UserController extends ControllerWrapper {
    private final UserService userService;
    private final RoleService roleService;
    private final AuthService authService;
    private final UserAvatarService userAvatarService;
    private final String success = "Пользователь сохранен успешно";

    @Autowired
    public UserController(JdbcTemplate db, Environment env, UserService userService, RoleService roleService, AuthService authService, UserAvatarService userAvatarService) {
        super(db, env);
        this.userService = userService;
        this.roleService = roleService;
        this.authService = authService;
        this.userAvatarService = userAvatarService;
    }

    @GetMapping("")
    public String index(Model model, HttpSession session) {
        if (this.paramsControl(session, "user_viewer")) {
            return "redirect:/";
        }
        model.addAttribute("users", userService.getAll());
        userService.saveUserAction((User) session.getAttribute("user"), "GET /user");
        return "views/user/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model, HttpSession session) {
        if (this.paramsControl(session, "user_viewer")) {
            return "redirect:/";
        }
        model.addAttribute("user", userService.get(id));
        model.addAttribute("roles", userService.getRolesById(id));
        String path = "GET /user/" + id;
        userService.saveUserAction((User) session.getAttribute("user"), path);
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
        userService.saveUserAction((User) session.getAttribute("user"), "GET /user/add");
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
        String path = "GET /user/" + id + "/edit";
        userService.saveUserAction((User) session.getAttribute("user"), path);
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
        } else {
            user.setAvatar("/img/avatar.svg");
        }
        int id = userService.add(user, roles);
        if (!avatar.isEmpty()) {
            user.setAvatar(userAvatarService.save(id, avatar));
            userService.update(id, user);
        }
        userService.saveUserAction((User) session.getAttribute("user"), "POST /user");
        return "redirect:/user/add?success=1";
    }

    @PostMapping("{id}")
    public String update(
            @PathVariable("id") int id,
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
        if (result.hasFieldErrors("login") || result.hasFieldErrors("fio")) {
            model.addAttribute("user", userService.get(id));
            model.addAttribute("roles", roleService.getAll());
            return "views/user/edit";
        }
        if (!avatar.isEmpty()) {
            String checkExtensions = userAvatarService.validateExtension(avatar);
            if (!checkExtensions.equals("")) {
                return "redirect:/user/" + id + "/edit?error=" + URLEncoder.encode(checkExtensions, StandardCharsets.UTF_8);
            }
        }
        userService.update(id, user);
        userService.updateRoles(id, roles);
        if (!avatar.isEmpty()) {
            user.setAvatar(userAvatarService.save(id, avatar));
            userService.update(id, user);
        }
        User sessionUser = (User) session.getAttribute("user");
        if (id == sessionUser.getId()) {
            session.removeAttribute("user");
            session.setAttribute("user", authService.getUserSession(id));
        }
        String path = "POST /user/" + id;
        userService.saveUserAction((User) session.getAttribute("user"), path);
        return "redirect:/user/" + id + "/edit?success=1";
    }

    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        if (this.paramsControl(session, "user_changed")) {
            return "redirect:/";
        }
        userService.delete(id);
        String path = "GET /user/" + id + "/delete";
        userService.saveUserAction((User) session.getAttribute("user"), path);
        return "redirect:/user";
    }
}
