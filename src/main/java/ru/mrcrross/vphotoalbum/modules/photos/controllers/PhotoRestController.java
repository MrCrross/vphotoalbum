package ru.mrcrross.vphotoalbum.modules.photos.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.models.Photo;
import ru.mrcrross.vphotoalbum.models.PhotoAlbum;
import ru.mrcrross.vphotoalbum.models.User;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoAlbumService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoFileService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoTreeService;
import ru.mrcrross.vphotoalbum.modules.user.services.UserService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/photo")
public class PhotoRestController extends ControllerWrapper {
    private final PhotoAlbumService photoAlbumService;
    private final PhotoService photoService;
    private final PhotoFileService fileService;
    private final PhotoTreeService treeService;
    private final UserService userService;

    @Autowired
    public PhotoRestController(
            PhotoAlbumService photoAlbumService,
            PhotoService photoService,
            PhotoFileService fileService,
            PhotoTreeService treeService,
            UserService userService
    ) {
        this.photoAlbumService = photoAlbumService;
        this.photoService = photoService;
        this.fileService = fileService;
        this.treeService = treeService;
        this.userService = userService;
    }

    @GetMapping(path = "album/get")
    public ResponseEntity<Object> getAlbums(
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
        List<PhotoAlbum> albums = photoAlbumService.getForSelect(currentUser);
        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (PhotoAlbum album : albums) {
            JSONObject entity = new JSONObject();
            entity.put("id", album.getId());
            entity.put("name", album.getName());
            entities.add(entity);
        }
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @GetMapping(path = "tree")
    public ResponseEntity<Object> getTree(
            @RequestParam(value="id", required=false) Integer id,
            @RequestParam(value="owner", required=false) Integer owner,
            HttpSession session
    ) {
        User user = (User)session.getAttribute("user");
        if (owner != null) {
            user = userService.get(owner.intValue());
        }
        List<List<JSONObject>> response = new ArrayList<>();
        List<JSONObject> breadCrumb = treeService.getBreadcrumb(id);
        List<JSONObject> tree = treeService.get(id, user);
        response.add(breadCrumb);
        response.add(tree);
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @PostMapping(path = "album", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addAlbum(
            @RequestBody PhotoAlbum album,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
        album.setOwnerID(currentUser.getId());
        int albumID = photoAlbumService.add(album);
        JSONObject entity = new JSONObject();
        entity.put("id", albumID);
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<Object> addPhoto(
            @ModelAttribute Photo photo,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session
    ) {
        User currentUser = (User)session.getAttribute("user");
        photo.setOwnerID(currentUser.getId());
        List<JSONObject> returns = new ArrayList<JSONObject>();
        if (!file.isEmpty()) {
            String checkExtensions = fileService.validateExtension(file);
            if (!checkExtensions.equals("")) {
                JSONObject error = new JSONObject();
                error.put("error", "1");
                error.put("message", "Загруженный файл неверного формата");
                returns.add(error);
                return new ResponseEntity<Object>(returns, HttpStatus.FAILED_DEPENDENCY);
            }
        } else {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Не загружен файл");
            returns.add(error);
            return new ResponseEntity<Object>(returns, HttpStatus.FAILED_DEPENDENCY);
        }
        int photoID = photoService.add(photo);
        photo.setPath(fileService.save(currentUser.getId(), photoID, file));
        photoService.update(photoID, photo);
        return new ResponseEntity<Object>(returns, HttpStatus.OK);
    }

    @PostMapping(path = "{id}")
    public ResponseEntity<Object> editPhoto(
            @PathVariable("id") int photoID,
            @ModelAttribute Photo photo,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        System.out.println(photo.getAlbumID());
        List<JSONObject> returns = new ArrayList<JSONObject>();
        if (file != null && !file.isEmpty()) {
            String checkExtensions = fileService.validateExtension(file);
            if (!checkExtensions.equals("")) {
                JSONObject error = new JSONObject();
                error.put("error", "1");
                error.put("message", "Загруженный файл неверного формата");
                returns.add(error);
                return new ResponseEntity<Object>(returns, HttpStatus.FAILED_DEPENDENCY);
            }
            photo.setPath(fileService.save(photo.getOwnerID(), photoID, file));
        }
        photoService.update(photoID, photo);
        Photo currentPhoto = photoService.getByID(photoID);
        JSONObject jsonPhoto = new JSONObject();
        jsonPhoto.put("albumID", currentPhoto.getAlbumID());
        jsonPhoto.put("albumName", currentPhoto.getAlbumID() != 0 ? currentPhoto.getAlbum().getName() : "");
        jsonPhoto.put("path", currentPhoto.getPath());
        jsonPhoto.put("name", currentPhoto.getName());
        jsonPhoto.put("description", currentPhoto.getDescription());
        jsonPhoto.put("dateEdit", currentPhoto.getDateEdit());
        return new ResponseEntity<Object>(jsonPhoto, HttpStatus.OK);
    }
}
