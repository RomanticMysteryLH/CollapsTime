package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.model.FileUploadResult;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.service.AdminSingerService;
import henu.javaweb.collapstime.service.SingerService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.FileHandleUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Controller
@RequestMapping("/admin/singer")
public class AdminSingerController {
    @Autowired
    private AdminSingerService adminSingerService;

    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\singerPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/singerPic/");
            }
        }
    }

    /**
     * 更新歌曲数据
     */
    @PostMapping("/updateSinger")
    @ResponseBody
    public String updateSinger(Singer singer) throws IOException {
        if(singer.getBirth().contains("T")){
            String birth = singer.getBirth().split("T")[0]+" "+singer.getBirth().split("T")[1].split("Z")[0];
            singer.setBirth(birth);
        }
        int i = adminSingerService.updateSinger(singer);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 上传歌曲封面
     * @param file
     * @return
     */
    @PostMapping("/singerPicUpload")
    @ResponseBody
    public FileUploadResult singerPicUpload(String file){
        String savePath ="singerPic";
        return FileHandleUtils.filehandle(file,savePath);
    }

    /**
     * 删除多出来的图片文件
     * @param id
     * @param picfilePath
     * @return
     */
    @PostMapping("/singerFileDelete")
    @ResponseBody
    public String singerFileDelete(String id,String picfilePath){
        String picPath = null;
        if(!id.isEmpty()){
            picPath = Cons.RESOURCE_WIN_PATH+adminSingerService.querySinger(id).getPicture();
        }else{
            picPath = Cons.RESOURCE_WIN_PATH+picfilePath;
        }
        String picResult = null;
        picResult=FileHandleUtils.deleteFile(picPath);
        return "图片"+picResult;
    }

    /**
     * 添加歌手
     */
    @PostMapping("/addSinger")
    @ResponseBody
    public String addSinger(Singer singer) throws IOException {
        if(singer.getBirth().contains("T")){
            String birth = singer.getBirth().split("T")[0]+" "+singer.getBirth().split("T")[1].split("Z")[0];
            singer.setBirth(birth);
        }
        int i = adminSingerService.addSinger(singer);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 删除歌手
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteSinger")
    @ResponseBody
    public String deleteSinger(String id) throws IOException{
        String msg = singerFileDelete(id,"");
        int i = adminSingerService.singerDelete(id);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }
}
