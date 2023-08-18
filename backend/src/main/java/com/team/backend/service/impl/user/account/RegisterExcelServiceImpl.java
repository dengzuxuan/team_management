package com.team.backend.service.impl.user.account;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.aliyuncs.utils.IOUtils;
import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.RegisterExcelService;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@Service
public class RegisterExcelServiceImpl implements RegisterExcelService {
    @Data
    public class RegisterDto {
        @ExcelProperty(index = 0)
        private String no;

        @ExcelProperty(index = 1)
        private String studentNo;

        @ExcelProperty(index = 2)
        private String username;

    }
    @Override
    public Result registerExcel(MultipartFile file) {
        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, RegisterDto.class, new ReadListener<RegisterDto>() {

                @Override
                public void onException(Exception e, AnalysisContext analysisContext) throws Exception {

                }

                @Override
                public void invokeHead(Map<Integer, CellData> map, AnalysisContext analysisContext) {

                }

                @Override
                public void invoke(RegisterDto registerDto, AnalysisContext analysisContext) {
                    System.out.println(registerDto.getNo());
                    System.out.println(registerDto.getUsername());
                    System.out.println(registerDto.getStudentNo());
                }

                @Override
                public void extra(CellExtra cellExtra, AnalysisContext analysisContext) {

                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {

                }

                @Override
                public boolean hasNext(AnalysisContext analysisContext) {
                    return false;
                }
            }).sheet().doRead();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }

        System.out.println("@");
        return null;
    }

}
