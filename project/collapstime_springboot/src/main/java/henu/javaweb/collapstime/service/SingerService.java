package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.*;

public interface SingerService {
    public PageVo<Singer> singerPage(Integer currentPage, Integer pageSize);
    public PageVo<Singer> singerPageByInitial(Integer currentPage, Integer pageSize,String initial);
    public PageVo<Singer> singerPageByCategory(Integer currentPage, Integer pageSize,Integer category);
    public PageVo<Singer> singerPageByInitialAndCategory(Integer currentPage, Integer pageSize,String initial,Integer category);
    public SingerDetail getSingerDetail(Integer singerId,Integer userId);
    public Integer followSinger(Collect collect);
    public PageVo<Singer> singerPageByCollections(Integer currentPage, Integer pageSize);
}
