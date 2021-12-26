package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.model.FileUploadResult;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.SongList;
import henu.javaweb.collapstime.service.AdminSongListService;
import henu.javaweb.collapstime.service.AdminSongService;
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
@RequestMapping("/admin/songList")
public class AdminSongListController {
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

    @Autowired
    private AdminSongListService adminSongListService;

    /**
     * 更新歌单数据
     */
    @PostMapping("/updateSongList")
    @ResponseBody
    public String updateSongList(SongList songList) throws IOException {
        if(songList.getCreateTime().contains("T")){
            String birth = songList.getCreateTime().split("T")[0]+" "+songList.getCreateTime().split("T")[1].split("Z")[0];
            songList.setCreateTime(birth);
        }
        int i = adminSongListService.updateSongList(songList);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 上传歌单封面
     * @param file
     * @return
     */
    @PostMapping("/songListPicUpload")
    @ResponseBody
    public FileUploadResult songListPicUpload(String file){
        String savePath ="songListPic";
        return FileHandleUtils.filehandle(file,savePath);
    }

    /**
     * 删除多出来的图片文件
     * @param id
     * @param picfilePath
     * @return
     */
    @PostMapping("/songListFileDelete")
    @ResponseBody
    public String songListFileDelete(String id,String picfilePath){
        String picPath = null;
        if(!id.isEmpty()){
            picPath = Cons.RESOURCE_WIN_PATH+adminSongListService.querySongList(id).getPicture();
        }else{
            picPath = Cons.RESOURCE_WIN_PATH+picfilePath;
        }
        String picResult = null;
        picResult=FileHandleUtils.deleteFile(picPath);
        return "图片"+picResult;
    }

    /**
     * 添加歌单
     */
    @PostMapping("/addSongList")
    @ResponseBody
    public String addSongList(SongList songList) throws IOException {
        if(songList.getCreateTime().contains("T")){
            String birth = songList.getCreateTime().split("T")[0]+" "+songList.getCreateTime().split("T")[1].split("Z")[0];
            songList.setCreateTime(birth);
        }
        int i = adminSongListService.addSongList(songList);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 删除歌单
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteSongList")
    @ResponseBody
    public String deleteSongList(String id) throws IOException{
        String msg = songListFileDelete(id,"");
        int i = adminSongListService.songListDelete(id);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 删除歌单评论
     * @param commentId
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteSongListComment")
    @ResponseBody
    public String deleteSongListComment(String commentId) throws IOException{
        int i = adminSongListService.songListCommentDelete(commentId);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }
    /**
     * 删除歌单的歌曲
     * @param
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteSongListSong")
    @ResponseBody
    public String deleteSongListSong(String songListId,String songIdstr) throws IOException{
        int n = 0;
        if(songIdstr.contains(",")){//如果传了两个以上就分割
            String[] strarr = songIdstr.split(",");
            for(int i = 0;i<strarr.length;i++){
                n = adminSongListService.songListSongDelete(songListId,strarr[i]);
            }
        }else{//如果只传一个就直接作为id
            n = adminSongListService.songListSongDelete(songListId,songIdstr);
        }
        if(n > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

}
