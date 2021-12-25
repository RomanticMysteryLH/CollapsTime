package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;

public interface AdminSingerService {
    /**
     * 更新后台歌手信息
     * @param singer
     * @return
     */
    public Integer updateSinger(Singer singer);

    /**
     *根据ID查询歌手信息
     * @param id
     * @return
     */
    public Singer querySinger(String id);

    /**
     * 添加歌手
     * @param singer
     * @return
     */
    public Integer addSinger(Singer singer);

    /**
     *删除歌手
     * @param id
     * @return
     */
    public Integer singerDelete(String id);
}
