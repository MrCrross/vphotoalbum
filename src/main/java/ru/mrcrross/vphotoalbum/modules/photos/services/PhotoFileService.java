package ru.mrcrross.vphotoalbum.modules.photos.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.wrappers.FileWrapper;

@Component
public class PhotoFileService extends FileWrapper {
    public String save(int userID, int photoID, MultipartFile file) {
        String fileName = "photo_" + photoID;
        String url = "/img/users/" + userID + "/photos";
        return writeFile(url, fileName, file);
    }
}
