package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.AdminUserService;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.service.UserService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.JWTUtil;
import henu.javaweb.collapstime.utils.VerifyCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/userPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\userPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/userPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/userPic/");
            }
        }
    }

    @Autowired
    private UserService userService;
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 后台返回用户列表
     * @return
     * @throws IOException
     */
    @GetMapping("/getlist")
    @ResponseBody
    public PageVo<User> queryUserList(Integer current, Integer size) {
        return adminUserService.userPage(current,size);
    }

    /**
     * 更新用户数据
     */
    @PostMapping("/updatelist")
    @ResponseBody
    public String updateUser(User user) throws IOException{
        int i = userService.updateUser(user);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }


    /**
     * 上传头像
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    @ResponseBody
    public FileUploadResult fileUpload(MultipartFile file) {
        FileUploadResult result = new FileUploadResult();
        if (file.isEmpty()) {
            result.setMsg("上传文件为空");
            return result;
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        if(".png".equals(suffixName) || ".jpg".equals(suffixName) || ".jpeg".equals(suffixName)){
            String filePath = ""; // 上传后的路径
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")){
                filePath = Cons.RESOURCE_WIN_PATH;
            }else {
                filePath = Cons.RESOURCE_MAC_PATH;
            }
            filePath+="/img/userPic/";
            fileName = UUID.randomUUID() + suffixName; // 新文件名
            File dest = new File(filePath + fileName);
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result.setMsg("上传成功");
            result.setFilePath("/img/userPic/"+fileName);
            return result;

        }else {
            result.setMsg("只支持上传png,jpeg,jpg格式的图像");
            return result;
        }
    }

    /**
     * 删除用户
     * @param user
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(User user) throws IOException{
        int i = userService.updateUser(user);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }


//    @GetMapping("/getUserSong")
//    @ResponseBody
//    public LinkedList<User> getUserSong(String id) throws IOException{
//        return songService.songPage();
//    }
}
