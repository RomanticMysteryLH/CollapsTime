package henu.javaweb.collapstime.service.Impl;

import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.service.AdminSingerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminSingerServiceImpl implements AdminSingerService {
    @Autowired
    private SingerMapper singerMapper;
    @Override
    public Integer updateSinger(Singer singer) { return singerMapper.updateById(singer); }

    @Override
    public Singer querySinger(String id) { return singerMapper.selectById(id); }

    @Override
    public Integer addSinger(Singer singer) { return singerMapper.insert(singer); }

    @Override
    public Integer singerDelete(String id) { return singerMapper.deleteById(id); }
}
