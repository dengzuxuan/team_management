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
    INPUT_PARAM_WRONG(2101,"输入参数有误"),
    INPUT_EMAIL_PARAM_WRONG(2102,"输入email格式有误"),
    INPUT_PHONE_PARAM_WRONG(2101,"输入电话格式有误"),
    INPUT_STUDENTNO_PARAM_WRONG(2103,"输入用户学号有误"),
    INPUT_USRRNAME_PARAM_WRONG(2104,"输入用户名称有误"),
    FILE_WRONG_EMPTY(2201,"文件无法正常读取或文件为空，请按格式填写"),
    FILE_WRONG_REPEAT(2202,"文件中有学号已经存在平台中"),
    FILE_WRONG_EMPTY_SINGLE(2203,"文件中有部分数据不全"),

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
