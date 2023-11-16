package com.team.backend.service.impl.report.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.resp.UserTimesInfo;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName getTimesInfo
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/16 11:42
 * @Version 1.0
 */
@Component
public class getTimesInfo {
    @Autowired
    UserMapper userMapper;

    public List<User> getUserList(User user,String No){

        List<User> userList = new ArrayList<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        if(user.getRole()==1){
            if(No!=null){
                if(No.length() == 6){
                    //表示是小组，而不是固定成员
                    queryWrapper.eq("team_no",No);
                }else{
                    //是固定学号
                    queryWrapper.eq("student_no",No);
                }
            }else{
                //如果传入学号为空的话 即查询所有的学生
                queryWrapper.eq("admin_no",user.getStudentNo());
            }
            userList = userMapper.selectList(queryWrapper);
        }else{
            if(No!=null){
                //是固定学号
                queryWrapper.eq("student_no",No);
            }else{
                //如果传入学号为空的话 即查询所有的学生
                queryWrapper.eq("leader_no",user.getStudentNo());
            }
            userList = userMapper.selectList(queryWrapper);
        }
        return userList;
    }

    public List getPageList(List totalList,int pageSize,int pageNum){
        int totalCount = totalList.size(); //总数量
        int pageCount = 0; //总页数
        List<Object> subyList = null;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        if (m == 0) {
            subyList = totalList.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
        } else {
            if (pageNum == pageCount) {
                subyList = totalList.subList((pageNum - 1) * pageSize, totalCount);
            }
            if (pageNum< pageCount){
                subyList = totalList.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
            }
        }
        if (pageNum > pageCount){
            subyList  = null;
        }
        return subyList;
    }
}
