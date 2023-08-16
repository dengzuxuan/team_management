package com.team.backend.controller.user.management;

import com.team.backend.config.result.Result;
import com.team.backend.service.user.management.UpdateLeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateLeaderController {
    @Autowired
    UpdateLeaderService updateLeaderService;

    @PostMapping("/v1/user/management/updateleader/")
    public Result UpdateLeader(@RequestParam Map<String,String>m1){
        String oldStudentNo = m1.get("oldStudentNo");
        String newStudentNo = m1.get("newStudentNo");

        return updateLeaderService.updateLeader(oldStudentNo,newStudentNo);
    }
}
