package henu.javaweb.collapstime;

import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SingerService;
import henu.javaweb.collapstime.service.SongListService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.MusicUtils;
import it.sauronsoftware.jave.EncoderException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

@SpringBootTest
class CollapstimeApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private SongListService songListService;
    @Autowired
    private SingerService singerService;
    @Autowired
    private SongListMapper songListMapper;
    @Test
    void test1(){
        PageVo<SongList> bgm = songListService.songListPageByStyle(1, 10, "BGM");
        System.out.println(bgm);
    }

    @Test
    public void test2() {
//        LinkedList<Singer> z = singerMapper.querySingerByInitial("Z");
        System.out.println("success");
    }
    @Test
    public void test3() {
        PageVo<Singer> z = singerService.singerPageByInitial(1, 10, "Z");
        System.out.println("success");
    }

    @Test
    public void test4() {
        PageVo<Singer> singerPageVo = singerService.singerPageByCategory(1, 10, 2);
        System.out.println("success");
    }

    @Test
    public void test5() {
        LinkedList<SongListComment> songListComment = songListMapper.getSongListComment(1);
        LinkedList<User> songListCommentUser = songListMapper.getSongListCommentUser(1);
        SongListDetail songListDetail = songListService.getSongListDetail(1,1);
        System.out.println("success");
    }

    @Test
    public void test7() throws EncoderException {
        String path = Cons.RESOURCE_WIN_PATH+"/song/G.E.M.邓紫棋-龙卷风.mp3";
        System.out.println(path);
        String musicDuration = MusicUtils.getMusicDuration(path);
        System.out.println(musicDuration);
    }
    @Test
    public void test8() {
        LinkedList<Integer> songListOfUserCollect = songListMapper.getSongListOfUserCollect(1);
        System.out.println(songListOfUserCollect);
    }
    @Test
    public void test9(){
        String lyc = "[00:00.00] 作曲 : 周杰伦\n" +
                "[00:01.00] 作词 : 方文山\n" +
                "[00:32.840] 塞纳河畔 左岸的咖啡\n" +
                "[00:35.438] 我手一杯 品尝你的美\n" +
                "[00:38.655] 留下唇印的嘴";
        String[] split = lyc.split("\n");
        String a ="";
        for (String s : split){
            a+=s.split("]")[1];
            a+="\n";
        }
        System.out.println(a);
    }
    @Test
    public void test10() {
        String s = songListMapper.querySongListAllStyle();
        System.out.println(s);
        HashSet<String> allStyle = new HashSet<>();
        String[] split = s.split(",");
        for (int i = 0; i < split.length; i++){
            if(split[i].contains("-")){
                String[] split1 = split[i].split("-");
                for (int j = 0; j < split1.length; j++){
                    allStyle.add(split1[j]);
                }
            }else {
                allStyle.add(split[i]);
            }
        }
        System.out.println("success");
    }
}
