package ru.mrcrross.vphotoalbum.modules.photos.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoAlbumService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/photo")
public class PhotoController extends ControllerWrapper {
    private final PhotoService photoService;
    private final PhotoAlbumService albumService;
    private final UserService userService;

    public PhotoController(PhotoService photoService, PhotoAlbumService albumService, UserService userService)
    {
        this.photoService = photoService;
        this.albumService = albumService;
        this.userService = userService;
    }
    @GetMapping("")
    public String index(HttpSession session) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        return "views/photo/index";
    }

    @GetMapping("{id}")
    public String showPhoto(
            @PathVariable("id") int id,
            Model model,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        if (photoService.checkViewer(id, (User)session.getAttribute("user"))) {
            return "redirect:/";
        }
        model.addAttribute("photo", photoService.getByID(id));
        model.addAttribute("users", userService.getAll());
        return "views/photo/photo";
    }

    @GetMapping("album/{id}")
    public String showAlbum(
            @PathVariable("id") int id,
            Model model,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        if (albumService.checkViewer(id, (User)session.getAttribute("user"))) {
            return "redirect:/";
        }
        model.addAttribute("album", albumService.getByID(id));
        model.addAttribute("users", userService.getAll());
        return "views/photo/album";
    }

    @GetMapping(path = "{id}/delete")
    public String deletePhoto(
            @PathVariable("id") int id,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        if (photoService.checkOwner(id, (User)session.getAttribute("user"))) {
            return "redirect:/";
        }
        photoService.delete(id);
        return "redirect:/photo";
    }

    @GetMapping(path = "album/{id}/delete")
    public String deleteAlbum(
            @PathVariable("id") int id,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        if (albumService.checkOwner(id, (User)session.getAttribute("user"))) {
            return "redirect:/";
        }
        albumService.delete(id);
        return "redirect:/photo";
    }

    @GetMapping(path = "viewer")
    public String getViewerAlbums(
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            return "redirect:/";
        }
        return "views/photo/viewer";
    }
}
