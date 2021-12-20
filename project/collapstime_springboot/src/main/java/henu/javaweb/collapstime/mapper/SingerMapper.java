package henu.javaweb.collapstime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import henu.javaweb.collapstime.model.Collect;
import henu.javaweb.collapstime.model.Singer;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.SongList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.security.PrivateKey;
import java.util.LinkedList;

@Mapper
@Repository
public interface SingerMapper extends BaseMapper<Singer>{
    /**
     * 根据歌手id查询歌手名字
     * @param id
     * @return
     */
    public String getSingerNameById(Integer id);

    /**
     * 根据歌手id查询歌手
     * @param singerId
     * @return
     */
    public Singer getSingerById(Integer singerId);

    /**
     * 根据歌手id查询歌手所唱的歌曲
     * @param singerId
     * @return
     */
    public LinkedList<Song> getSongBySingerId(Integer singerId);

    /**
     * 获取用户关注的所有歌手id
     * @param userId
     * @return
     */
    public LinkedList<Integer> getSingerOfUserCollect(Integer userId);

    /**
     * 关注歌手
     * @param collect
     * @return
     */
    public Integer followSinger(Collect collect);

    /**
     * 取关歌手
     * @param collect
     * @return
     */
    public Integer dismissFollowSinger(Collect collect);

    /**
     * 获取关注歌手的数量
     * @param singerId
     * @return
     */
    public int getCollectSingerCount(Integer singerId);

    /**
     * 获取最热的前二十个歌手
     * @return
     */
    public LinkedList<Singer> querySingerTop20(LinkedList<Integer> singerIds);

    /**
     * 获取热门歌手id
     */
    public LinkedList<Integer> querySingerTop20OfId();

}
