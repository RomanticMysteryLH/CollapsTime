package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.model.FileUploadResult;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.User;
import henu.javaweb.collapstime.service.AdminUserService;
import henu.javaweb.collapstime.service.UserService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.FileHandleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

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
    @PostMapping("/userAvatarUpload")
    @ResponseBody
    public FileUploadResult userAvatarUpload(String file){
        String savePath ="userPic";
        return FileHandleUtils.filehandle(file,savePath);
    }
    /**
     * 删除数据库覆盖的头像图片
     * @param id
     * @return
     */
    @PostMapping("/userFileDelete")
    @ResponseBody
    public String userFileDelete(String id){
        String picfilePath = Cons.RESOURCE_WIN_PATH+adminUserService.queryUser(id).getAvator();
        String picResult = null;
        picResult=FileHandleUtils.deleteFile(picfilePath);

        return "图片"+picResult;
    }

    /**
     * 删除没保存的时候上传的上一个图片
     * @param picfilePath
     * @return
     */
    @PostMapping("/userProFileDelete")
    @ResponseBody
    public String userProFileDelete(String picfilePath){
        String picResult=FileHandleUtils.deleteFile(Cons.RESOURCE_WIN_PATH+picfilePath);
        return "图片"+picResult;
    }

    /**
     * 删除用户
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(String id) throws IOException{
        String msg = userFileDelete(id);
        int i = adminUserService.userDelete(id);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }
}
