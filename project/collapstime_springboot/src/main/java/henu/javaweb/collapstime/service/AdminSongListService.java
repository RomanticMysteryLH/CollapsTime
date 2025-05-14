package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.SongList;

public interface AdminSongListService {

    /**
     * 更新后台歌单信息
     * @param songList
     * @return
     */
    public Integer updateSongList(SongList songList);

    /**
     *根据ID查询歌单信息
     * @param id
     * @return
     */
    public SongList querySongList(String id);

    /**
     * 添加歌单
     * @param songList
     * @return
     */
    public Integer addSongList(SongList songList);

    /**
     *删除歌单
     * @param id
     * @return
     */
    public Integer songListDelete(String id);

    /**
     * 删除歌单评论
     * @param id
     * @return
     */
    public Integer songListCommentDelete(String id);

    public Integer songListSongDelete(String songListId,String songId);
}
