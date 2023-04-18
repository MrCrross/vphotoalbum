package ru.mrcrross.vphotoalbum.modules.user.services;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.mrcrross.vphotoalbum.wrappers.FileWrapper;

@Component
public class UserAvatarService extends FileWrapper {

    public String save(int id, MultipartFile avatar) {
        String fileName = "avatar_" + id;
        String url = "/img/users/" + id;
        return writeFile(url, fileName, avatar);
    }
}
