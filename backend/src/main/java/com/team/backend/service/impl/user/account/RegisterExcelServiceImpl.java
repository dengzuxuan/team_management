package com.team.backend.service.impl.user.account;

import cn.hutool.json.JSON;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyuncs.utils.IOUtils;
import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.RegisterExcelService;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@Service
public class RegisterExcelServiceImpl implements RegisterExcelService {
    @Override
    public Result registerExcel(MultipartFile file) {
        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, DemoData.class, new PageReadListener<DemoData>(dataList -> {
                for (DemoData demoData : dataList) {
                    System.out.println(demoData.getNo()+demoData.getStudentNo()+demoData.getUsername());
                }
            })).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }

        return null;
    }

}
