package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.utils.Cons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Controller
@RequestMapping("/song")
public class SongController {


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
    @Autowired
    private SongMapper songMapper;

    /**
     * 实现歌曲分页
     * @param current
     * @param size
     * @return
     */
//    @GetMapping("/songPage")
//    @ResponseBody
//    public PageVo<Song> queryAllSong(Integer current, Integer size) {
//        return songService.songPage(current,size);
//    }

    /**
     * 获取歌曲详情页的所有信息
     * @param songId
     * @param userId
     * @return
     */
    @GetMapping("/getSongDetail")
    @ResponseBody
    public SongDetail getSongDetail(Integer songId,Integer userId){
        return songService.getSongDetail(songId, userId);
    }

    /**
     * 添加歌曲评论
     * @param comment
     * @return
     */
    @PostMapping("/addSongComment")
    @ResponseBody
    public CommentResult addSongComment(Comment comment){
        CommentResult commentResult = new CommentResult();
        if(songService.addSongComment(comment)>0){
            LinkedList<CommentUser> commentUserList = new LinkedList<CommentUser>();
            LinkedList<SongComment> songComment = songMapper.getSongComment(comment.getSongId());
            LinkedList<User> songOfCommentUser = songMapper.getSongOfCommentUser(comment.getSongId());
            for(int i = 0;i < songComment.size(); i++){
                for(int j = 0;j < songOfCommentUser.size(); j++){
                    if(songComment.get(i).getUserId().equals(songOfCommentUser.get(j).getId())){
                        CommentUser commentUser = new CommentUser();
                        commentUser.setId(songOfCommentUser.get(j).getId());
                        commentUser.setUsername(songOfCommentUser.get(j).getUsername());
                        commentUser.setAvator(songOfCommentUser.get(j).getAvator());
                        commentUser.setCommentId(songComment.get(i).getId());
                        commentUser.setContent(songComment.get(i).getContent());
                        commentUser.setCreateTime(songComment.get(i).getCreateTime());
                        commentUser.setCommentUp(songComment.get(i).getUp());
                        commentUserList.add(commentUser);
                        break;
                    }else {
                        continue;
                    }
                }
            }
            commentResult.setCommentUser(commentUserList);
            commentResult.setStatus("success");
            commentResult.setMsg("添加歌曲评论成功");
        }else {
            commentResult.setStatus("fail");
            commentResult.setMsg("添加歌曲评论失败");
        }
        return commentResult;
    }

    /**
     * 收藏歌曲
     * @param collect
     * @return
     */
    @PostMapping("/collectSong")
    @ResponseBody
    public CollectResult collectSong(Collect collect){
        CollectResult collectResult = new CollectResult();
        if(collect.getFlag() == 1){
            return dismissCollectSong(collect);
        }
        if(songService.collectSong(collect)>0){
            Integer songCollectCount = songMapper.getSongCollectCount(collect.getSongId());
            collectResult.setCollectCount(songCollectCount);
            collectResult.setStatus("success");
            collectResult.setMsg("收藏歌曲成功");
        }else {
            collectResult.setStatus("fail");
            collectResult.setMsg("收藏歌曲失败");
        }
        return collectResult;
    }

    /**
     * 根据歌曲id获取歌词并且增加歌曲播放量
     * 因为我们都是在点击播放的时候发送获取歌词请求所以用此统计歌曲播放量
     * @param songId
     * @return
     */
    @PostMapping("/getLyric")
    @ResponseBody
    public Map<String, String> getLyric(Integer songId,Integer userId){
        String lyric = songMapper.getLyricBySongId(songId);
        Map<String, String> result = new HashMap<>();
        result.put("lyric",lyric);
        LinkedList<Integer> songIds = songMapper.queryAllSongIdOfPlay();
        boolean flag = false;
        for(int i = 0; i < songIds.size(); i++) {
            if (songId.equals(songIds.get(i))) {
                flag = true;
                //更新歌曲播放量  +1
                songMapper.updatePlayCount(songId);
                break;
            }
        }
        if(!flag){
            //添加播放记录 并初始化播放量为1
            songMapper.addPlayCount(songId,userId);
        }
        return result;
    }

    @PostMapping("/dismissCollectSong")
    @ResponseBody
    public CollectResult dismissCollectSong(Collect collect){
        CollectResult collectResult = new CollectResult();
        if(songMapper.dismissCollectSong(collect) > 0){
            Integer songCollectCount = songMapper.getSongCollectCount(collect.getSongId());
            collectResult.setCollectCount(songCollectCount);
            collectResult.setStatus("success");
            collectResult.setMsg("取消收藏成功");
        }else {
            collectResult.setStatus("fail");
            collectResult.setMsg("出错了");
        }
        return collectResult;
    }

    /**
     * 点赞歌曲评论
     * @param commentId
     * @return
     */
    @PostMapping("/upSongComment")
    @ResponseBody
    public UpCommentResult upSongListComment(Integer commentId){
        UpCommentResult upCommentResult = new UpCommentResult();
        if(songMapper.upSongCommment(commentId)>0){
            int songCommentUpCount = songMapper.getSongCommentUpCount(commentId);
            upCommentResult.setUpCount(songCommentUpCount);
            upCommentResult.setStatus("success");
            upCommentResult.setMsg("点赞成功");
            return upCommentResult;
        }else {
            upCommentResult.setStatus("fail");
            upCommentResult.setMsg("出错了");
            return upCommentResult;
        }
    }

    /**
     * 获取热门歌曲前五十首
     * @return
     */
    @PostMapping("/getHotSong")
    @ResponseBody
    public PageVo<SongShowInList> getHotSong(Integer current,Integer size,Integer userId){
        return songService.getHotSongTop50(current, size, userId);
    }

}


