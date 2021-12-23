package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

@Service
public class SongServiceImpl implements SongService {


    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SingerMapper singerMapper;

    @Override
    public PageVo<Song> songPage(Integer currentPage, Integer pageSize) {
        PageVo<Song> songVo = new PageVo<>();
        IPage<Song> page = new Page<>(currentPage, pageSize);
        songMapper.selectPage(page,null);
        songVo.setTotal(page.getTotal());
        songVo.setCurrent(currentPage);
        songVo.setDataList(page.getRecords());
        songVo.setSize(pageSize);
        return songVo;
    }

    /**
     * 获取歌曲详情页所需的所有信息
     * @param songId
     * @param userId
     * @return
     */
    @Override
    public SongDetail getSongDetail(Integer songId, Integer userId) {
        Song song = songMapper.getSongById(songId);
        SongInfo songInfo = new SongInfo();
        Integer songCollectCount = songMapper.getSongCollectCount(songId);
        Integer songCommentCount = songMapper.getSongCommentCount(songId);
        LinkedList<SongComment> songComment = songMapper.getSongComment(songId);
        LinkedList<User> songOfCommentUser = songMapper.getSongOfCommentUser(songId);
        LinkedList<Integer> songIdOfUserCollect = songMapper.getSongIdOfUserCollect(userId);
        SongDetail songDetail = new SongDetail();
        Integer collectStatus = 0;
        for (Integer id : songIdOfUserCollect) {
            if(id.equals(songId)){
                collectStatus = 1;
                break;
            }
        }
        songDetail.setCollectStatus(collectStatus);
        songDetail.setCommentCount(songCommentCount);
        songDetail.setCollectCount(songCollectCount);
        String lyricOrigin = song.getLyric();
        String lyric = "";
        String[] split = lyricOrigin.split("\n");
        for(int i = 0;i < split.length; i++){
            String temp="";
            try {
                temp = split[i].split("]")[1];
            }catch (Exception e) {
                continue;
            }
            lyric+=temp;
            lyric +="\n";
        }
        String os = System.getProperty("os.name");
        String path = null;
        if (os.toLowerCase().startsWith("win")){
            path = Cons.RESOURCE_WIN_PATH;
        }else {
            path = Cons.RESOURCE_MAC_PATH;
        }
        songInfo.setLyricWithTime(song.getLyric());
        songInfo.setId(song.getId());
        songInfo.setIntroduction(song.getIntroduction());
        songInfo.setName(song.getName().split("-")[1]);
        songInfo.setPicture(song.getPicture());
        songInfo.setSingerId(song.getSingerId());
        songInfo.setUrl(song.getUrl());
        path+=song.getUrl();
        songInfo.setDuration(MusicUtils.getDuration(path));
        songInfo.setLyric(lyric);
        songInfo.setSingerName(singerMapper.getSingerNameById(song.getSingerId()));
        songDetail.setSongInfo(songInfo);
        LinkedList<CommentUser> commentUserList = new LinkedList<CommentUser>();
        for(int i = 0;i < songComment.size(); i++){
            for(int j = 0;j < songOfCommentUser.size(); j++){
                if(songOfCommentUser.get(j).getId().equals(songComment.get(i).getUserId())){
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
        songDetail.setCommentUsers(commentUserList);
        return songDetail;
    }
    /**
     * 增加歌曲评论
     * @param comment
     * @return
     */
    @Override
    public int addSongComment(Comment comment) {
        return songMapper.addComment(comment);
    }

    /**
     * 收藏歌曲
     * @param collect
     * @return
     */
    @Override
    public int collectSong(Collect collect) {
        return songMapper.collectSong(collect);
    }

    /**
     * 获取热门50首歌
     * @param userId 此参数用于判断用户对歌曲是否收藏
     * @return
     */
    @Override
    public PageVo<SongShowInList> getHotSongTop50(Integer current,Integer size,Integer userId){
        LinkedList<SongShowInList> list = new LinkedList<>();
        //热门的50首歌曲
        LinkedList<Song> songs = songMapper.querySongTop50(songMapper.querySongTop50OfId());
        String os = System.getProperty("os.name");
        LinkedList<Integer> songOfUserCollect = songMapper.getSongOfUserCollect(userId);
        int end = 0;
        if((current*size) > songs.size())
        {
            end = songs.size();
        }else {
            end = current*size;
        }
        for(int i = (current-1)*size;i < end; i++){
            String path = null;
            if (os.toLowerCase().startsWith("win")){
                path = Cons.RESOURCE_WIN_PATH;
            }else {
                path = Cons.RESOURCE_MAC_PATH;
            }
            SongShowInList songShowInList = new SongShowInList();
            Integer collectStatus = 0;
            for (int j = 0; j < songOfUserCollect.size(); j++) {
                if(songs.get(i).getId().equals(songOfUserCollect.get(j))){
                    collectStatus = 1;
                    break;
                }else {
                    continue;
                }
            }
            songShowInList.setCollectStatus(collectStatus);
            songShowInList.setUrl(songs.get(i).getUrl());
            songShowInList.setCover(songs.get(i).getPicture());
            songShowInList.setSingerId(songs.get(i).getSingerId());
            songShowInList.setId(songs.get(i).getId());
            songShowInList.setName(songs.get(i).getName().split("-")[1]);
            songShowInList.setSingerName(singerMapper.getSingerNameById(songs.get(i).getSingerId()));
            path+=songs.get(i).getUrl();
            songShowInList.setDuration(MusicUtils.getDuration(path));
            list.add(songShowInList);
        }
        PageVo<SongShowInList> songShowInListPageVo = new PageVo<>();
        songShowInListPageVo.setTotal((long) songs.size());
        songShowInListPageVo.setCurrent(current);
        songShowInListPageVo.setSize(size);
        songShowInListPageVo.setDataList(list);
        return songShowInListPageVo;
    }

    /**
     * 获取用户播放次数最多的五首歌的信息
     * 歌曲id
     * 歌曲名字
     * 播放数量
     * 用户总共的播放数量total
     * @param userId
     * @return
     */
    @Override
    public HashMap<String, Object> getTop5OfUserPlayCount(Integer userId) {
        LinkedList<Play> plays = songMapper.getTop5OfUserPlayCount(userId);
        for(int i = 0; i < plays.size(); i++){
            plays.get(i).setName(songMapper.getSongNameBySongId(plays.get(i).getSongId()).split("-")[1]);
        }
        int count = songMapper.getAllPlaySongCountOfUser(userId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("total",count);
        map.put("songs",plays);
        return map;
    }

    /**
     * 删除用户收藏的歌曲
     * @param songId
     * @param userId
     * @return
     */
    @Override
    public int deleteCollectSong(Integer songId, Integer userId) {
        return songMapper.deleteCollectSongById(songId, userId);
    }

    /**
     * 获取用户收藏的歌曲 分页形式
     * @param userId
     * @return
     */
    @Override
    public PageVo<SongShowInList> queryCollectSong(Integer current,Integer size,Integer userId) {
        LinkedList<SongShowInList> list = new LinkedList<>();
        LinkedList<Song> userCollect = songMapper.getSongInfoOfUserCollect(userId);
        String os = System.getProperty("os.name");
        for(int i = (current-1)*size;i < current*size; i++){
            String path = null;
            if (os.toLowerCase().startsWith("win")){
                path = Cons.RESOURCE_WIN_PATH;
            }else {
                path = Cons.RESOURCE_MAC_PATH;
            }
            SongShowInList songShowInList = new SongShowInList();
            Integer collectStatus = 1;
            songShowInList.setCollectStatus(collectStatus);
            songShowInList.setUrl(userCollect.get(i).getUrl());
            songShowInList.setCover(userCollect.get(i).getPicture());
            songShowInList.setSingerId(userCollect.get(i).getSingerId());
            songShowInList.setId(userCollect.get(i).getId());
            songShowInList.setName(userCollect.get(i).getName().split("-")[1]);
            songShowInList.setSingerName(singerMapper.getSingerNameById(userCollect.get(i).getSingerId()));
            path+=userCollect.get(i).getUrl();
            songShowInList.setDuration(MusicUtils.getDuration(path));
            list.add(songShowInList);
        }
        PageVo<SongShowInList> songShowInListPageVo = new PageVo<>();
        songShowInListPageVo.setTotal((long) userCollect.size());
        songShowInListPageVo.setCurrent(current);
        songShowInListPageVo.setSize(size);
        songShowInListPageVo.setDataList(list);
        return songShowInListPageVo;
    }
}
