package henu.javaweb.collapstime.model;

import java.util.LinkedList;

public class SongDetail {
    //收藏数
    private Integer collectCount;
    //评论数
    private Integer commentCount;
    //歌曲基本信息
    private SongInfo songInfo;
    //对某首歌发表评论的用户的相关信息
    private LinkedList<CommentUser> commentUsers;
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

    public SongInfo getSongInfo() {
        return songInfo;
    }

    public void setSongInfo(SongInfo songInfo) {
        this.songInfo = songInfo;
    }

    public LinkedList<CommentUser> getCommentUsers() {
        return commentUsers;
    }

    public void setCommentUsers(LinkedList<CommentUser> commentUsers) {
        this.commentUsers = commentUsers;
    }

    public Integer getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(Integer collectStatus) {
        this.collectStatus = collectStatus;
    }
}
