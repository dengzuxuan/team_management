package com.team.backend.service.impl.user.account;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyuncs.utils.IOUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.user.account.RegisterExcelService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegisterExcelServiceImpl implements RegisterExcelService {

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class RegisterUser implements Serializable {
        private String studentNo;
        private String password;
        private String username;
        private String failReason;
    }
    @Autowired
    UserMapper userMapper;
    int totalCnt=0;
    ResultCodeEnum resultCodeEnum = ResultCodeEnum.SUCCESS;
    @Override
    public Result registerExcel(MultipartFile file) {
        List<RegisterUser> correctUsers= new ArrayList<>();
        List<RegisterUser> wrongUsers= new ArrayList<>();
        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, DemoData.class, new PageReadListener<DemoData>(dataList -> {
                if(dataList==null){
                    resultCodeEnum=ResultCodeEnum.FILE_WRONG_EMPTY;
                }

                totalCnt = dataList.size();

                for (DemoData demoData : dataList) {
                    RegisterUser user = new RegisterUser();
                    user.setUsername(demoData.getUsername());
                    user.setStudentNo(demoData.getStudentNo());

                    if(demoData.getStudentNo()==null || demoData.getUsername()==null || demoData.getStudentNo().length()<3){
                        user.failReason=ResultCodeEnum.FILE_WRONG_EMPTY_SINGLE.getMessage();
                        wrongUsers.add(user);
                        continue;
                    }

                    user.setPassword(demoData.getStudentNo().substring(2));

                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("student_no",demoData.getStudentNo());
                    User userCheck = userMapper.selectOne(queryWrapper);
                    if(userCheck!=null){
                        user.failReason=ResultCodeEnum.FILE_WRONG_REPEAT.getMessage();
                        wrongUsers.add(user);
                        continue;
                    }

                    correctUsers.add(user);
                }
            })).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        Map<String,Object> res = new HashMap<>();
        res.put("totalCnt",totalCnt);
        res.put("corrcetCnt",correctUsers.size());
        res.put("wrongCnt",wrongUsers.size());

        res.put("correctUsers",correctUsers);
        res.put("wrongUsers",wrongUsers);

        return Result.build(res,resultCodeEnum);
    }

}
