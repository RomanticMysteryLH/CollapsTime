package henu.javaweb.collapstime.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import org.apache.ibatis.type.Alias;

/**
 * 歌曲播放
 */
@TableName("play")
public class Play {
    //歌曲id
    private Integer songId;
    //歌曲名字
    @TableField(exist = false)
    private String name;
    //歌曲播放次数
    private Integer playCount;

    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Integer playCount) {
        this.playCount = playCount;
    }
}
