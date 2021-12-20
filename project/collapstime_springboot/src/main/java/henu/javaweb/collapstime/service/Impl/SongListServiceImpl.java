package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SongListService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SongListServiceImpl implements SongListService {

    @Autowired
    private SongListMapper songListMapper;
    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private SongMapper songMapper;

    /**
     * 实现歌单分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageVo<SongList> songListPage(Integer currentPage, Integer pageSize) {
        PageVo<SongList> songListVo = new PageVo<SongList>();
        Page<SongList> page = new Page<SongList>(currentPage,pageSize);
        songListMapper.selectPage(page,null);
        songListVo.setTotal(page.getTotal());
        songListVo.setSize(pageSize);
        List<SongList> records = page.getRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0;i < records.size(); i++){
            try {
                Date date = sdf.parse(records.get(i).getCreateTime());
                records.get(i).setCreateTime(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        songListVo.setDataList(records);
        songListVo.setCurrent(currentPage);
        return songListVo;
    }

    /**
     * 获取歌单所有的style
     * @return LinkedList
     */
    @Override
    public HashSet<String> querySongListAllStyle() {
        String styles = songListMapper.querySongListAllStyle();
        HashSet<String> allStyle = new HashSet<>();
        String[] split = styles.split(",");
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
        return allStyle;
    }

    /**
     * 根据歌单风格查询并进行分页
     * @param currentPage
     * @param pageSize
     * @param style
     * @return
     */
    @Override
    public PageVo<SongList> songListPageByStyle(Integer currentPage, Integer pageSize, String style) {
        PageVo<SongList> songListVo = new PageVo<>();
        QueryWrapper<SongList> songListQueryWrapper = new QueryWrapper<>();
        songListQueryWrapper.like("style",style);
        Page<SongList> page = new Page<SongList>(currentPage, pageSize);
        songListMapper.selectPage(page, songListQueryWrapper);
        songListVo.setCurrent(currentPage);
        songListVo.setSize(pageSize);
        List<SongList> records = page.getRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0;i < records.size(); i++){
            try {
                Date date = sdf.parse(records.get(i).getCreateTime());
                records.get(i).setCreateTime(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        songListVo.setDataList(records);
        songListVo.setTotal(page.getTotal());
        return songListVo;
    }

    /**
     * 获取歌单详情界面的所有信息
     * @param songListId
     * @return
     */
    @Override
    public SongListDetail getSongListDetail(Integer songListId,Integer userId) {
        //获取用户收藏的歌单
        LinkedList<Integer> songListIds = songListMapper.getSongListOfUserCollect(userId);
        //判断用户是否收藏了该歌单
        Integer collectStatus = 0;
        for (Integer id : songListIds) {
            if(id.equals(songListId)){
               collectStatus = 1;
               break;
            }
        }
        HashMap<String, Object> map = new HashMap<>();
        //获取歌单的评论数
        Integer songListCommentCount = songListMapper.getSongListCommentCount(songListId);
        //获取歌单的收藏数
        Integer songListCollectCount = songListMapper.getSongListCollectCount(songListId);
        //获取歌单的所有评论
        LinkedList<SongListComment> songListComment = songListMapper.getSongListComment(songListId);
        //获取歌单的所有评论的用户
        LinkedList<User> songListCommentUser = songListMapper.getSongListCommentUser(songListId);
        //获取歌单中的所有歌曲
        LinkedList<Song> songOfSongList = songListMapper.getSongOfSongList(songListId);
        //获取歌单基本信息
        SongList songListBaseInfo = songListMapper.selectById(songListId);
        SongListDetail songListDetail = new SongListDetail();
        songListDetail.setCollectStatus(collectStatus);
        songListDetail.setSongListInfo(songListBaseInfo);
        songListDetail.setCollectCount(songListCollectCount);
        songListDetail.setCommentCount(songListCommentCount);
        LinkedList<CommentUser> commentUserList = new LinkedList<CommentUser>();
        for(int i = 0;i < songListComment.size(); i++){
            for(int j = 0;j < songListCommentUser.size(); j++){
                if(songListCommentUser.get(j).getId().equals(songListComment.get(i).getUserId())){
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
        songListDetail.setCommentUsers(commentUserList);
//        map.put("commentUser",commentUserList);
        LinkedList<SongShowInList> songs = new LinkedList<>();
        String os = System.getProperty("os.name");
        LinkedList<Integer> songOfUserCollect = songMapper.getSongOfUserCollect(userId);
        for(int i = 0;i < songOfSongList.size(); i++){
            SongShowInList songShowInList = new SongShowInList();
            String path = null;
            if (os.toLowerCase().startsWith("win")){
                path = Cons.RESOURCE_WIN_PATH;
            }else {
                path = Cons.RESOURCE_MAC_PATH;
            }
            collectStatus = 0;
            for (int j = 0; j < songOfUserCollect.size(); j++) {
                if(songOfSongList.get(i).getId().equals(songOfUserCollect.get(j))){
                    collectStatus = 1;
                    break;
                }else {
                    continue;
                }
            }
            songShowInList.setCollectStatus(collectStatus);
            songShowInList.setUrl(songOfSongList.get(i).getUrl());
            songShowInList.setCover(songOfSongList.get(i).getPicture());
            songShowInList.setSingerId(songOfSongList.get(i).getSingerId());
            songShowInList.setId(songOfSongList.get(i).getId());
            songShowInList.setName(songOfSongList.get(i).getName().split("-")[1]);
            songShowInList.setSingerName(singerMapper.getSingerNameById(songOfSongList.get(i).getSingerId()));
            path+=songOfSongList.get(i).getUrl();
            songShowInList.setDuration(MusicUtils.getDuration(path));
            songs.add(songShowInList);
        }
        songListDetail.setSongs(songs);
//        map.put("songs",songs);
        return songListDetail;
    }
    /**
     * 添加歌单评论
     * @param comment
     * @return
     */
    @Override
    public Integer addSongListComment(Comment comment) {
        return songListMapper.addSongListComment(comment);
    }

    /**
     * 收藏歌单
     * @param collect
     * @return
     */
    @Override
    public Integer collectSongList(Collect collect) {
        return songListMapper.collectSongList(collect);
    }

    /**
     * 获取热门歌单前三十个
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageVo<SongList> songListPageByCollections(Integer currentPage, Integer pageSize) {
        //获取热门歌单三十个
        LinkedList<SongList> songLists = songListMapper.querySongListTop30(songListMapper.querySongListTop30OfId());
        LinkedList<SongList> songListOfPart = new LinkedList<>();
        for(int i = (currentPage - 1)*pageSize;i < currentPage*pageSize;i++){
            songListOfPart.add(songLists.get(i));
        }
        PageVo<SongList> pageVo = new PageVo<>();
        pageVo.setTotal((long) songLists.size());
        pageVo.setSize(pageSize);
        pageVo.setDataList(songListOfPart);
        pageVo.setCurrent(currentPage);
        return pageVo;
    }
}
