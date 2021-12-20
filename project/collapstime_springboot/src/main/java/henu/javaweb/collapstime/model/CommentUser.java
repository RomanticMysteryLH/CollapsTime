package henu.javaweb.collapstime.model;

import java.util.Date;

/**
 * 存储发表评论的用户的相关信息
 */
public class CommentUser {
    private Integer id;
    private String username;
    private String avator;
    private Integer commentId;
    private String content;
    private String createTime;
    private Integer commentUp;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getCommentUp() {
        return commentUp;
    }

    public void setCommentUp(Integer commentUp) {
        this.commentUp = commentUp;
    }
}
