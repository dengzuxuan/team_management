package com.team.backend.dto.resp;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.backend.pojo.User;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ReportCommentGroup{
    List<ReportCommentType> leaderGroupComments;
    List<ReportCommentType> adminGroupComments;
}