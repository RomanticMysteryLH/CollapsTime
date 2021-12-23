package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.UserMapper;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.User;
import henu.javaweb.collapstime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int register(User user) {
        int i = userMapper.insertSelective(user);
        return i;
    }

    @Override
    public boolean accountDetection(String account) {
        //一开始设定没有账号重复
        boolean flag = true;
        LinkedList<User> users = userMapper.queryAllUser();
        for (User user : users) {
            if(account.equals(user.getAccount())){
                //如果有该账号存在修改flag的值
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public User selectByAccount(String account) {
        User user = userMapper.selectByAccount(account);
        return user;
    }


    @Override
    public int updateUser(User user) {
        int i = userMapper.updateUserdata(user);
        return i;
    }

    /**
     * 根据关键字进行全局搜索
     * @param key
     * @return
     */
    @Override
    public HashMap<String, Object> search(String key) {
        HashMap<String,Object> result = new HashMap<>();
        LinkedList<HashMap<String, Object>> singers = userMapper.searchSinger(key);
        LinkedList<HashMap<String, Object>> songs = userMapper.searchSong(key);
        LinkedList<HashMap<String, Object>> songLists = userMapper.searchSongList(key);
        result.put("songs",songs);
        result.put("singers",singers);
        result.put("songLists",songLists);
        return result;
    }


}
