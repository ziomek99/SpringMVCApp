package com.springapp.mvc;

        import java.io.BufferedOutputStream;
        import java.io.File;
        import java.io.FileOutputStream;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
        import org.springframework.web.bind.WebDataBinder;
        import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;
        import org.springframework.web.bind.annotation.RequestParam;
        import org.springframework.web.bind.annotation.ResponseBody;
        import org.springframework.web.multipart.MultipartFile;

/**
 * Created by Adrian on 04-11-2014.
 */
@Controller
public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);

    @RequestMapping(value = "/uploadPage", method = RequestMethod.POST)
    public String uploadPage() {
       return "upload";
    }

    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadFileHandler(@RequestParam("filename") String name, @RequestParam("file") MultipartFile file)
    {
        if (!file.isEmpty()) {
            try{
                byte[] bytes = file.getBytes();

                //Creating directory to store the file
                String rootPath = System.getProperty("adrian.home");
                File dir = new File(rootPath + File.separator + "tmpFiles");
                if (!dir.exists())
                    dir.mkdirs();

                //Create the file on server
                File serverFile = new File(dir.getAbsolutePath()+ File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();
                logger.info("Server File Location="+ serverFile.getAbsolutePath());

                return "redirect:/uploadFile";
            } catch (Exception e) {
                return "You failed to upload " +name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload it";
        }
    }
}
