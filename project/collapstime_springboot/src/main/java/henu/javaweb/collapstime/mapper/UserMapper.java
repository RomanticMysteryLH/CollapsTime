package henu.javaweb.collapstime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import henu.javaweb.collapstime.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.LinkedList;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {


    //根据用户提供的部分信息添加用户
    int insertSelective(User record);

    //查询所有用户
    LinkedList<User> queryAllUser();

    //根据账号查user
    User selectByAccount(String account);

    //更新用户数据
    int updateUserdata(User user);

    /**
     * 根据关键字搜索歌曲
     *
     * {
     *     id:
     *     name:
     * }
     *
     * @param key
     * @return
     */
    LinkedList<HashMap<String,Object>> searchSong(String key);

    /**
     * 搜索歌手
     * {
     *     id:
     *     name:
     * }
     * @param key
     * @return
     */
    LinkedList<HashMap<String, Object>> searchSinger(String key);

    /**
     * 搜索歌单
     * {
     *     id:
     *     title:
     *     style:
     * }
     * @param key
     * @return
     */
    LinkedList<HashMap<String, Object>> searchSongList(String key);

}