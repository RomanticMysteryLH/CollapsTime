package henu.javaweb.collapstime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.SongList;
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

    /**
     * 搜索歌曲得到其基本信息
     * @param key
     * @return
     */
    LinkedList<Song> searchSongInfo(String key);

    /**
     * 搜索歌单得到其基本信息
     * @param key
     * @return
     */
    LinkedList<SongList> searchSongListInfo(String key);

    /**
     * 搜索歌手得到其基本信息
     * @param key
     * @return
     */
    LinkedList<Singer> searchSingerInfo(String key);

    /**
     * 更新用户基本信息
     * {
     *     username
     *     sex
     *     location
     *     introduction
     * }
     * @param user
     * @return
     */
    int updateUserBaseInfo(User user);

    /**
     * 更新用户密码
     * @param account
     * @param password
     * @return
     */
    int updatePwd(String account,String password);

    /**
     * 更新用户头像
     * @param account
     * @param filePath
     * @return
     */
    int updateAvator(String account,String filePath);

    /**
     * 查询用户更新头像之前的照片
     * @param account
     * @return
     */
    String queryUserAvatorOfPast(String account);

    /**
     * 根据账号查询用户信息
     * @param account
     * @return
     */
    User queryUserByAccount(String account);

    /**
     * 注销用户
     * @param account
     * @return
     */
    Integer deleteUser(String account);

    /**
     * 获取男性会员数量
     * @return
     */
    Integer getUserCountOfmale();

    /**
     * 获取女性会员数量
     * @return
     */
    Integer getUserCountOfFemale();

    /**
     * 根据歌曲名字获取所属歌单的id
     * @param songName
     * @return
     */
    Integer getSongListIdBySongName(String songName);

    /**
     * 根据歌单id获取歌单类型
     * @param id
     * @return
     */
    String getSongListTypeById(Integer id);

    /**
     * 根据歌单类型获取歌单id
     * @param style
     * @return
     */
    LinkedList<Integer> getSongListIdByStyle(String style);

}