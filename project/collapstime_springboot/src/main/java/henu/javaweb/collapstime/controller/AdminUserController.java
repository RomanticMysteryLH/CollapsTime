package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.model.FileUploadResult;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.User;
import henu.javaweb.collapstime.service.*;
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
import java.util.HashMap;

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
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongService songService;
    @Autowired
    private SongListService songListService;

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
     * 删除多出来的图片文件
     * @param id
     * @param picfilePath
     * @return
     */
    @PostMapping("/userFileDelete")
    @ResponseBody
    public String userFileDelete(String id,String picfilePath){
        String picPath = null;
        if(!id.isEmpty()){
            picPath = Cons.RESOURCE_WIN_PATH+adminUserService.queryUser(id).getAvator();
        }else{
            picPath = Cons.RESOURCE_WIN_PATH+picfilePath;
        }
        String picResult = null;
        picResult=FileHandleUtils.deleteFile(picPath);
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
        String msg = userFileDelete(id,"");
        int i = adminUserService.userDelete(id);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 删除用户收藏的歌手、歌曲、歌单
     * @param userId
     * @param idStr
     * @param flag
     * @return
     */
    @PostMapping("/deleteCollect")
    @ResponseBody
    public HashMap<String, String> deleteCollect(Integer userId,String idStr,String flag){
        int n = 0;
        if(idStr.contains(",")){//如果传了两个以上就分割
            String[] strarr = idStr.split(",");
            if(flag.equals("singer")){
                for(int i = 0;i<strarr.length;i++){
                    n = singerService.deleteUserCollectSinger(Integer.parseInt(strarr[i]), userId);
                }
            }else if (flag.equals("song")){
                for(int i = 0;i<strarr.length;i++){
                    n = songService.deleteCollectSong(Integer.parseInt(strarr[i]), userId);
                }
            }else{
                for(int i = 0;i<strarr.length;i++){
                    n = songListService.deleteUserCollectSongList(Integer.parseInt(strarr[i]), userId);
                }
            }

        }else{//如果只传一个就直接作为id
            if(flag.equals("singer")){
                n = singerService.deleteUserCollectSinger(Integer.parseInt(idStr), userId);
            }else if (flag.equals("song")){
                n = songService.deleteCollectSong(Integer.parseInt(idStr), userId);
            }else{
                n = songListService.deleteUserCollectSongList(Integer.parseInt(idStr), userId);
            }
        }
        HashMap<String, String> result = new HashMap<>();
        if(n > 0){
            result.put("state","success");
            result.put("msg","取消收藏成功");
        }else {
            result.put("state","fail");
            result.put("msg","出错了");
        }
        return result;
    }
}
