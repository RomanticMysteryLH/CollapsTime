package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.User;
import org.springframework.stereotype.Service;


public interface AdminUserService {
    public PageVo<User> userPage(Integer currentPage, Integer pageSize);
}
