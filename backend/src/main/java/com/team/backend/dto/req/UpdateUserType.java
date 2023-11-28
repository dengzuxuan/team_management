package com.team.backend.dto.req;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * @ClassName UpdateUserType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:11
 * @Version 1.0
 */
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserType {
        private int id;
        private String username;
        private String studentNo;
        private String tel;
        private String email;
        private String cardNo;
        private String teamNo;
        private String teamName;
        private String Role;
}
