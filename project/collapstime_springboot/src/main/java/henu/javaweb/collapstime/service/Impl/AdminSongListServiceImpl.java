package henu.javaweb.collapstime.service.Impl;

import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.SongList;
import henu.javaweb.collapstime.service.AdminSongListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSongListServiceImpl implements AdminSongListService {
    @Autowired
    private SongListMapper songListMapper;

    @Override
    public Integer updateSongList(SongList songList) {
        return songListMapper.updateById(songList);
    }

    @Override
    public SongList querySongList(String id) {
        return songListMapper.selectById(id);
    }

    @Override
    public Integer addSongList(SongList songList) {
        return songListMapper.insert(songList);
    }

    @Override
    public Integer songListDelete(String id) {
        return songListMapper.deleteById(id);
    }

    @Override
    public Integer songListCommentDelete(String id) {
        return songListMapper.deleteSongListComment(id);
    }

    @Override
    public Integer songListSongDelete(String songListId, String songId) {
        return songListMapper.deleteSongListSong(songListId,songId);
    }
}
