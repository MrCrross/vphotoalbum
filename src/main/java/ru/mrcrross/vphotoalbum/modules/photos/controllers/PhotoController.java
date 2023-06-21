package ru.mrcrross.vphotoalbum.modules.photos.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoAlbumService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

@Controller
@RequestMapping(path = "/photo")
public class PhotoController extends ControllerWrapper {
    private final PhotoService photoService;
    private final PhotoAlbumService albumService;

    public PhotoController(PhotoService photoService, PhotoAlbumService albumService)
    {
        this.photoService = photoService;
        this.albumService = albumService;
    }
    @GetMapping("")
    public String index(HttpSession session)
    {
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
        model.addAttribute("photo", photoService.getByID(id));
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
        model.addAttribute("album", albumService.getByID(id));
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
        albumService.delete(id);
        return "redirect:/photo";
    }
}
