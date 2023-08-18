package com.team.backend.controller.team.info;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.team.info.UpdateTeamServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateTeamInfoController {
    private static class updateTeamInfo{
        private int id;

        public int getId() {
            return id;
        }

        public String getTeamname() {
            return teamname;
        }

        public String getDescription() {
            return description;
        }

        public String getPerformance() {
            return performance;
        }

        public String getTask() {
            return task;
        }

        private String teamname;
        private String description;
        private String performance;
        private String task;
    }

    @Autowired
    UpdateTeamServiceImpl updateTeamService;

    @PostMapping(value = "/v1/team/info/updateinfo/",consumes="application/json")
    public Result updateInfo(@RequestBody updateTeamInfo info){
        return updateTeamService.updateTeamInfo(
                info.getId(),
                info.getTeamname(),
                info.getDescription(),
                info.getPerformance(),
                info.getTask()
        );
    }
}
