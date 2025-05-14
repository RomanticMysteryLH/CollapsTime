package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.Song;

import java.util.List;

public interface AdminSongService {
    /**
     * 更新后台歌曲信息
     * @param song
     * @return
     */
    public Integer updateSong(Song song);

    /**
     *根据ID查询歌曲封面路径
     * @param id
     * @return
     */
    public Song querySong(String id);

    /**
     * 添加歌曲
     * @param song
     * @return
     */
    public Integer addSong(Song song);

    /**
     * 获取歌手列表
     * @return
     */
    public List getSinger(Song song);

    /**
     *删除歌曲以及它的图片、歌曲文件
     * @param id
     * @return
     */
    public Integer songDelete(String id);
}
