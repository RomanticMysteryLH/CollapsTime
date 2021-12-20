package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.Song;
import henu.javaweb.collapstime.model.User;
import org.springframework.stereotype.Service;

import java.util.LinkedList;


public interface UserService {

    public int register(User user);
    public boolean accountDetection(String account);
    public User selectByAccount(String account);
    public int updateUser(User user);

}
