package com.team.backend.controller.team.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.TeamInfoType;
import com.team.backend.service.team.management.AddTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AddTeamInfoController
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/14 20:38
 * @Version 1.0
 */
@RestController
public class AddTeamInfoController {
    @Autowired
    AddTeamService addTeamService;
    @PostMapping(value="/v1/team/management/moreadd/",consumes="application/json")
    public Result addTeamInfoMore(@RequestBody TeamInfoType[] teamInfoTypes){
        return addTeamService.addTeamMoreService(teamInfoTypes);
    }
}
