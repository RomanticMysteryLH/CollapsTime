package henu.javaweb.collapstime.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequestMapping("/admin/home")
public class AdminHomeController {
    @Autowired
    AdminUserController adminUserController;
    @Autowired
    SingerController singerController;
    @Autowired
    SongController songController;
    @Autowired
    SongListController songListController;

    /**
     * 获取统计数据
     * @param
     */
    @GetMapping("/getCount")
    @ResponseBody
    public HashMap<String, Long> getCount(){
        Long allUser = adminUserController.queryUserList(1,10).getTotal();
        Long allSinger = singerController.queryAllSinger(1,10,null,null).getTotal();
        Long allSong = songController.queryAllSong(1,10).getTotal();
        Long allSongList = songListController.queryAllSongList(1,10).getTotal();
        HashMap<String, Long> result = new HashMap<>();
        result.put("Usernum",allUser);
        result.put("Singernum",allSinger);
        result.put("Songnum",allSong);
        result.put("SongListnum",allSongList);
        return result;
    }
}
