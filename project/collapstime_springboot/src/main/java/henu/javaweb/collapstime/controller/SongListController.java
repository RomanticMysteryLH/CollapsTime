package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SongListService;
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

@Controller
@RequestMapping("/songList")
public class SongListController {


    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/songListPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\songListPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/songListPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/songListPic/");
            }
        }
    }


    @Autowired
    private SongListService songListService;
    @Autowired
    private SongListMapper songListMapper;


    /**
     * 实现歌单分页
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/songListPage")
    @ResponseBody
    public PageVo<SongList> queryAllSongList(Integer current, Integer size){
        return songListService.songListPage(current, size);
    }

    /**
     * 获取歌单所有的style
     * @return
     */
    @GetMapping("/querySongListAllStyle")
    @ResponseBody
    public HashSet<String> querySongListAllStyle(){
        return songListService.querySongListAllStyle();
    }

    /**
     * 实现根据style查询的歌单进行分页
     * @param current
     * @param size
     * @param style
     * @return
     */
    @GetMapping("/songListPageByStyle")
    @ResponseBody
    public  PageVo<SongList> queryAllSongListByStyle(Integer current, Integer size,String style){
        return songListService.songListPageByStyle(current, size, style);
    }

    /**
     * 获取歌单详情界面中的所有信息
     * @param songListId
     * @return
     */
    @GetMapping("/getsongListDetail")
    @ResponseBody
    public SongListDetail getsongListDetail(Integer songListId,Integer userId){
        return songListService.getSongListDetail(songListId,userId);
    }

    /**
     * 添加歌单评论
     * @param comment
     * @return
     */
    @PostMapping("/addSongListComment")
    @ResponseBody
    public CommentResult addSongListComment(Comment comment){
        CommentResult commentResult = new CommentResult();
        if(songListService.addSongListComment(comment)>0){
            LinkedList<SongListComment> songListComment = songListMapper.getSongListComment(comment.getSongListId());
            LinkedList<User> songListCommentUser = songListMapper.getSongListCommentUser(comment.getSongListId());
            LinkedList<CommentUser> commentUserList = new LinkedList<CommentUser>();
            for(int i = 0;i < songListComment.size(); i++){
                for(int j = 0;j < songListCommentUser.size(); j++){
                    if(songListComment.get(i).getUserId().equals(songListCommentUser.get(j).getId())){
                        CommentUser commentUser = new CommentUser();
                        commentUser.setId(songListCommentUser.get(j).getId());
                        commentUser.setUsername(songListCommentUser.get(j).getUsername());
                        commentUser.setAvator(songListCommentUser.get(j).getAvator());
                        commentUser.setCommentId(songListComment.get(i).getId());
                        commentUser.setContent(songListComment.get(i).getContent());
                        commentUser.setCreateTime(songListComment.get(i).getCreateTime());
                        commentUser.setCommentUp(songListComment.get(i).getUp());
                        commentUserList.add(commentUser);
                        break;
                    }else {
                        continue;
                    }
                }
            }
            commentResult.setCommentUser(commentUserList);
            commentResult.setStatus("success");
            commentResult.setMsg("评论歌单成功");
        }else {
            commentResult.setStatus("fail");
            commentResult.setMsg("评论歌单失败");
        }
        return commentResult;
    }

    /**
     * 收藏歌单
     * @param collect
     * @return
     */
    @PostMapping("/collectSongList")
    @ResponseBody
    public CollectResult collectSongList(Collect collect){
        CollectResult collectResult = new CollectResult();
        if(collect.getFlag() == 1){
            return dismissCollectSongList(collect);
        }
        if(songListService.collectSongList(collect) > 0){
            Integer songListCollectCount = songListMapper.getSongListCollectCount(collect.getSongListId());
            collectResult.setCollectCount(songListCollectCount);
            collectResult.setStatus("success");
            collectResult.setMsg("收藏歌单成功");
        }else {
            collectResult.setStatus("fail");
            collectResult.setMsg("收藏歌单失败");
        }
        return collectResult;
    }

    /**
     * 取消收藏歌单
     * @param collect
     * @return
     */
    @PostMapping("/dismissCollectSongList")
    @ResponseBody
    public CollectResult dismissCollectSongList(Collect collect){
        CollectResult collectResult = new CollectResult();
        if(songListMapper.dismissCollectSongList(collect)>0){
            Integer songListCollectCount = songListMapper.getSongListCollectCount(collect.getSongListId());
            collectResult.setCollectCount(songListCollectCount);
            collectResult.setStatus("success");
            collectResult.setMsg("取消收藏歌单成功");
        }else {
            collectResult.setStatus("fail");
            collectResult.setMsg("出错了");
        }
        return collectResult;
    }

    /**
     * 点赞歌单评论
     * @param commentId
     * @return
     */
    @PostMapping("/upSongListComment")
    @ResponseBody
    public UpCommentResult upSongListComment(Integer commentId){
        UpCommentResult upCommentResult = new UpCommentResult();
        if(songListMapper.upSongListCommment(commentId)>0){
            int songListCommentUpCount = songListMapper.getSongListCommentUpCount(commentId);
            upCommentResult.setUpCount(songListCommentUpCount);
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
     * 获取热门歌单前30个
     * 分页展示
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getHotSongList")
    @ResponseBody
    public PageVo<SongList> getHotSongList(Integer current,Integer size){
        return songListService.songListPageByCollections(current, size);
    }

    /**
     * 获取用户收藏的歌单
     * @param current
     * @param size
     * @param userId
     * @return
     */
    @PostMapping("/getUserCollectSongList")
    @ResponseBody
    public PageVo<SongList> getUserCollectSong(Integer current,Integer size,Integer userId){
        return songListService.queryUserCollectSongList(current, size, userId);
    }

    /**
     * 删除用户收藏的歌单
     * @param songListId
     * @param userId
     * @return
     */
    @PostMapping("/deleteCollectSongList")
    @ResponseBody
    public HashMap<String, String> deleteCollectSongList(Integer songListId,Integer userId){
        int delete = songListService.deleteUserCollectSongList(songListId, userId);
        HashMap<String, String> result = new HashMap<>();
        if(delete > 0){
            result.put("state","success");
            result.put("msg","取消收藏成功");
        }else {
            result.put("state","fail");
            result.put("msg","出错了");
        }
        return result;
    }
}
