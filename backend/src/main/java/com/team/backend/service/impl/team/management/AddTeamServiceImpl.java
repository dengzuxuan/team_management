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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AddTeamServiceImpl implements AddTeamService {
    @Autowired
    TeamInfoMapper teamInfoMapper;

    @Override
    public Result addTeamService(TeamInfoType teamInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        ResultCodeEnum codeEnum = checkTeamInfo(teamInfo.getNo(), teamInfo.getTeamName(),adminUser.getStudentNo());
        if( codeEnum!= ResultCodeEnum.SUCCESS){
            return Result.build(null,codeEnum);
        }
        Date now = new Date();
        TeamInfo newTeam = new TeamInfo(null, teamInfo.getNo(),adminUser.getStudentNo(),null ,teamInfo.getTeamName(), now, now);
        teamInfoMapper.insert(newTeam);
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
            TeamInfo teamInfo = new TeamInfo(null,info.getNo() ,adminUser.getStudentNo(), null, info.getTeamName(), now, now);
            teamInfoMapper.insert(teamInfo);
        }

        return Result.success(null);
    }


    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    private static class TeamExcelInfo implements Serializable {
        private String no;
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
                //重复名称
                Set<String> teamNameSet = new HashSet<>();
                //重复编号
                Set<String> teamNoSet = new HashSet<>();

                for (TeamType data : dataList) {
                    TeamExcelInfo teamExcelInfo = new TeamExcelInfo();
                    teamExcelInfo.setTeamName(data.getTypename());
                    teamExcelInfo.setNo(data.getNo());

                    if(teamNameSet.contains(data.getTypename())){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NAME_ALRADY_EXIST_BEFORE.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }

                    if(teamNoSet.contains(data.getNo())){
                        teamExcelInfo.failReason=ResultCodeEnum.TEAM_NO_ALRADY_EXIST_BEFORE.getMessage();
                        wrongTeams.add(teamExcelInfo);
                        continue;
                    }

                    ResultCodeEnum codeEnum = checkTeamInfo(data.getNo(),data.getTypename(),adminUser.getStudentNo());
                    if(codeEnum == ResultCodeEnum.SUCCESS){
                        correctTeams.add(teamExcelInfo);
                        teamNoSet.add(data.getNo());
                        teamNameSet.add(data.getTypename());
                    }else{
                        teamExcelInfo.failReason=codeEnum.getMessage();
                        wrongTeams.add(teamExcelInfo);
                    }
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

    private ResultCodeEnum checkTeamInfo(String no,String typename,String studentno){
        if(no == null){
            return ResultCodeEnum.TEAM_NO_NOT_EMPTY;
        }
        if(typename == null){
            return ResultCodeEnum.TEAM_NAME_NOT_EMPTY;
        }
        String noRegex = "^[a-zA-Z0-9]{6}$";
        Pattern noPatten = Pattern.compile(noRegex);
        Matcher noMatcher = noPatten.matcher(no);
        if(!noMatcher.matches()){
            return ResultCodeEnum.TEAM_NO_WRONG;
        }

        if(typename.length() > 10){
            return ResultCodeEnum.TEAM_NAME_WRONG;
        }

        QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no",no).eq("admin_no",studentno);
        TeamInfo teamInfoNoFind = teamInfoMapper.selectOne(queryWrapper);
        if(teamInfoNoFind!=null){
            return ResultCodeEnum.TEAM_NO_ALRADY_EXIST;
        }

        QueryWrapper<TeamInfo> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("teamname",typename).eq("admin_no",studentno);
        TeamInfo teamInfoNameFind = teamInfoMapper.selectOne(queryWrapper2);
        if(teamInfoNameFind!=null){
            return ResultCodeEnum.TEAM_NAME_ALRADY_EXIST;
        }

        return ResultCodeEnum.SUCCESS;
    }
}
