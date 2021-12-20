package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SingerService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class SingerServiceImpl implements SingerService {

    @Autowired
    private SingerMapper singerMapper;
    @Autowired
    private SongMapper songMapper;

    /**
     * 歌手分页
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageVo<Singer> singerPage(Integer currentPage, Integer pageSize) {
        PageVo<Singer> singVo = new PageVo<Singer>();
        Page<Singer> page = new Page<Singer>(currentPage, pageSize);
        singerMapper.selectPage(page,null);
        singVo.setTotal(page.getTotal());
        singVo.setSize(pageSize);
        singVo.setCurrent(currentPage);
        singVo.setDataList(page.getRecords());
        return singVo;
    }

    /**
     * 歌手根据名字首字母分页
     * @param currentPage
     * @param pageSize
     * @param initial
     * @return
     */
    @Override
    public PageVo<Singer> singerPageByInitial(Integer currentPage, Integer pageSize, String initial) {
        PageVo<Singer> singerVO = new PageVo<Singer>();
        Page<Singer> page = new Page<Singer>(currentPage, pageSize);
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>();
        queryWrapper.eq("GET_FIRST_PINYIN_CHAR(name)",initial);
        singerMapper.selectPage(page,queryWrapper);
        List<Singer> records = page.getRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0;i < records.size(); i++){
            try {
                Date date = sdf.parse(records.get(i).getBirth());
                records.get(i).setBirth(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        singerVO.setTotal(page.getTotal());
        singerVO.setDataList(records);
        singerVO.setSize(pageSize);
        singerVO.setCurrent(currentPage);
        return singerVO;
    }

    /**
     * 根据歌手性别分类
     * @param currentPage
     * @param pageSize
     * @param category
     * @return
     */
    @Override
    public PageVo<Singer> singerPageByCategory(Integer currentPage, Integer pageSize, Integer category) {
        PageVo<Singer> singerVO = new PageVo<Singer>();
        Page<Singer> page = new Page<Singer>(currentPage, pageSize);
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>();
        queryWrapper.eq("sex",category);
        singerMapper.selectPage(page,queryWrapper);
        List<Singer> records = page.getRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0;i < records.size(); i++){
            try {
                Date date = sdf.parse(records.get(i).getBirth());
                records.get(i).setBirth(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        singerVO.setTotal(page.getTotal());
        singerVO.setDataList(records);
        singerVO.setSize(pageSize);
        singerVO.setCurrent(currentPage);
        return singerVO;
    }

    /**
     * 根据歌手性别以及歌手名字首字母分类
     * @param currentPage
     * @param pageSize
     * @param category
     * @return
     */
    @Override
    public PageVo<Singer> singerPageByInitialAndCategory(Integer currentPage, Integer pageSize, String initial, Integer category) {
        PageVo<Singer> singerVO = new PageVo<Singer>();
        Page<Singer> page = new Page<Singer>(currentPage, pageSize);
        QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>();
        queryWrapper.and(i -> i.eq("sex",category).eq("GET_FIRST_PINYIN_CHAR(name)",initial));
        singerMapper.selectPage(page,queryWrapper);
        List<Singer> records = page.getRecords();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0;i < records.size(); i++){
            try {
                Date date = sdf.parse(records.get(i).getBirth());
                records.get(i).setBirth(sdf.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        singerVO.setTotal(page.getTotal());
        singerVO.setDataList(records);
        singerVO.setSize(pageSize);
        singerVO.setCurrent(currentPage);
        return singerVO;
    }

    /**
     * 获取歌手详情页的所有信息
     * @param singerId
     * @return
     */
    @Override
    public SingerDetail getSingerDetail(Integer singerId,Integer userId) {
        LinkedList<Integer> singerOfUserCollect = singerMapper.getSingerOfUserCollect(userId);
        Integer singerCollectStatus = 0;
        for (Integer id:singerOfUserCollect) {
            if(id.equals(singerId)){
                singerCollectStatus = 1;
                break;
            }
        }
        SingerDetail singerDetail = new SingerDetail();
        singerDetail.setSingerCollectStatus(singerCollectStatus);
        Singer singer = singerMapper.getSingerById(singerId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(singer.getBirth());
            singer.setBirth(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        LinkedList<Song> songLinkedList = singerMapper.getSongBySingerId(singerId);
        LinkedList<SongShowInList> songs = new LinkedList<>();
        String os = System.getProperty("os.name");
        LinkedList<Integer> songOfUserCollect = songMapper.getSongOfUserCollect(userId);
        for(int i = 0;i < songLinkedList.size(); i++){
            SongShowInList songShowInList = new SongShowInList();
            String path = null;
            if (os.toLowerCase().startsWith("win")){
                path = Cons.RESOURCE_WIN_PATH;
            }else {
                path = Cons.RESOURCE_MAC_PATH;
            }
            singerCollectStatus = 0;
            for (int j = 0; j < songOfUserCollect.size(); j++) {
                if(songLinkedList.get(i).getId().equals(songOfUserCollect.get(j))){
                    singerCollectStatus = 1;
                    break;
                }else {
                    continue;
                }
            }
            songShowInList.setCollectStatus(singerCollectStatus);
            songShowInList.setUrl(songLinkedList.get(i).getUrl());
            songShowInList.setCover(songLinkedList.get(i).getPicture());
            songShowInList.setSingerId(singerId);
            songShowInList.setId(songLinkedList.get(i).getId());
            String name = songLinkedList.get(i).getName();
            name = name.split("-")[1];
            songShowInList.setName(name);
            songShowInList.setSingerName(singerMapper.getSingerNameById(songLinkedList.get(i).getSingerId()));
            path+=songLinkedList.get(i).getUrl();
            songShowInList.setDuration(MusicUtils.getDuration(path));
            songs.add(songShowInList);
        }
        int collectSingerCount = singerMapper.getCollectSingerCount(singerId);
        singerDetail.setFollowSingerCount(collectSingerCount);
        singerDetail.setSinger(singer);
        singerDetail.setSongs(songs);
        return singerDetail;
    }
    /**
     * 关注歌手
     * @param collect
     * @return
     */
    @Override
    public Integer followSinger(Collect collect) {
        return singerMapper.followSinger(collect);
    }

    /**
     * 获取热门歌手前二十
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageVo<Singer> singerPageByCollections(Integer currentPage, Integer pageSize) {
        //获取热门歌手二十个
        LinkedList<Singer> singers = singerMapper.querySingerTop20(singerMapper.querySingerTop20OfId());
        LinkedList<Singer> singerOfPart = new LinkedList<>();
        for(int i = (currentPage - 1)*pageSize;i < currentPage*pageSize;i++){
            singerOfPart.add(singers.get(i));
        }
        PageVo<Singer> pageVo = new PageVo<>();
        pageVo.setTotal((long) singers.size());
        pageVo.setSize(pageSize);
        pageVo.setDataList(singerOfPart);
        pageVo.setCurrent(currentPage);
        return pageVo;
    }
}
