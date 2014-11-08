package com.springapp.mvc.objects;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Created by Adrian on 06-11-2014.
 */
public class myFile {

    CommonsMultipartFile file;
    String fileName;
    String filePath;

    public CommonsMultipartFile getFile() {
        return file;
    }

    public void setFile(CommonsMultipartFile file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
