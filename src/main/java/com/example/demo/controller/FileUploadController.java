package com.example.demo.controller;

import com.example.demo.common.IMoocJSONResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileUploadController {

    // Content-Type   application/x-www-form-urlencoded

    @PostMapping("/uploadFace")
    public IMoocJSONResult uploadFace(String productId, @RequestParam("file") MultipartFile[] files) throws IOException {

        System.out.println("==="+productId);

        System.out.println("----"+files.toString());

        //文件保存路径
        String fileSpace = "D:/imooc_dev_vedios";

        //用户保存的路径（相对路径）
        String uploadPathDB = "/"+productId+"/face";

        FileOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null || files.length >0){

                //获取file的名字
                String filename = files[0].getOriginalFilename();


                if (StringUtils.isNotBlank(filename)){
                    //文件最终上传的保存路径
                    String path = fileSpace+uploadPathDB+"/"+filename;
                    //设置数据库保存的路径
                    uploadPathDB += ("/"+filename);

                    File outFile = new File(path);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()){
                        //创建文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    outputStream = new FileOutputStream(outFile);
                    inputStream =  files[0].getInputStream();

                    //工具类进行拷贝
                    IOUtils.copy(inputStream,outputStream);

                }

            }else {
                return IMoocJSONResult.errorMsg("上传文件不能为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream != null){
                outputStream.flush();
                outputStream.close();
            }
        }

        //进行路径的保存
        /*Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDB);
        this.userService.updateUserInfo(user);*/

        return IMoocJSONResult.ok(uploadPathDB);
    }
}
