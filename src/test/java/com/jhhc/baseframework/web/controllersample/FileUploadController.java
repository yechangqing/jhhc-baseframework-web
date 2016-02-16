package com.jhhc.baseframework.web.controllersample;

import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 上传文件
 *
 * @author yecq
 */
//@Controller
public class FileUploadController {

    @RequestMapping("file.go")
    @ResponseBody
    public String doFileUpload(@RequestParam("name") String name, @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                System.out.println("接受了" + bytes.length);
            } catch (IOException ex) {
                // ...
            }
        }
        return "收到name " + name + "，文件 " + file.getOriginalFilename();
    }
}
