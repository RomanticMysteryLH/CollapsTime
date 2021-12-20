package henu.javaweb.collapstime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import henu.javaweb.collapstime.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Mapper
@Repository
public interface SongMapper extends BaseMapper<Song> {
    /**
     * 根据歌曲id查询歌曲
     * @param songId
     * @return
     */
    public Song getSongById(Integer songId);

    /**
     * 获取歌曲收藏数
     * @param songId
     * @return
     */
    public Integer getSongCollectCount(Integer songId);

    /**
     * 获取歌曲评论数
     * @param songId
     * @return
     */
    public Integer getSongCommentCount(Integer songId);

    /**
     * 获取歌曲评论
     * @param songId
     * @return
     */
    public LinkedList<SongComment> getSongComment(Integer songId);

    /**
     * 获取对某歌曲评论的用户的信息
     * @param songId
     * @return
     */
    public LinkedList<User> getSongOfCommentUser(Integer songId);

    /**
     * 获取用户收藏的歌曲id
     * @param userId
     * @return
     */
    public LinkedList<Integer> getSongIdOfUserCollect(Integer userId);

    /**
     * 根据歌曲id获取歌曲名字
     * @param songId
     * @return
     */
    public String getSongNameBySongId(Integer songId);

    /**
     * 添加歌曲评论
     * @param comment
     * @return
     */
    public int addComment(Comment comment);

    /**
     * 收藏歌曲
     * @param collect
     * @return
     */
    public int collectSong(Collect collect);

    /**
     * 用户取消收藏歌曲
     * @param collect
     * @return
     */
    public int dismissCollectSong(Collect collect);

    /**
     * 根据歌曲id获取歌词
     * @param songId
     * @return
     */
    public String getLyricBySongId(Integer songId);

    /**
     * 根据用户id查询用户收藏的所有歌曲的id
     * @param userId
     * @return
     */
    public LinkedList<Integer> getSongOfUserCollect(Integer userId);

    /**
     * 获取收藏歌曲的数量
     * @param songId
     * @return
     */
    public int getCollectSongCount(Integer songId);

    /**
     * 获取歌曲评论的点赞数
     * @param commentId
     * @return
     */
    public int getSongCommentUpCount(Integer commentId);

    /**
     * 点赞歌曲评论
     * @param commentId
     * @return
     */
    public int upSongCommment(Integer commentId);

    /**
     * 向播放表中插入一条数据
     * @param songId
     * @return
     */
    public int addPlayCount(Integer songId,Integer userId);

    /**
     * 查询播放表中的所有歌曲id
     * @return
     */
    public LinkedList<Integer> queryAllSongIdOfPlay();

    /**
     * 更新歌曲播放量
     * @param songId
     * @return
     */
    public int updatePlayCount(Integer songId);

    /**
     * 获取最热的前五十首歌
     * @return
     */
    public LinkedList<Song> querySongTop50(LinkedList<Integer> songIds);

    /**
     * 获取最热的前五十首歌的id
     */
    public LinkedList<Integer> querySongTop50OfId();

}
