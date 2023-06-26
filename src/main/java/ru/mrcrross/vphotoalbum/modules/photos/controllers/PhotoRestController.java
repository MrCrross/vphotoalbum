package ru.mrcrross.vphotoalbum.modules.photos.controllers;

import jakarta.servlet.http.HttpSession;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.models.*;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoCategoryService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoFileService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoService;
import ru.mrcrross.vphotoalbum.modules.photos.services.PhotoTreeService;
import ru.mrcrross.vphotoalbum.wrappers.ControllerWrapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/photo")
public class PhotoRestController extends ControllerWrapper {
    private final PhotoCategoryService photoCategoryService;
    private final PhotoService photoService;
    private final PhotoFileService fileService;
    private final PhotoTreeService treeService;

    @Autowired
    public PhotoRestController(
            JdbcTemplate db, Environment env,
            PhotoCategoryService photoCategoryService,
            PhotoService photoService,
            PhotoFileService fileService,
            PhotoTreeService treeService
    ) {
        super(db, env);
        this.photoCategoryService = photoCategoryService;
        this.photoService = photoService;
        this.fileService = fileService;
        this.treeService = treeService;
    }

    @GetMapping(path = "category/get")
    public ResponseEntity<Object> getCategories() {
        List<PhotoCategory> categories = photoCategoryService.getForSelect();
        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (PhotoCategory category : categories) {
            JSONObject entity = new JSONObject();
            entity.put("id", category.getId());
            entity.put("name", category.getName());
            entities.add(entity);
        }
        return new ResponseEntity<Object>(entities, HttpStatus.OK);
    }

    @GetMapping(path = "tree")
    public ResponseEntity<Object> getTree(
            @RequestParam(value = "id", required = false) Integer id,
            @RequestParam(value = "type") String type,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
        List<Object> response = new ArrayList<>();
        List<JSONObject> breadCrumb = treeService.getBreadcrumb(id);
        List<JSONObject> tree = treeService.get(id, currentUser);
        JSONObject users = new JSONObject();
        users.put("current", currentUser.getId());
        response.add(breadCrumb);
        response.add(tree);
        response.add(users);
        this.saveUserAction(currentUser, "GET /photo/tree");
        return new ResponseEntity<Object>(response, HttpStatus.OK);
    }

    @GetMapping(path = "category/tree")
    public ResponseEntity<Object> getCategoryTree() {
        return new ResponseEntity<Object>(photoCategoryService.getTree(), HttpStatus.OK);
    }

    @PostMapping(path = "category", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCategory(
            @RequestBody PhotoCategory category,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
        category.setOwnerID(currentUser.getId());
        int categoryID = photoCategoryService.add(category);
        JSONObject entity = new JSONObject();
        entity.put("id", categoryID);
        this.saveUserAction(currentUser, "POST /photo/category");
        return new ResponseEntity<Object>(entity, HttpStatus.OK);
    }

    @PostMapping(path = "category/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editCategory(
            @PathVariable("id") int categoryID,
            @RequestBody PhotoCategory category,
            HttpSession session
    ) {
        if (this.paramsControl(session, "category_changed")) {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Недостаточно прав");
            return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
        }
        photoCategoryService.update(categoryID, category);
        PhotoCategory currentCategory = photoCategoryService.getByID(categoryID);
        JSONObject json = new JSONObject();
        json.put("parent", currentCategory.getParent());
        json.put("name", currentCategory.getName());
        json.put("description", currentCategory.getDescription());
        json.put("dateEdit", currentCategory.getDateEdit());
        this.saveUserAction((User) session.getAttribute("user"), "POST /photo/category/" + categoryID);
        return new ResponseEntity<Object>(json, HttpStatus.OK);
    }

    @PostMapping(path = "")
    public ResponseEntity<Object> addPhoto(
            @ModelAttribute Photo photo,
            @RequestParam(value = "file") MultipartFile file,
            HttpSession session
    ) {
        User currentUser = (User) session.getAttribute("user");
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
        this.saveUserAction((User) session.getAttribute("user"), "POST /photo");
        return new ResponseEntity<Object>(returns, HttpStatus.OK);
    }

    @PostMapping(path = "{id}")
    public ResponseEntity<Object> editPhoto(
            @PathVariable("id") int photoID,
            @ModelAttribute Photo photo,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session
    ) {
        if (photoService.checkOwner(photoID, (User) session.getAttribute("user"))) {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Недостаточно прав");
            return new ResponseEntity<Object>(error, HttpStatus.FORBIDDEN);
        }
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
        jsonPhoto.put("category", currentPhoto.getCategory());
        jsonPhoto.put("path", currentPhoto.getPath());
        jsonPhoto.put("name", currentPhoto.getName());
        jsonPhoto.put("description", currentPhoto.getDescription());
        jsonPhoto.put("dateEdit", currentPhoto.getDateEdit());
        this.saveUserAction((User) session.getAttribute("user"), "POST /photo/" + photoID);
        return new ResponseEntity<Object>(jsonPhoto, HttpStatus.OK);
    }

    @PostMapping(path = "{id}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> addCommentPhoto(
            @PathVariable("id") int photoID,
            @RequestBody PhotoComment comment,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Неавторизованный пользователь");
            return new ResponseEntity<Object>(error, HttpStatus.UNAUTHORIZED);
        }
        comment.setUserID(((User) session.getAttribute("user")).getId());
        photoService.addComment(comment);
        this.saveUserAction((User) session.getAttribute("user"), "POST /photo/" + photoID + "/comment");
        return new ResponseEntity<Object>(photoService.getByID(photoID).getComments(), HttpStatus.OK);
    }

    @GetMapping(path = "get")
    public ResponseEntity<Object> getPagePhotos(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "owner_id", required = false) Integer owner,
            @RequestParam(value = "category[]", required = false) List<Integer> categories,
            @RequestParam(value = "search", required = false, defaultValue = "") String search
    ) {
        List<Photo> photos = photoService.getPage(page, categories, search, owner);
        JSONObject jsonPage = new JSONObject();
        jsonPage.put("data", photos);
        jsonPage.put("currentPage", page);
        jsonPage.put("maxPage", photoService.countPage(categories, search, owner));
        return new ResponseEntity<Object>(jsonPage, HttpStatus.OK);
    }

    @PostMapping(path = "comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> saveComment(
            @RequestBody PhotoComment comment,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Неавторизованный пользователь");
            return new ResponseEntity<Object>(error, HttpStatus.UNAUTHORIZED);
        }
        comment.setUserID(((User) session.getAttribute("user")).getId());
        photoService.addComment(comment);
        this.saveUserAction((User) session.getAttribute("user"), "POST /photo/" + comment.getPhotoID() + "/comment");
        return new ResponseEntity<Object>(photoService.getByID(comment.getPhotoID()).getComments(), HttpStatus.OK);
    }

    @GetMapping(path = "{photoID}/comment/{commentID}")
    public ResponseEntity<Object> deleteComment(
            @PathVariable("photoID") Integer photoID,
            @PathVariable("commentID") Integer commentID,
            HttpSession session
    ) {
        if (this.loginControl(session)) {
            JSONObject error = new JSONObject();
            error.put("error", "1");
            error.put("message", "Неавторизованный пользователь");
            return new ResponseEntity<Object>(error, HttpStatus.UNAUTHORIZED);
        }
        photoService.deleteComment(commentID);
        this.saveUserAction((User) session.getAttribute("user"), "GET /photo/" + photoID + "/comment/" + commentID);
        return new ResponseEntity<Object>(photoService.getByID(photoID).getComments(), HttpStatus.OK);
    }
}
