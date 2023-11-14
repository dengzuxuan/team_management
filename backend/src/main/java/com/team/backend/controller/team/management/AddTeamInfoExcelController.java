package com.team.backend.controller.team.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.team.management.AddTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName AddTeamInfoExcelController
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/14 20:38
 * @Version 1.0
 */
@RestController
public class AddTeamInfoExcelController {
    @Autowired
    AddTeamService addTeamService;
    @PostMapping("/v1/team/management/exceladd/")
    Result addTeamInfoExcel(@RequestParam("file") MultipartFile file){
        return addTeamService.addTeamExcelService(file);
    }

}
