package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public interface SongListService {
    public PageVo<SongList> songListPage(Integer currentPage, Integer pageSize);
    public HashSet<String> querySongListAllStyle();
    public PageVo<SongList> songListPageByStyle(Integer currentPage, Integer pageSize,String style);
    public SongListDetail getSongListDetail(Integer songListId,Integer userId);
    public Integer addSongListComment(Comment comment);
    public Integer collectSongList(Collect collect);
    public PageVo<SongList> songListPageByCollections(Integer currentPage, Integer pageSize);
    public PageVo<SongList> queryUserCollectSongList(Integer current,Integer size,Integer userId);
    public int deleteUserCollectSongList(Integer songListId,Integer userId);
    public PageVo<SongList> searchSongListInfo(Integer currentPage, Integer pageSize,String key);
}