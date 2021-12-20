package henu.javaweb.collapstime.model;

import java.util.LinkedList;

/**
 * 存储歌手详情页中的所有信息
 */
public class SingerDetail {
    //歌手基本信息
    private Singer singer;
    //歌手所唱的所有歌曲
    private LinkedList<SongShowInList> songs;
    //歌手是否关注 0为关注1关注
    private Integer singerCollectStatus;
    //关注歌手的人数
    private Integer followSingerCount;

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public LinkedList<SongShowInList> getSongs() {
        return songs;
    }

    public void setSongs(LinkedList<SongShowInList> songs) {
        this.songs = songs;
    }

    public Integer getSingerCollectStatus() {
        return singerCollectStatus;
    }

    public void setSingerCollectStatus(Integer singerCollectStatus) {
        this.singerCollectStatus = singerCollectStatus;
    }

    public Integer getFollowSingerCount() {
        return followSingerCount;
    }

    public void setFollowSingerCount(Integer followSingerCount) {
        this.followSingerCount = followSingerCount;
    }
}
