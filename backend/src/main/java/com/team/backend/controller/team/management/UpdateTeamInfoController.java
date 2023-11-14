package com.team.backend.controller.team.management;


import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateTeamInfo;
import com.team.backend.service.impl.team.management.UpdateTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateTeamInfoController {

    @Autowired
    UpdateTeamServiceImpl updateTeamService;

    @PostMapping(value = "/v1/team/management/updateinfo/",consumes="application/json")
    public Result updateInfo(@RequestBody UpdateTeamInfo info){
        return updateTeamService.updateTeamInfo(
                info.getId(),
                info.getTeamname()
        );
    }
}
