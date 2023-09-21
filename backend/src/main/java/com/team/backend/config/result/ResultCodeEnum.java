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
    INPUT_PARAM_WRONG(2101,"输入参数有误"),
    INPUT_EMAIL_PARAM_WRONG(2102,"输入email格式有误"),
    INPUT_PHONE_PARAM_WRONG(2101,"输入电话格式有误"),
    INPUT_STUDENTNO_PARAM_WRONG(2103,"输入用户学号有误"),
    INPUT_USRRNAME_PARAM_WRONG(2104,"输入用户名称有误"),
    FILE_WRONG_EMPTY(2201,"文件无法正常读取或文件为空，请按格式填写"),
    FILE_WRONG_REPEAT(2202,"文件中有学号已经存在平台中"),
    FILE_WRONG_EMPTY_SINGLE(2203,"文件中有部分数据不全"),
    INPUT_STUDENTNO_IS_NULL(2204,"输入用户学号为空"),
    INPUT_USERNAME_IS_NULL(2205,"输入用户名称为空"),


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
    SERVICE_ERROR(2012, "服务异常"),
    DATA_ERROR(204, "数据异常"),
    ILLEGAL_REQUEST(205, "非法请求"),
    REPEAT_SUBMIT(206, "重复提交"),

    LOGIN_AUTH(208, "未登陆"),
    PERMISSION(209, "没有权限"),

    ORDER_PRICE_ERROR(210, "订单商品价格变化"),
    ORDER_STOCK_FALL(204, "订单库存锁定失败"),
    CREATE_ORDER_FAIL(210, "创建订单失败"),

    COUPON_GET(220, "优惠券已经领取"),
    COUPON_LIMIT_GET(221, "优惠券已发放完毕"),

    URL_ENCODE_ERROR( 216, "URL编码失败"),
    ILLEGAL_CALLBACK_REQUEST_ERROR( 217, "非法回调请求"),
    FETCH_ACCESSTOKEN_FAILD( 218, "获取accessToken失败"),
    FETCH_USERINFO_ERROR( 219, "获取用户信息失败"),


    SKU_LIMIT_ERROR(230, "购买个数不能大于限购个数"),
    REGION_OPEN(240, "该区域已开通"),
    REGION_NO_OPEN(240, "该区域未开通"),
    ;

    private Integer code;

    private String message;

    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
