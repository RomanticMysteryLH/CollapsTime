package henu.javaweb.collapstime.model;

/**
 * 存储歌曲在歌单和歌手详情页面中歌曲的展示信息
 */
public class SongShowInList {
    //歌曲id
    private Integer id;
    //歌名
    private String name;
    //时长
    private String duration;
    //歌手名
    private String singerName;
    //歌手id
    private Integer singerId;
    //歌曲路径
    private String url;
    //歌曲图片
    private String cover;
    //用户收藏状态  0为未收藏1为收藏
    private Integer collectStatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Integer getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(Integer collectStatus) {
        this.collectStatus = collectStatus;
    }
}
