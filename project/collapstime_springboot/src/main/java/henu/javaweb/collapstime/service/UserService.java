package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.SongShowInList;
import henu.javaweb.collapstime.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;


public interface UserService {

    public int register(User user);
    public boolean accountDetection(String account);
    public User selectByAccount(String account);
    public int updateUser(User user);
    public HashMap<String, Object> search(String key);
    //搜索与该歌曲风格相同的其他歌曲
    public HashMap<String,Object> similarSearch(String key);

    PageVo<SongShowInList> similarSearchInfo(Integer current, Integer size, String key, Integer userId);

}
