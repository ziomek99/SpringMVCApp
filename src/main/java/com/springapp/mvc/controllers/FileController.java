package com.springapp.mvc.controllers;

import com.springapp.mvc.DAO.FilesService;
import com.springapp.mvc.FileRepository;
import com.springapp.mvc.classes.dbFile;
import com.springapp.mvc.classes.multipartFile;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;


@Controller
@RequestMapping(value = "/files")
public class FileController {

    private FilesService filesService = new FilesService();
    dbFile fileFromDatabase = new dbFile();
    @Autowired
    private FileRepository fileRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String listUsers(ModelMap model) {
        model.addAttribute("file", new multipartFile());
        model.addAttribute("files", fileRepository.findAll());
        return "files";

    }

    @RequestMapping(value = "/add", method=RequestMethod.POST)
    public String uploadFile(@ModelAttribute("file") multipartFile mfile, BindingResult result){
        Map<Object, Object> model = new HashMap<Object, Object>();
        if (mfile.getFile().isEmpty())
        {
            System.out.println("Error. No file selected.");
            return "redirect:/files";
        }
        for (dbFile file : fileRepository.findAll()) {
            if (mfile.getFile().getOriginalFilename().equals(file.getFileName())) {
                System.out.println("Error. File already exists.");
                return "redirect:/files";
            }
        }
        if(!result.hasErrors()){
            //System.out.println(mfile.getFile().getOriginalFilename());
            fileFromDatabase = this.filesService.save(mfile);
            fileRepository.save(fileFromDatabase);
            return "redirect:/files";

        } else{
            System.out.println("Error:" + result.toString());
            return "redirect:/files";
        }
    }

    @RequestMapping("/delete/{fileId}")
    public String deleteUser(@PathVariable("fileId") Long fileId) {
        fileFromDatabase = fileRepository.findOne(fileId);

        filesService.delete(fileFromDatabase.getFilePath());
        fileRepository.delete(fileFromDatabase);
        return "redirect:/files";
    }

    @RequestMapping(value = "/download/{fileId}", method = RequestMethod.POST)
    public String getFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) throws Exception{
        OutputStream outputStream = response.getOutputStream();

        try{
            fileFromDatabase = fileRepository.findOne(fileId);
            File fileToDownload = new File(fileFromDatabase.getFilePath());
            InputStream inputStream = new FileInputStream(fileToDownload);
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", fileFromDatabase.getFileName());
            response.setHeader(headerKey, headerValue);
            IOUtils.copy(inputStream, outputStream);

            inputStream.close();

        }catch(Exception e){
            System.out.println("Download fail. " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            response.flushBuffer();
            outputStream.close();
        }
        return "redirect:/files";
    }
}
/*
@Controller
@RequestMapping(value="/FileUploadForm")
public class FileController implements HandlerExceptionResolver {
    private FilesService filesService = new FilesService();

    @RequestMapping(method= RequestMethod.GET)
    public String showForm(ModelMap model){
        myFile myfile = new myFile();
        model.addAttribute("MYFILE", myfile);
        return "FileUploadForm";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String uploadFile(@ModelAttribute(value="MYFILE") myFile myfile, BindingResult result){
        Map<Object, Object> model = new HashMap<Object, Object>();
        if(!result.hasErrors()){
            File dir = new File("user_files" + File.separator + "userId");
            if(!dir.exists())
                dir.mkdirs();
            myfile.setFileName(myfile.getFile().getOriginalFilename());
            myfile.setFilePath(dir.getAbsolutePath()+ File.separator + myfile.getFileName());
            this.filesService.save(myfile);
            //System.out.println(myfile.getFilePath());
            return "FileUploadForm";

        } else{
            return "FileUploadForm";
        }
    }

    @RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
    public String getFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception{
        try{
            File fileToDownload = this.filesService.find(fileName);

            // set headers for the response
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"",
                    fileName);
            //response.setContentType("application/force-download");
            response.setHeader(headerKey, headerValue);
            InputStream inputStream = new FileInputStream(fileToDownload);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

        }catch(Exception e){
            System.out.println("not done" + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/delete/{fileName:.+}", method = RequestMethod.GET)
    public String delFile(@PathVariable("fileName") String fileName) throws Exception{
        try{
            this.filesService.delete(fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest arg0,
                                         HttpServletResponse arg1, Object arg2, Exception exception) {
        Map<Object, Object> model = new HashMap<Object, Object>();
        if (exception instanceof MaxUploadSizeExceededException){
            model.put("errors", "File size should be less then "+
                    ((MaxUploadSizeExceededException)exception).getMaxUploadSize()+" byte.");
        } else{
            model.put("errors", "Unexpected error: " + exception.getMessage());
        }
        model.put("MYFILE", new myFile());
        return new ModelAndView("/FileUploadForm", (Map) model);
    }
}

*/