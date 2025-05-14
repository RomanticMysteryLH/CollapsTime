package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.model.FileUploadResult;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.service.AdminSongService;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.FileHandleUtils;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Controller
@RequestMapping("/admin/song")
public class AdminSongController {
    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\songPic\\");
                registry.addResourceHandler("/song/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\song\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/songPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/songPic/");
                registry.addResourceHandler("/song/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/song/");
            }
        }
    }


    @Autowired
    private AdminSongService adminSongService;
    @Autowired
    private SongService songService;

    /**
     * 更新歌曲数据
     */
    @PostMapping("/updateSong")
    @ResponseBody
    public String updateSong(Song song) throws IOException {
        int i = adminSongService.updateSong(song);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 实现歌曲分页
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/songPage")
    @ResponseBody
    public PageVo<Song> queryAllSong(Integer current, Integer size) {
        return songService.songPage(current,size);
    }


    /**
     * 上传mp3文件
     * @param file
     * @return
     */
    @PostMapping("/songUpload")
    @ResponseBody
    public FileUploadResult songUpload(MultipartFile file){
        return FileHandleUtils.mp3Upload(file);
    }

    /**
     * 上传歌曲封面
     * @param file
     * @return
     */
    @PostMapping("/songPicUpload")
    @ResponseBody
    public FileUploadResult songPicUpload(String file){
        String savePath ="songPic";
        return FileHandleUtils.filehandle(file,savePath);
    }

    /**
     * 删除数据库覆盖的歌曲文件
     * @param id
     * @return
     */
    @PostMapping("/songFileDelete")
    @ResponseBody
    public String songFileDelete(String id,String select){
        String picfilePath = Cons.RESOURCE_WIN_PATH+adminSongService.querySong(id).getPicture();
        String songPath = Cons.RESOURCE_WIN_PATH+adminSongService.querySong(id).getUrl();
        String picResult = null;
        String songResult = null;
        if(select.equals("both")){
            picResult=FileHandleUtils.deleteFile(picfilePath);
            songResult = FileHandleUtils.deleteFile(songPath);
        }else if(select.equals("pic")){
            picResult=FileHandleUtils.deleteFile(picfilePath);
        }else if(select.equals("song")){
            songResult = FileHandleUtils.deleteFile(songPath);
        }
        return "图片"+picResult+"\t歌曲"+songResult;
    }

    /**
     * 删除没保存的时候上传的上一个图片
     * @param picfilePath
     * @param songPath
     * @return
     */
    @PostMapping("/songProFileDelete")
    @ResponseBody
    public String songProFileDelete(String picfilePath,String songPath){
        String picResult=FileHandleUtils.deleteFile(Cons.RESOURCE_WIN_PATH+picfilePath);
        String songResult = FileHandleUtils.deleteFile(Cons.RESOURCE_WIN_PATH+songPath);
        return "图片"+picResult+"\t歌曲"+songResult;
    }


    /**
     * 上传歌词
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/songLrcUpload")
    @ResponseBody
    public Song songLrcUpload(MultipartFile file) throws IOException {
        Song song = new Song();
        //起手转成字符流
        if (file.isEmpty())
        {
            return song;
        }
        InputStream is = file.getInputStream();
        InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
        BufferedReader br = new BufferedReader(isReader);
        //循环逐行读取
        String lrc = null;
        String s=null;
        while ((s=br.readLine())!=null) {
            lrc+=s+"\n";//那这必须得用字符串存着拼接，不然就等着看不连贯的歌词吧
        }
        song.setLyric(lrc);
        //关闭流，讲究
        br.close();

        return song;
    }

    /**
     * 添加歌曲
     */
    @PostMapping("/addSong")
    @ResponseBody
    public String addSong(Song song) throws IOException {
        int i = adminSongService.addSong(song);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }

    /**
     * 获取歌手列表
     * @param query
     * @return
     */
    @GetMapping("/getSinger")
    @ResponseBody
    public List<Song> getSinger(String query){
        Song song = new Song();
        return adminSongService.getSinger(song);
    }

    /**
     * 删除歌曲
     * @param id
     * @return
     * @throws IOException
     */
    @PostMapping("/deleteSong")
    @ResponseBody
    public String deleteSong(String id) throws IOException{
        System.out.println(id);
        String msg = songFileDelete(id,"both");
        int i = adminSongService.songDelete(id);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }
}
