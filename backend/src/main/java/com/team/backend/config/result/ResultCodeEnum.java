package com.team.backend.config.result;

import lombok.Getter;
/**
 * 统一返回结果状态信息类
 *
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201, "失败"),

    USER_LOGIN_WRONG(2000,"用户登陆异常"),
    USER_NOT_EXIST(2001,"用户不存在"),

    USER_PASSWORD_WRONG(2002,"用户密码错误"),
    USER_NAME_NOT_EMPTY(2003,"用户不能为空"),
    USER_NAME_ALREADY_EXIST(2004,"用户已存在"),
    USER_NAME_NOT_EXIST(2005,"该用户不存在"),
    PASSWORD_NOT_EMPTY(2006,"密码不能为空"),
    PASSWORD_NOT_EQUAL(2007,"两次密码输入不等"),
    PASSWORD_PARAM_WRONG(2008,"密码格式有误"),
    ROLE_PARAM_WRONG(2009,"角色参数有误"),

    ROLE_AUTHORIZATION_NOT_ENOUGHT(2010,"角色权限不足"),
    USER_ALREADY_IN_TEAM(2011,"该用户已加入小组，无法设为组长"),
    USER_NOT_IN_TEAM(2012,"该用户目前未加入小组"),
    USER_IS_LEADER(2012,"该用户是组长，无法直接移除"),
    USER_CHANGE_WRONG(2013,"输入用户有误，无法实现组长切换"),
    USER_ADMIN_WRONG(2013,"管理员无法对非自己所属组成员进行操作"),
    USER_ROLE_WRONG(2014,"输入用户角色有误"),
    TEAM_NOT_EXIST(2015,"该小组不存在"),
    PARAMS_WRONG(2016,"请输入正确参数"),
    TEAM_NAME_NOT_EMPTY(2017,"小组名称不能为空"),
    TEAM_NAME_WRONG(2018,"小组名称格式有误"),
    TEAM_NAME_ALRADY_EXIST(2019,"小组名称已存在"),
    TEAM_NO_NOT_EMPTY(2020,"小组编号不能为空"),
    TEAM_NO_WRONG(2021,"小组编号格式有误"),
    TEAM_NO_ALRADY_EXIST(2022,"小组编号已存在"),
    TEAM_NAME_ALRADY_EXIST_BEFORE(2023,"当前excel中已存在该小组名称"),
    TEAM_NO_ALRADY_EXIST_BEFORE(2024,"当前excel中已存在该小组编号"),

    USER_EXPORY_WRONG(2025,"用户信息导出失败"),

    INPUT_PARAM_WRONG(2101,"输入参数有误"),
    INPUT_EMAIL_PARAM_WRONG(2102,"输入email格式有误"),
    INPUT_TEL_PARAM_WRONG(2101,"输入电话格式有误"),
    INPUT_STUDENTNO_PARAM_WRONG(2103,"输入用户学号有误"),
    INPUT_USRNAME_PARAM_WRONG(2104,"输入用户名称有误"),
    INPUT_CARDNO_PARAM_WRONG(2105,"输入身份证号格式有误"),
    INPUT_VAR_WRONG(2106,"输入的小组编号 小组名称以及角色应同时为空或同时不为空"),
    INPUT_TEAM_NO_NOT_EXIST(2107,"该小组编号不存在"),
    INPUT_TEAM_NAME_CANT_MATCH(2108,"小组编号与小组名称对应错误"),
    INPUT_LEADER_ALREAY_EXIST(2109,"该小组已存在组长"),
    INPUT_ROLE_WRONG(2110,"输入角色有误"),


    FILE_WRONG_EMPTY(2201,"文件无法正常读取或文件为空，请按格式填写"),
    FILE_WRONG_STUDENTNO_REPEAT(2202,"文件中有学号已经存在平台中"),
    FILE_WRONG_LEADER_REPEAT(2203,"文件中该小组组长已经存在"),
    FILE_WRONG_EMPTY_SINGLE(2203,"文件中有部分数据不全"),
    INPUT_STUDENTNO_IS_NULL(2204,"输入用户学号为空"),
    INPUT_USERNAME_IS_NULL(2205,"输入用户名称为空"),
    USER_CANT_UPDATE_REPORT(2206,"无法修改他人周报"),


    EQUIPMENT_ALREAY_EXIST(2301,"该设备已经存在"),
    EQUIPMENT_SERIALNUMBER_PARAM_WRONG(2302,"设备序列号参数有误"),
    EQUIPMENT_NAME_PARAM_WRONG(2303,"设备名称参数有误"),
    EQUIPMENT_VERSION_PARAM_WRONG(2304,"设备型号参数有误"),
    EQUIPMENT_PERFORMANCEINDEX_PARAM_WRONG(2305,"设备性能指标参数有误"),
    EQUIPMENT_ADDRESS_PARAM_WRONG(2306,"设备存放地参数有误"),
    EQUIPMENT_WAREHOUSEENTRYTIME_PARAM_WRONG(2307,"设备入库时间参数有误"),
    EQUIPMENT_HOSTREMARKS_PARAM_WRONG(2308,"设备主机备注参数有误"),
    EQUIPMENT_REMARK_PARAM_WRONG(2309,"设备备注参数有误"),
    EQUIPMENT_ORIGINAL_VALUE_PARAM_WRONG(2310,"设备原值参数有误"),
    EQUIPMENT_NOT_EXIST(2311,"该设备不存在"),

    EQUIPMENT_ERCORD_NOT_REPEAT(2312,"无法多次申请同一设备，请先撤销原申请"),
    EQUIPMENT_ERCORD_NOT_EXIST(2313,"该记录不存在"),
    EQUIPMENT_ERCORD_CANT_RECOVER(2314,"无法撤销未处于正常使用状态的记录"),

    REPORT_NOT_EXIST(2315,"周报不存在"),
    REPORT_COMMENT_NOT_ADMIN(2316,"管理员无法评论非本组成员周报"),
    REPORT_COMMENT_NOT_LEADER(2317,"组长无法评论非本组成员周报"),
    REPORT_COMMENT_NOT_MEMBER(2318,"组员无法评论非自己周报"),
    REPORT_COMMENT_USER_WRONG(2319,"只可以撤回自己评论"),
    REPORT_CANT_UPDATE_HERE(2320,"周报已提交过，如要修改，请进入历史周报的修改功能进行修改"),


    RECOVER_FILE_WRONG(2401,"恢复数据失败"),
    RECOVER_FILE_NOT_EXIT(2402,"指定备份数据不存在"),
    BACKUP_FILE_WRONG(2403,"备份数据失败"),
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
