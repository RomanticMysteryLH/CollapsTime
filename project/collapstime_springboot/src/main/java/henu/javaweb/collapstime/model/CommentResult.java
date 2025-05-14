package henu.javaweb.collapstime.model;

import java.util.LinkedList;

public class CommentResult {
    private String status;
    private String msg;
    private LinkedList<CommentUser> commentUser;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public LinkedList<CommentUser> getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(LinkedList<CommentUser> commentUser) {
        this.commentUser = commentUser;
    }
}
