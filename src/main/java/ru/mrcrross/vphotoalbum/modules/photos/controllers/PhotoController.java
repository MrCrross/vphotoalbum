package ru.mrcrross.vphotoalbum.modules.photos.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoCategoryService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/photo")
public class PhotoController extends ControllerWrapper {
    private final PhotoService photoService;
    private final PhotoCategoryService categoryService;
    private final UserService userService;

    public PhotoController(PhotoService photoService, PhotoCategoryService categoryService, UserService userService)
    {
        this.photoService = photoService;
        this.categoryService = categoryService;
        this.userService = userService;
    }
    @GetMapping("")
    public String index(HttpSession session) {
        if (this.loginControl(session)) {
            return "redirect:/login";
        }
        return "views/photo/index";
    }

    @GetMapping("category")
    public String indexCategory(Model model, HttpSession session) {
        if (this.paramsControl(session, "category_changed")) {
            return "redirect:/";
        }
        model.addAttribute("categories", categoryService.getAll());
        return "views/photo/category/index";
    }

    @GetMapping("{id}")
    public String showPhoto(
            @PathVariable("id") int id,
            Model model,
            HttpSession session
    ) {
        model.addAttribute("photo", photoService.getByID(id));
        if (!this.loginControl(session)) {
            model.addAttribute("users", userService.getAll());
        }
        return "views/photo/photo";
    }

    @GetMapping("category/{id}")
    public String showCategory(
            @PathVariable("id") int id,
            Model model,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/login";
        }
        model.addAttribute("category", categoryService.getByID(id));
        model.addAttribute("users", userService.getAll());
        return "views/photo/category";
    }

    @GetMapping(path = "{id}/delete")
    public String deletePhoto(
            @PathVariable("id") int id,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/login";
        }
        if (photoService.checkOwner(id, (User)session.getAttribute("user"))) {
            return "redirect:/";
        }
        photoService.delete(id);
        return "redirect:/photo";
    }

    @GetMapping(path = "category/{id}/delete")
    public String deleteCategory(
            @PathVariable("id") int id,
            HttpSession session
    ) {
        if (this.paramsControl(session, "category_deleted")) {
            return "redirect:/";
        }
        categoryService.delete(id);
        return "redirect:/photo";
    }
}
