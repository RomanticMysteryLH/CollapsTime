package henu.javaweb.collapstime.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import henu.javaweb.collapstime.model.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Mapper
@Repository
public interface SongListMapper extends BaseMapper<SongList> {
    /**
     * 查询歌单的所有风格
     * @return
     */
    public String querySongListAllStyle();

    /**
     * 获取歌单的评论数
     * @param songListId
     * @return
     */
    public Integer getSongListCommentCount(Integer songListId);

    /**
     * 获取歌单收藏数
     * @param songListId
     * @return
     */
    public Integer getSongListCollectCount(Integer songListId);

    /**
     * 获取评论歌单的用户的信息
     * @param songListId
     * @return
     */
    public LinkedList<User> getSongListCommentUser(Integer songListId);

    /**
     * 获取歌单中的歌曲
     * @param songListId
     * @return
     */
    public LinkedList<Song> getSongOfSongList(Integer songListId);

    /**
     * 获取歌单评论
     * @param songListId
     * @return
     */
    public LinkedList<SongListComment> getSongListComment(Integer songListId);

    /**
     * 查询用户收藏的所有歌单id
     * @param userId
     * @return
     */
    public LinkedList<Integer> getSongListOfUserCollect(Integer userId);

    /**
     * 添加歌单评论
     * @param comment
     * @return
     */
    public Integer addSongListComment(Comment comment);

    /**
     * 收藏歌单
     * @param collect
     * @return
     */
    public Integer collectSongList(Collect collect);

    /**
     * 取消收藏歌单
     * @param collect
     * @return
     */
    public int dismissCollectSongList(Collect collect);

    /**
     * 获取收藏歌单的数量
     * @param songListId
     * @return
     */
    public int getCollectSongListCount(Integer songListId);

    /**
     * 点赞歌单评论
     * @param commentId
     * @return
     */
    public int upSongListCommment(Integer commentId);

    /**
     * 取消歌单评论的点赞
     * @param commentId
     * @return
     */
    public int dismissUpSongListComment(Integer commentId);

    /**
     * 获取歌单评论的点赞数
     * @param commentId
     * @return
     */
    public int getSongListCommentUpCount(Integer commentId);
}
