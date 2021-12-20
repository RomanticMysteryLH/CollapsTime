package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.utils.Cons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    private SongService songService;
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
}
