package com.team.backend.service.impl.team.management;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyuncs.utils.IOUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.TeamInfoType;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.management.AddTeamService;
import com.team.backend.utils.common.excelType.TeamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.*;

@Service
public class AddTeamServiceImpl implements AddTeamService {
    @Autowired
    TeamInfoMapper teamInfoMapper;

    @Override
    public Result addTeamService(String leaderNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        Date now = new Date();
        TeamInfo teamInfo = new TeamInfo(null, adminUser.getStudentNo(), leaderNo,"", now, now);
        teamInfoMapper.insert(teamInfo);
        return Result.success(null);
    }

    @Override
    public Result addTeamMoreService(TeamInfoType[] teamInfos) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        Date now = new Date();
        for (TeamInfoType info:teamInfos ) {
            TeamInfo teamInfo = new TeamInfo(null, adminUser.getStudentNo(), null, info.getTeamName(), now, now);
            teamInfoMapper.insert(teamInfo);
        }

        return Result.success(null);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class TeamExcelInfo implements Serializable {
        private String id;
        private String teamName;
        private String failReason;
    }
    int totalCnt=0;
    ResultCodeEnum resultCodeEnum = ResultCodeEnum.SUCCESS;
    @Override
    public Result addTeamExcelService(MultipartFile file) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        List<TeamExcelInfo> correctTeams= new ArrayList<>();
        List<TeamExcelInfo> wrongTeams= new ArrayList<>();
        InputStream inputStream =null;

        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, TeamType.class, new PageReadListener<TeamType>(dataList -> {
                if(dataList==null){
                    resultCodeEnum=ResultCodeEnum.FILE_WRONG_EMPTY;
                }

                //总长度
                totalCnt = dataList.size();
                List<TeamInfo> teamInfos = teamInfoMapper.selectList(null);
                int idInt = teamInfos.get(teamInfos.size()-1).getId() + 1;
                //重复名称
                Set<String> teamNameSet = new HashSet<>();

                for (TeamType demoData : dataList) {
                    String teamname = demoData.getTypename();

                    TeamExcelInfo teamExcelInfo = new TeamExcelInfo();
                    teamExcelInfo.setTeamName(demoData.getTypename());

                    if(teamname==null){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NAME_NOT_EMPTY.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }
                    if(teamname.length()>20){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NAME_WRONG.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }

                    QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("teamname",teamname);
                    if(teamNameSet.contains(teamname)){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NAME_ALRADY_EXIST_BEFORE.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }
                    teamNameSet.add(teamname);
                    TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper);
                    if(teamInfoFind!=null){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NAME_ALRADY_EXIST.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }
                    teamExcelInfo.setId(getFillId(idInt));

                    idInt = idInt+1;

                    correctTeams.add(teamExcelInfo);
                }
            })).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            IOUtils.closeQuietly(inputStream);
        }
        Map<String,Object> res = new HashMap<>();
        res.put("totalCnt",totalCnt);
        res.put("corrcetCnt",correctTeams.size());
        res.put("wrongCnt",wrongTeams.size());

        res.put("correctTeams",correctTeams);
        res.put("wrongTeams",wrongTeams);

        return Result.success(res);
    }

    private String getFillId(int id){
        StringBuilder idStr = new StringBuilder(String.valueOf(id));

        for(int i = idStr.length();i<6;i++){
            idStr.insert(0, "0");
        }
        return String.valueOf(idStr);
    }
}
