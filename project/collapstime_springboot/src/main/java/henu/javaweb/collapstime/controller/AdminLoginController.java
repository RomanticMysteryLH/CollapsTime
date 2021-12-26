package henu.javaweb.collapstime.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminLoginController {
    @PostMapping("/login")
    @ResponseBody
    public Map login(String username,String password) {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, Object> responseData = new HashMap<>();
        if(username.equals("admin")&&password.equals("z666l666")){
            responseData.put("token","admin");
            response.put("msg","登录成功");
            response.put("data",responseData);
            return response;
        }else{
            responseData.put("token",null);
            response.put("msg","登录失败");
            response.put("data",responseData);
            return response;
        }
    }

    @GetMapping("info")
    @ResponseBody
    public Map info() {
        HashMap<String, Object> responseInfo = new HashMap<>();
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("roles","admin");
        responseInfo.put("msg","登录成功");
        responseInfo.put("data",responseData);
        return responseInfo;
    }
}
