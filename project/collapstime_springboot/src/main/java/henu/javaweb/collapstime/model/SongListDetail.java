package henu.javaweb.collapstime.model;

import java.util.LinkedList;

/**
 * 存储歌单详情页的所有信息
 */
public class SongListDetail {
    //收藏数
    private Integer collectCount;
    //评论数
    private Integer commentCount;
    //歌单中歌曲的相关信息
    private LinkedList<SongShowInList> songs;
    //歌单中评论的用户的相关信息
    private LinkedList<CommentUser> commentUsers;
    //歌单的基本信息
    private SongList songListInfo;
    //用户收藏状态  0为未收藏1为收藏
    private Integer collectStatus;


    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public LinkedList<SongShowInList> getSongs() {
        return songs;
    }

    public void setSongs(LinkedList<SongShowInList> songs) {
        this.songs = songs;
    }


    public LinkedList<CommentUser> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(LinkedList<CommentUser> commentUsers) {
        this.commentUsers = commentUsers;
    }

    public SongList getSongListInfo() {
        return songListInfo;
    }

    public void setSongListInfo(SongList songListInfo) {
        this.songListInfo = songListInfo;
    }

    public Integer getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(Integer collectStatus) {
        this.collectStatus = collectStatus;
    }
}
