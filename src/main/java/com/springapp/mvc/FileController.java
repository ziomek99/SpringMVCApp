package com.springapp.mvc;

import com.google.common.io.Files;
import com.springapp.mvc.objects.myFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value="/FileUploadForm")
public class FileController implements HandlerExceptionResolver {

    @RequestMapping(method= RequestMethod.GET)
    public String showForm(ModelMap model){
        myFile myfile = new myFile();
        model.addAttribute("MYFILE", myfile);
        model.addAttribute("msg", "");
        return "FileUploadForm";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String processForm(@ModelAttribute(value="MYFILE") myFile myfile, @ModelAttribute(value = "msg") String Msg,BindingResult result){
        if(!result.hasErrors()){
            FileOutputStream outputStream = null;
            myfile.setFilePath(System.getProperty("java.io.tmpdir") + "/" + myfile.getFile().getOriginalFilename());
            try {
                outputStream = new FileOutputStream(new File(myfile.getFilePath()));
                outputStream.write(myfile.getFile().getFileItem().get());
                outputStream.close();
            } catch (Exception e) {
                System.out.println("Error while saving file" + myfile.getFile().getOriginalFilename());
                return "FileUploadForm";
            }
            System.out.println(System.getProperty("java.io.tmpdir") + "/" + myfile.getFile().getOriginalFilename());
            return "FileUploadForm";
        }else{
            System.out.println("Error " + myfile.getFile().getOriginalFilename());
            return "FileUploadForm";
        }
    }

    @RequestMapping(value = "/download/{file:.+}", method = RequestMethod.GET)
    @ResponseBody
    public byte[] getFile(@PathVariable final String file) throws IOException {
            System.out.println(file);
            return Files.toByteArray(new File(System.getProperty("java.io.tmpdir") + "/" + file));
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