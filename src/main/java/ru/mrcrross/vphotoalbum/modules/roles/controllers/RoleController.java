package ru.mrcrross.vphotoalbum.modules.roles.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.permissions.services.PermissionService;
import ru.mrcrross.vphotoalbum.modules.roles.services.RoleService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/role")
public class RoleController extends ControllerWrapper {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public RoleController(JdbcTemplate db, Environment env, RoleService roleService, PermissionService permissionService)
    {
        super(db, env);
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("")
    public String index(Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_viewer")) {
            return "redirect:/";
        }
        model.addAttribute("roles", roleService.getAll());
        this.saveUserAction((User) session.getAttribute("user"), "GET /role");
        return "views/role/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_viewer")) {
            return "redirect:/";
        }
        Role role = roleService.getOne(id);
        model.addAttribute("role", role);
        model.addAttribute("params", permissionService.getByIds(role.getParams()));
        this.saveUserAction((User) session.getAttribute("user"), "GET /role/" + id);
        return "views/role/show";
    }

    @GetMapping("add")
    public String add(@RequestParam(value = "success", required = false) String success, Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_changed")) {
            return "redirect:/";
        }
        model.addAttribute("role", new Role());
        model.addAttribute("params", permissionService.getAll());
        if (success != null) {
            model.addAttribute("success", "Роль сохранена успешно");
        }
        this.saveUserAction((User) session.getAttribute("user"), "GET /role/add");
        return "views/role/add";
    }

    @GetMapping("{id}/edit")
    public String edit(@RequestParam(value = "success", required = false) String success, @PathVariable("id") int id, Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_changed")) {
            return "redirect:/";
        }
        model.addAttribute("role", roleService.getOne(id));
        model.addAttribute("params", permissionService.getAll());
        if (success != null) {
            model.addAttribute("success", "Роль сохранена успешно");
        }
        this.saveUserAction((User) session.getAttribute("user"), "GET /role/" + id + "/edit");
        return "views/role/edit";
    }

    @PostMapping("")
    public String insert(@ModelAttribute("role") @Valid Role role, BindingResult result, Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_changed")) {
            return "redirect:/";
        }
        model.addAttribute("params", permissionService.getAll());
        if (result.hasErrors()) {
            return "views/role/add";
        }
        roleService.add(role);
        this.saveUserAction((User) session.getAttribute("user"), "POST /role");
        return "redirect:/role/add?success=1";
    }

    @PostMapping("{id}")
    public String update(@PathVariable("id") int id, @ModelAttribute("role") @Valid Role role, BindingResult result, Model model, HttpSession session)
    {
        if (this.paramsControl(session, "role_changed")) {
            return "redirect:/";
        }
        model.addAttribute("params", permissionService.getAll());
        if (result.hasErrors()) {
            return "views/role/" + id + "/edit";
        }
        roleService.update(id, role);
        model.addAttribute("role", new Role());
        this.saveUserAction((User) session.getAttribute("user"), "POST /role/" + id);
        return "redirect:/role/" + id + "/edit?success=1";
    }
    @GetMapping("{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session)
    {
        if (this.paramsControl(session, "role_deleted")) {
            return "redirect:/";
        }
        roleService.delete(id);
        this.saveUserAction((User) session.getAttribute("user"), "POST /role/" + id + "/delete");
        return "redirect:/role";
    }
}
