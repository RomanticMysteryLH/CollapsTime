package henu.javaweb.collapstime.service.Impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import henu.javaweb.collapstime.mapper.UserMapper;
import henu.javaweb.collapstime.model.PageVo;
import henu.javaweb.collapstime.model.User;
import henu.javaweb.collapstime.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public PageVo<User> userPage(Integer currentPage, Integer pageSize) {
        PageVo<User> userVo = new PageVo<>();
        IPage<User> page = new Page<>(currentPage, pageSize);
        userMapper.selectPage(page,null);
        userVo.setTotal(page.getTotal());
        userVo.setCurrent(currentPage);
        userVo.setDataList(page.getRecords());
        userVo.setSize(pageSize);
        return userVo;
    }

    @Override
    public Integer userDelete(String userId) {
        return userMapper.deleteById(userId);
    }

    @Override
    public User queryUser(String id) {
        return userMapper.selectById(id);
    }
}
