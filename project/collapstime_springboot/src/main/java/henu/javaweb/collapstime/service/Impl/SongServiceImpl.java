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
}
