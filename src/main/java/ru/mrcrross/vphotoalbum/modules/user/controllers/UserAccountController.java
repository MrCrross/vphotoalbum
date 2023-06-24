package ru.mrcrross.vphotoalbum.modules.user.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
@RequestMapping(path = "/account")
public class UserAccountController extends ControllerWrapper {

    private final UserService userService;
    private final RoleService roleService;
    private final UserAvatarService userAvatarService;
    private final AuthService authService;
    private final String success = "Данные сохранены";

    public UserAccountController(UserService userService, RoleService roleService, UserAvatarService userAvatarService, AuthService authService){
        this.userService = userService;
        this.roleService = roleService;
        this.userAvatarService = userAvatarService;
        this.authService = authService;
    }

    @GetMapping("")
    public String account(
            @RequestParam(value = "success", required = false) String success,
            @RequestParam(value = "error", required = false) String error,
            Model model,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("user", userService.get(sessionUser.getId()));
        model.addAttribute("roles", roleService.getAll());
        if (error != null) {
            model.addAttribute("error", error);
        }
        if (success != null) {
            model.addAttribute("success", this.success);
        }
        return "views/user/account";
    }

    @PostMapping("")
    public String update(
            @RequestParam(value = "file", required = false) MultipartFile avatar,
            @RequestParam(value = "roles", required = false) int[] roles,
            @ModelAttribute("user") @Valid User user,
            BindingResult result,
            Model model,
            HttpSession session
    ) {
        User sessionUser = (User) session.getAttribute("user");
        int id = sessionUser.getId();
        if (result.hasFieldErrors("login") || result.hasFieldErrors("fio")) {
            model.addAttribute("user", userService.get(id));
            model.addAttribute("roles", roleService.getAll());
            return "views/user/account";
        }
        if (!avatar.isEmpty()) {
            String checkExtensions = userAvatarService.validateExtension(avatar);
            if (!checkExtensions.equals("")) {
                return "redirect:/account?error=" + URLEncoder.encode(checkExtensions, StandardCharsets.UTF_8);
            }
        }
        userService.update(id, user);
        userService.updateRoles(id, roles);
        if (!avatar.isEmpty()) {
            user.setAvatar(userAvatarService.save(id, avatar));
            userService.update(id, user);
        }
        if (id == sessionUser.getId()) {
            session.removeAttribute("user");
            session.setAttribute("user", authService.getUserSession(id));
        }
        return "redirect:/account?success=1";
    }
}
