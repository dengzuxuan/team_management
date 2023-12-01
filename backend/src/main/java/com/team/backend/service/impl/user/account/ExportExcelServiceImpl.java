package com.team.backend.service.impl.user.account;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.dto.excel.UserType;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.ExportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ExportExcelServiceImpl
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/28 22:00
 * @Version 1.0
 */
@Service
public class ExportExcelServiceImpl implements ExportExcelService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Result exportExcel() throws IOException {
        String fileName =   "./成员信息"  + ".xlsx";
        EasyExcel.write(fileName, UserType.class).sheet("成员信息")
                .doWrite(usersdata());
        File file = new File(fileName);

        // 将Excel文件读取到字节数组中
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("attachment", "成员信息.xlsx");

        file.delete();
        return Result.success(ResponseEntity.ok().headers(headers).contentLength(bytes.length)
                .body(bytes));
    }

    private List<UserType> usersdata() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_no",user.getStudentNo());
        List<User> userList = userMapper.selectList(queryWrapper);

        List<UserType> usersdata = ListUtils.newArrayList();

        for (User userinfo:userList){
            UserType userType = new UserType(
                    userinfo.getStudentNo(),
                    userinfo.getUsername(),
                    userinfo.getPhone(),
                    userinfo.getEmail(),
                    userinfo.getCardNo()
            );
            usersdata.add(userType);
        }

        return usersdata;
    }
}
