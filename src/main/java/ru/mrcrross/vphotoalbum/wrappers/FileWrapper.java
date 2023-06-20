package ru.mrcrross.vphotoalbum.wrappers;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileWrapper {

    private ArrayList<String> extensions;
    private final Path root = Paths.get("src/main/resources/static");

    public void setExtensions(ArrayList<String> extensions)
    {
        this.extensions = extensions;
    }
    public String writeFile(String url, String fileName, MultipartFile multipartFile)
    {
        Path dir = Paths.get(root + url);
        try {
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String extension = getExtension(multipartFile, false);
        String urlNewFile = url + "/" + fileName + extension;
        Path newFile = Paths.get(root + urlNewFile);
        try {
            Files.deleteIfExists(newFile);
            Files.copy(multipartFile.getInputStream(), newFile);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось сохранить файл. Ошибка: " + e.getMessage());
        }
        return urlNewFile;
    }

    private void setDefaultExtensions() {
        extensions = new ArrayList<String>();
        extensions.add("jpg");
        extensions.add("jpeg");
        extensions.add("png");
        extensions.add("PNG");
        extensions.add("gif");
        extensions.add("GIF");
        extensions.add("JPG");
        extensions.add("JPEG");
        extensions.add("jpe");
        extensions.add("tiff");
        extensions.add("TIFF");
    }

    public String getExtension(MultipartFile file, boolean check)
    {
        String fileName = file.getOriginalFilename();
        assert fileName != null;
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        if (check) {
            index = index + 1;
        }
        return fileName.substring(index);
    }

    public String validateExtension(MultipartFile file)
    {
        if (extensions == null) {
            this.setDefaultExtensions();
        }
        String extension = getExtension(file, true);
        if (!extension.equals("") && extensions.contains(extension)) {
            return "";
        }
        return "Неверный тип файла";
    }
}
