package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.*;

import java.util.HashMap;
import java.util.LinkedList;

public interface SongService {
    /**
     * 歌曲分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageVo<Song> songPage(Integer currentPage, Integer pageSize);

    /**
     * 获取歌曲详情页面的所需信息
     * @param songId
     * @param userId
     * @return
     */
    public SongDetail getSongDetail(Integer songId,Integer userId);
    /**
     * 添加歌曲评论
     * @param comment
     * @return
     */
    public int addSongComment(Comment comment);

    /**
     * 收藏歌曲
     * @param collect
     * @return
     */
    public int collectSong(Collect collect);

    /**
     * 获取热门的50首歌以分页的形式
     * @param userId
     * @return
     */
    public PageVo<SongShowInList> getHotSongTop50(Integer current,Integer size,Integer userId);

    /**
     * 获取用户播放最多的前五首歌
     * @param userId
     * @return
     */
    public HashMap<String, Object> getTop5OfUserPlayCount(Integer userId);

    /**
     * 删除用户收藏的歌曲
     * @param songId
     * @param userId
     */
    public int deleteCollectSong(Integer songId, Integer userId);

    /**
     * 获取用户收藏的歌曲
     * @param userId
     */
    public PageVo<SongShowInList> queryCollectSong(Integer current,Integer size,Integer userId);

    public PageVo<SongShowInList> searchSongInfo(Integer current,Integer size,String key,Integer userId);

    /**
     * 获取所有歌曲表达的心情
     * @return
     */
    public LinkedList<String> getAllEmotion();

    /**
     * 给定相应心情，筛选出相应的歌曲
     * @param current
     * @param size
     * @param key
     * @param userId
     * @return
     */
    public PageVo<SongShowInList> getSongByEmotion(Integer current,Integer size,String key,Integer userId);
}
