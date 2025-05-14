package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.mapper.UserMapper;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.SongList;
import henu.javaweb.collapstime.service.SongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

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
    @Autowired
    SingerMapper singerMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SongListMapper songListMapper;
    @Autowired
    SongMapper songMapper;
    @Autowired
    SongListService songListService;

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

    /**
     * 获取统计数据
     * @return
     */
    @GetMapping("/queryStasticOfHome")
    @ResponseBody
    public HashMap<String,Object> queryStasticOfHome(){
        HashMap<String, Object> map = new HashMap<>();
        //男歌手数量
        Integer maleSingerCount = singerMapper.getSingerCountOfMale();
        map.put("maleSingerCount",maleSingerCount);
        //女歌手数量
        Integer femaleSingerCount = singerMapper.getSingerCountOfFemale();
        map.put("femaleSingerCount",femaleSingerCount);
        //乐队数量
        Integer bandCount = singerMapper.getBandCount();
        map.put("bandCount",bandCount);
        //男性会员数量
        Integer maleUserCount = userMapper.getUserCountOfmale();
        map.put("maleUserCount",maleUserCount);
        //女性会员数量
        Integer femaleUserCount = userMapper.getUserCountOfFemale();
        map.put("femaleUserCount",femaleUserCount);
        //歌单不同类型数量
        //获取所有歌单类型
        HashMap<String, Integer> songListCountByStyle = new HashMap<>();
        List<String> list = new ArrayList<String>(songListService.querySongListAllStyle());
        for(int i = 0; i < list.size(); i++){
            songListCountByStyle.put(list.get(i),songListMapper.getCountByStyle(list.get(i)));
        }
        map.put("songListCountByStyle",songListCountByStyle);
        //热门歌手前二十  按收藏量
        HashMap<String, Integer> collectSingerCount = new HashMap<>();
        //前二十个歌手
        LinkedList<Singer> singers = singerMapper.querySingerTop20(singerMapper.querySingerTop20OfId());
        for(int i = 0; i < singers.size(); i++){
            collectSingerCount.put(singers.get(i).getName(),singerMapper.getCollectSingerCount(singers.get(i).getId()));
        }
        map.put("collectSingerCount",collectSingerCount);
        //热门歌单前三十  收藏量
        HashMap<String,Integer> collectSongListCount = new HashMap<>();
        //前三十个歌单
        LinkedList<SongList> songLists = songListMapper.querySongListTop30(songListMapper.querySongListTop30OfId());
        for (int i = 0; i < songLists.size(); i++) {
            collectSongListCount.put(songLists.get(i).getTitle(),songListMapper.getCollectSongListCount(songLists.get(i).getId()));
        }
        map.put("collectSongListCount",collectSongListCount);
        //热门歌曲前五十  播放量
        HashMap<String, Integer> playSongCount = new HashMap<>();
        LinkedList<Song> songs = songMapper.querySongTop50(songMapper.querySongTop50OfId());
        for (int i = 0; i < songs.size(); i++) {
            playSongCount.put(songs.get(i).getName().split("-")[1],songMapper.getPlayCountById(songs.get(i).getId()));
        }
        map.put("playSongCount",playSongCount);
        return map;
    }

}
