package henu.javaweb.collapstime.model;

import lombok.Data;

import java.util.List;

/**
 * 用来保存分页所需的数据
 * @param <T>
 */


public class PageVo<T> {
    private Integer current;
    private Integer size;
    private Long total;
    private List<T> dataList;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
