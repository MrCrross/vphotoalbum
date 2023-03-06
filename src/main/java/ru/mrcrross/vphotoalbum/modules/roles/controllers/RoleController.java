package ru.mrcrross.vphotoalbum.modules.roles.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.mrcrross.vphotoalbum.models.Role;
import ru.mrcrross.vphotoalbum.modules.permissions.services.PermissionService;
import ru.mrcrross.vphotoalbum.modules.roles.services.RoleService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/role")
public class RoleController extends ControllerWrapper {
    private final RoleService roleService;
    private final PermissionService permissionService;

    @Autowired
    public RoleController(RoleService roleService, PermissionService permissionService)
    {
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    @GetMapping("")
    public String index(Model model, HttpSession session)
    {
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        model.addAttribute("roles", roleService.getAll());
        return "views/role/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id, Model model, HttpSession session)
    {
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        Role role = roleService.getOne(id);
        model.addAttribute("role", role);
        model.addAttribute("params", permissionService.getByIds(role.getParams()));
        return "views/role/show";
    }

    @GetMapping("add")
    public String add(Model model, HttpSession session)
    {
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        model.addAttribute("role", new Role());
        model.addAttribute("params", permissionService.getAll());
        return "views/role/add";
    }

    @GetMapping("{id}/edit")
    public String edit(@PathVariable("id") int id, Model model, HttpSession session)
    {
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        model.addAttribute("role", roleService.getOne(id));
        model.addAttribute("params", permissionService.getAll());
        return "views/role/edit";
    }

    @PostMapping("")
    public String insert(@ModelAttribute("role") @Valid Role role, BindingResult result, Model model, HttpSession session)
    {
        String loginControl = this.loginControl(session);
        if (!loginControl.equals("")) return loginControl;
        model.addAttribute("params", permissionService.getAll());
        if (result.hasErrors()) {
            return "views/role/add";
        }
        roleService.add(role);
        return "views/role/add";
    }
}
