package henu.javaweb.collapstime.controller;

import henu.javaweb.collapstime.mapper.SingerMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SingerService;
import henu.javaweb.collapstime.utils.Cons;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Controller
@RequestMapping("/singer")
public class SingerController {
    @Autowired
    private SingerService singerService;
    @Autowired
    private SingerMapper singerMapper;

    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\singerPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/singerPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/singerPic/");
            }
        }
    }

    /**
     * 实现歌手分页以及根据歌手首字母分页以及根据歌手性别查询
     * @param current
     * @param size
     * @param initial
     * @param category
     * @return
     */
    @GetMapping("/singerPage")
    @ResponseBody
    public PageVo<Singer> queryAllSinger(Integer current, Integer size,String initial,String category){
        if(initial == null && category == null){
            return singerService.singerPage(current,size);
        }else if(initial != null && category == null){
            return singerService.singerPageByInitial(current, size,initial);
        }else if(initial == null && category != null){
            Integer sex = -1;
            if("男歌手".equals(category)){
                sex = 1;
            }else if ("女歌手".equals(category)){
                sex = 0;
            }else{
                sex = 2;
            }
            return singerService.singerPageByCategory(current, size,sex);
        }else {
            Integer sex = -1;
            if("男歌手".equals(category)){
                sex = 1;
            }else if ("女歌手".equals(category)){
                sex = 0;
            }else{
                sex = 2;
            }
            return singerService.singerPageByInitialAndCategory(current, size, initial,sex);
        }

    }

    /**
     * 获取歌手详情页所有信息
     * @param singerId
     * @return
     */
    @GetMapping("/getSingerDetail")
    @ResponseBody
    public SingerDetail getSingerDetail(Integer singerId,Integer userId){
        return singerService.getSingerDetail(singerId,userId);
    }
    /**
     * 关注歌手
     * @param collect
     * @return
     */
    @PostMapping("/followSinger")
    @ResponseBody
    public CollectResult followSinger(Collect collect){
        CollectResult followResult = new CollectResult();
        if(collect.getFlag() == 1){
            return dismissFollowSinger(collect);
        }
        if(singerService.followSinger(collect) > 0){
            int collectSingerCount = singerMapper.getCollectSingerCount(collect.getSingerId());
            followResult.setCollectCount(collectSingerCount);
            followResult.setStatus("success");
            followResult.setMsg("关注成功");
        }else {
            followResult.setStatus("fail");
            followResult.setMsg("关注失败");
        }
        return followResult;
    }

    /**
     * 取关歌手
     * @param collect
     * @return
     */
    @PostMapping("/dismissFollowSinger")
    @ResponseBody
    public CollectResult dismissFollowSinger(Collect collect){
        CollectResult followResult = new CollectResult();
        if(singerMapper.dismissFollowSinger(collect) > 0){
            int collectSingerCount = singerMapper.getCollectSingerCount(collect.getSingerId());
            followResult.setCollectCount(collectSingerCount);
            followResult.setStatus("success");
            followResult.setMsg("取关成功");
        }else {
            followResult.setStatus("fail");
            followResult.setMsg("出错了");
        }
        return followResult;
    }

    /**
     * 获取热门歌手前二十
     * @param current
     * @param size
     * @return
     */
    @GetMapping("/getHotSinger")
    @ResponseBody
    public PageVo<Singer> getHotSinger(Integer current,Integer size){
        return singerService.singerPageByCollections(current, size);
    }

}
