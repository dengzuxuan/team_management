package com.team.backend.controller.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.service.user.account.GetListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

@RestController
public class GetList {
    @Autowired
    GetListService getListService;

    @GetMapping("/v1/user/account/getlist/")
    public Result getList(@RequestParam Map<String,String> map){
        String range = map.get("range");
        int pageNum = parseInt(map.get("pageNum")) ;
        int pageSize = parseInt(map.get("pageSize"));
        return getListService.getList(range,pageNum,pageSize);
    }
}
