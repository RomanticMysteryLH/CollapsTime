package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.mapper.SongListMapper;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.mapper.UserMapper;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.SongShowInList;
import henu.javaweb.collapstime.model.User;
import henu.javaweb.collapstime.service.UserService;
import henu.javaweb.collapstime.utils.Cons;
import henu.javaweb.collapstime.utils.MusicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SongListMapper songListMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private SingerMapper singerMapper;

    @Override
    public int register(User user) {
        int i = userMapper.insertSelective(user);
        return i;
    }

    @Override
    public boolean accountDetection(String account) {
        //一开始设定没有账号重复
        boolean flag = true;
        LinkedList<User> users = userMapper.queryAllUser();
        for (User user : users) {
            if(account.equals(user.getAccount())){
                //如果有该账号存在修改flag的值
                flag = false;
                break;
            }
        }
        return flag;
    }

    @Override
    public User selectByAccount(String account) {
        User user = userMapper.selectByAccount(account);
        return user;
    }


    @Override
    public int updateUser(User user) {
        int i = userMapper.updateUserdata(user);
        return i;
    }

    /**
     * 根据关键字进行全局搜索
     * @param key
     * @return
     */
    @Override
    public HashMap<String, Object> search(String key) {
        HashMap<String,Object> result = new HashMap<>();
        LinkedList<HashMap<String, Object>> singers = userMapper.searchSinger(key);
        LinkedList<HashMap<String, Object>> songs = userMapper.searchSong(key);
        LinkedList<HashMap<String, Object>> songLists = userMapper.searchSongList(key);
        result.put("songs",songs);
        result.put("singers",singers);
        result.put("songLists",songLists);
        return result;
    }

    /**
     * 输入歌曲名称显示与该歌曲相同类型的歌曲
     * @param key
     * @return
     */
    @Override
    public HashMap<String, Object> similarSearch(String key) {
        HashMap<String,Object> result = new HashMap<>();
        LinkedList<HashMap<String,Object>> songs = new LinkedList<>();
        //根据歌曲名称获取所属歌单id
        Integer songListId = userMapper.getSongListIdBySongName(key);
        //根据歌单id获取歌单类型
        String type = userMapper.getSongListTypeById(songListId);
        //根据歌单类型获取歌单id
        LinkedList<Integer> songListIds = userMapper.getSongListIdByStyle(type);
        LinkedList<Integer> ids = new LinkedList<>();
        //根据歌单id获取歌曲
        for(int i = 0;i < songListIds.size();i++)
        {
            LinkedList<Song> song = songListMapper.getSongOfSongList(songListIds.get(i));
            for(int j = 0;j < song.size();j++) {
                if(!ids.contains(song.get(j).getId()))
                {
                    ids.add(song.get(j).getId());
                    HashMap<String,Object> baseInfo = new HashMap<>();
                    baseInfo.put("id", song.get(j).getId());
                    baseInfo.put("name", song.get(j).getName());
                    songs.add(baseInfo);
                }
            }
        }
        result.put("songs",songs);
        return result;
    }

    @Override
    public PageVo<SongShowInList> similarSearchInfo(Integer current, Integer size, String key, Integer userId) {
        //根据歌曲名称获取所属歌单id
        Integer songListId = userMapper.getSongListIdBySongName(key);
        //根据歌单id获取歌单类型
        String type = userMapper.getSongListTypeById(songListId);
        //根据歌单类型获取歌单id
        LinkedList<Integer> songListIds = userMapper.getSongListIdByStyle(type);
        LinkedList<Integer> ids = new LinkedList<>();
        //根据歌单id获取歌曲
        for(int i = 0;i < songListIds.size();i++) {
            LinkedList<Song> song = songListMapper.getSongOfSongList(songListIds.get(i));
            for (int j = 0; j < song.size(); j++) {
                if (!ids.contains(song.get(j).getId())) {
                    ids.add(song.get(j).getId());
                }
            }
        }
        LinkedList<Song> songs = new LinkedList<>();
        for(int i = 0;i < ids.size();i++)
        {
            songs.add(songMapper.getSongById(ids.get(i)));
        }

        LinkedList<SongShowInList> list = new LinkedList<>();
        String os = System.getProperty("os.name");
        LinkedList<Integer> songOfUserCollect = songMapper.getSongOfUserCollect(userId);
        int end = 0;
        if((current*size) > songs.size())
        {
            end = songs.size();
        }else {
            end = current*size;
        }
        for(int i = (current-1)*size;i < end; i++){
            String path = null;
            if (os.toLowerCase().startsWith("win")){
                path = Cons.RESOURCE_WIN_PATH;
            }else {
                path = Cons.RESOURCE_MAC_PATH;
            }
            SongShowInList songShowInList = new SongShowInList();
            Integer collectStatus = 0;
            for (int j = 0; j < songOfUserCollect.size(); j++) {
                if(songs.get(i).getId().equals(songOfUserCollect.get(j))){
                    collectStatus = 1;
                    break;
                }else {
                    continue;
                }
            }
            songShowInList.setCollectStatus(collectStatus);
            songShowInList.setUrl(songs.get(i).getUrl());
            songShowInList.setCover(songs.get(i).getPicture());
            songShowInList.setSingerId(songs.get(i).getSingerId());
            songShowInList.setId(songs.get(i).getId());
            songShowInList.setIntroduction(songs.get(i).getIntroduction());
            songShowInList.setLyric(songs.get(i).getLyric());
            songShowInList.setName(songs.get(i).getName().split("-")[1]);
            songShowInList.setSingerName(singerMapper.getSingerNameById(songs.get(i).getSingerId()));
            path+=songs.get(i).getUrl();
            songShowInList.setDuration(MusicUtils.getDuration(path));
            list.add(songShowInList);
        }
        PageVo<SongShowInList> songShowInListPageVo = new PageVo<>();
        songShowInListPageVo.setTotal((long) songs.size());
        songShowInListPageVo.setCurrent(current);
        songShowInListPageVo.setSize(size);
        songShowInListPageVo.setDataList(list);
        return songShowInListPageVo;
    }


}
