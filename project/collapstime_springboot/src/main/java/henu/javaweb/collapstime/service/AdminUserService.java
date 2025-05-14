package henu.javaweb.collapstime.service;

import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.User;


public interface AdminUserService {
    /**
     * 分页获取用户列表
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageVo<User> userPage(Integer currentPage, Integer pageSize);

    /**
     * 从数据库中删除用户
     * @param userId
     * @return
     */
    public Integer userDelete(String userId);

    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    public User queryUser(String id);
}
