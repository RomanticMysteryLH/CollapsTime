package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.*;

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
}
