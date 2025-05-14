package henu.javaweb.collapstime.controller;

import com.auth0.jwt.interfaces.Claim;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import henu.javaweb.collapstime.mapper.SongMapper;
import henu.javaweb.collapstime.mapper.UserMapper;
import henu.javaweb.collapstime.model.*;
import henu.javaweb.collapstime.service.SingerService;
import henu.javaweb.collapstime.service.SongListService;
import henu.javaweb.collapstime.service.SongService;
import henu.javaweb.collapstime.service.UserService;
import henu.javaweb.collapstime.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

/**
 * 用户的相关操作
 */
@Controller
@RequestMapping("/user")
public class UserController {



    @Configuration
    public class MyConfig implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            String os = System.getProperty("os.name");
            if (os.toLowerCase().startsWith("win")) { // windos系统
                registry.addResourceHandler("/img/userPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_WIN_PATH + "\\img\\userPic\\");
            } else { // MAC、Linux系统
                registry.addResourceHandler("/img/userPic/**")
                        .addResourceLocations("file:" + Cons.RESOURCE_MAC_PATH + "/img/userPic/");
            }
        }
    }


    @Autowired
    private UserService userService;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SongService songService;
    @Autowired
    private SongListService songListService;
    @Autowired
    private SingerService singerService;

    /**
     * 注册
     * @param username
     * @param password
     * @param account
     * @param email
     * @param sex
     */
    @PostMapping("/register")
    @ResponseBody
    public String register(String username, String password, String account, String email, String sex, String location) {
//        String avator = "assets/user/default.jpg";
        Byte s = 0;
        if(sex.equals("男")){
            s = 1;
        }else {
            s = 0;
        }
        User user = new User(username, account, Md5Utils.code(password),s,location,email);
        int register = userService.register(user);
        if(register > 0){
            return "success";
        }else {
            return "fail";
        }
    }

    /**
     * 检测用户账号是否重复
     * @param account
     * @return
     */
    @PostMapping("/accountDetection")
    @ResponseBody
    public String accountDetection(String account){
        boolean b = userService.accountDetection(account);
        if(b){
            return "ok";
        }else {
            return "exist";
        }
    }

    /**
     * 登录
     * @param account 账号
     * @param password  密码
     * @param code 验证码
     * @return
     */
    @PostMapping("/login")
    @ResponseBody
    public HashMap<String,Object> login(String account, String password, String code, HttpServletRequest request){
        String generateCode = (String) request.getSession().getAttribute("code");
        //首先校验验证码，如果验证码错误则返回"验证码错误"，如果验证码正确且密码正确则返回"登录成功",否则返回"账号或者密码错误";
        HashMap<String,Object> result = new HashMap<>();
        HashMap<String,String> payload = new HashMap<>();
        if(generateCode.equals(code)){
            User user = userService.selectByAccount(account);
            if (user == null) {
                result.put(Cons.state,"账号或者密码错误");
                return result;
            }else {
                if(Md5Utils.code(password).equals(user.getPassword())){
                    result.put(Cons.username,user.getUsername());
                    result.put(Cons.account,user.getAccount());
                    result.put(Cons.avator,user.getAvator());
                    result.put(Cons.id,user.getId());
                    result.put(Cons.state,"登录成功");
                    payload.put(Cons.account,user.getAccount());
                    payload.put(Cons.id,user.getId().toString());
                    payload.put(Cons.username,user.getUsername());
                    payload.put(Cons.avator,user.getAvator());
                    String token = JWTUtil.getToken(payload);
                    result.put(Cons.token,token);
                    //将用户登录账号存到session,用于以后登录检测
                    request.getSession().setAttribute(Cons.account,user.getAccount());
                    return result;
                }else {
                    result.put(Cons.state,"账号或者密码错误");
                    return result;
                    }
                }
            } else {
            result.put(Cons.state,"验证码错误");
            return result;
        }
    }

    /**
     * 获取登录时的验证码
     * @param session
     * @param response
     * @throws IOException
     */
    @GetMapping(path = "/getImage")
    @ResponseBody
    public void getImage(HttpSession session, HttpServletResponse response) throws IOException {
        //生成验证码
        String securityCode = VerifyCodeUtils.getSecurityCode();
        //将验证码放入session
        session.setAttribute("code",securityCode);
        //生成图片
        BufferedImage image = VerifyCodeUtils.createImage(securityCode);
        //输出图片
        ServletOutputStream outputStream = response.getOutputStream();
        //调用工具类
        ImageIO.write(image,"png",outputStream);
    }


    /**
     * 更新用户数据
     */
    @PostMapping("/updatelist")
    @ResponseBody
    public String updateUser(User user) throws IOException{
        int i = userService.updateUser(user);
        if(i > 0)
        {
            return "success";
        }else {
            return "info";
        }
    }


    @GetMapping("/downloadSong")
    @ResponseBody
    public void downloadSong(Integer songId, HttpServletResponse response){
        String fileName = songMapper.getSongById(songId).getName().split("-")[1];// 文件名
        fileName+=".mp3";
        String songPath = songMapper.getSongById(songId).getUrl();//歌曲相对路径
        SongDownloadResult songDownloadResult = new SongDownloadResult();
        //设置文件路径
        String os = System.getProperty("os.name");
        String path = null;
        if (os.toLowerCase().startsWith("win")){
            path = Cons.RESOURCE_WIN_PATH;
        }else {
            path = Cons.RESOURCE_MAC_PATH;
        }
        path+=songPath;
        try {
            // path是指想要下载的文件的路径
            File file = new File(path);
            // 将文件写入输入流
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStream fis = new BufferedInputStream(fileInputStream);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            //Content-Disposition的作用：告知浏览器以何种方式显示响应返回的文件，用浏览器打开还是以附件的形式下载到本地保存
            //attachment表示以附件方式下载 inline表示在线打开 "Content-Disposition: inline; filename=文件名.mp3"
            // filename表示文件的默认名称，因为网络传输只支持URL编码的相关支付，因此需要将文件名URL编码后进行传输,前端收到后需要反编码才能获取到真正的名称
            response.setContentType("application/octet-stream;charset=utf-8");
            response.addHeader("Content-Length", "" + file.length());
            response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            outputStream.write(buffer);
            outputStream.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 用户搜索推荐信息
     * @param key
     * @return
     */
    @PostMapping("/search")
    @ResponseBody
    public HashMap<String, Object> search(String key){
        return userService.search(key);
    }

    /**
     * 输入歌曲名称显示与该歌曲相同类型的歌曲
     * @param key
     * @return
     */
    @PostMapping("/similarSearch")
    @ResponseBody
    public HashMap<String,Object> similarSearch(String key){return userService.similarSearch(key);}
    /**
     * 根据id获取用户信息
     * @param account
     * @return
     */
    @PostMapping("/getInfo")
    @ResponseBody
    public User getInfo(String account){
        return userMapper.selectByAccount(account);
    }

    /**
     * 搜索歌单歌曲歌手
     * @param key
     * @param type 搜索的是歌曲歌手还是歌单
     * @param current
     * @param size
     * @param userId
     *  type
     * {
     *    singer
     *    song
     *    songList
     * }
     * @return
     */
    @PostMapping("/searchInfo")
    @ResponseBody
    public Object searchInfo(Integer current,Integer size,String key,String type,Integer userId){
        if("song".equals(type)){
            return songService.searchSongInfo(current, size, key, userId);
        }else if("songList".equals(type)){
            return songListService.searchSongListInfo(current, size, key);
        }else {
            return singerService.searchSingerInfo(current, size, key);
        }
    }
    @PostMapping("/similarSearchInfo")
    @ResponseBody
    public Object similarSearchInfo(Integer current,Integer size,String key,Integer userId)
    {
        return userService.similarSearchInfo(current, size, key, userId);
    }
    /**
     * 验证token
     * @param request
     * @return
     * /user/verifyToken
     */
    @PostMapping("/verifyToken")
    @ResponseBody
    public HashMap<String,Object> verifyToken(HttpServletRequest request){
        String token = request.getHeader("token");
        Map<String, Claim> payloadFromToken = JWTUtil.getPayloadFromToken(token);
        HashMap<String, Object> map = new HashMap<>();
        String account = payloadFromToken.get(Cons.account).asString();
        User user = userMapper.queryUserByAccount(account);
        map.put("userId",user.getId());
        map.put("account",account);
        map.put("avator",user.getAvator());
        map.put("username",user.getUsername());
        map.put("status","success");
        return map;
    }

    /**
     * 更新用户基本信息
     * @param user
     * @return
     */
    @PostMapping("/updateUserBaseInfo")
    @ResponseBody
    public HashMap<String,String> updateUserBaseInfo(User user){
        HashMap<String, String> map = new HashMap<>();
        int i = userMapper.updateUserBaseInfo(user);
        if(i > 0){
            map.put("status","success");
            map.put("msg","更新成功");
        }else {
            map.put("status","fail");
            map.put("msg", "出错了");
        }
        return map;
    }

    /**
     * 用户修改密码
     * @param account
     * @param password
     * @return
     */
    @PostMapping("/updatePwd")
    @ResponseBody
    public HashMap<String,String> updatePwd(String account,String password){
        HashMap<String, String> map = new HashMap<>();
        int i = userMapper.updatePwd(account, Md5Utils.code(password));
        if(i > 0){
            map.put("status","success");
            map.put("msg","修改成功");
        }else {
            map.put("status","fail");
            map.put("msg", "出错了");
        }
        return map;
    }

    /**
     * 修改用户头像
     * @param account
     * @param filePath
     * @return
     */
    @PostMapping("/updateAvator")
    @ResponseBody
    public HashMap<String,String> updateAvator(String account,String filePath){
        String os = System.getProperty("os.name");
        String path = null;
        if (os.toLowerCase().startsWith("win")){
            path = Cons.RESOURCE_WIN_PATH;
        }else {
            path = Cons.RESOURCE_MAC_PATH;
        }
        path += userMapper.queryUserAvatorOfPast(account);
        FileHandleUtils.deleteFile(path);
        HashMap<String, String> map = new HashMap<>();
        int i = userMapper.updateAvator(account,filePath);
        if(i > 0){
            map.put("status","success");
            map.put("msg","修改成功");
        }else {
            map.put("status","fail");
            map.put("msg", "出错了");
        }
        return map;
    }

    /**
     * 注销用户
     * @param account
     * @return
     */
    @PostMapping("/deleteUser")
    @ResponseBody
    public HashMap<String,String> deleteUser(String account){
        HashMap<String, String> map = new HashMap<>();
        Integer i = userMapper.deleteUser(account);
        if(i > 0){
            map.put("status","success");
            map.put("msg","注销成功");
        }else {
            map.put("status","fail");
            map.put("msg", "出错了");
        }
        return map;
    }

}
