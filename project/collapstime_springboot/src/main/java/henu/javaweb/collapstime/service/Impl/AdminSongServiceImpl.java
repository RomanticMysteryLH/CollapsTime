package henu.javaweb.collapstime.service.Impl;

import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.service.AdminSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminSongServiceImpl implements AdminSongService {
    @Autowired
    private SongMapper songMapper;

    @Override
    public Integer updateSong(Song song) {
        return songMapper.updateById(song);
    }

    @Override
    public Song querySong(String id) {
        return songMapper.selectById(id);
    }

    @Override
    public Integer addSong(Song song) { return songMapper.insert(song); }

    @Override
    public List getSinger(Song song) { return songMapper.selectList(null); }

    @Override
    public Integer songDelete(String id) { return songMapper.deleteById(id); }
}
