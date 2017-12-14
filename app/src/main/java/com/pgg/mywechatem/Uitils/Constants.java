package com.pgg.mywechatem.Uitils;

/**
 * Created by PDD on 2017/11/13.
 */

public interface Constants {

    //记录登录状态的常量
    String STATE_LOGIN="LoginState";
    //记录是否绑定过账号
    String STATE_BIND="BindState";

    String URL = "URL";
    String Title = "Title";

    String Phone="phone";
    String NAME="name";
    String PWD="pwd";

    String IS_OPEN="is_open";

    String ID="id";
    String CHATTYPE="chatType";

    String NET_ERROR = "网络错误，请稍后再试！";
    String Result = "status";
    String Value = "data";
    String Info = "info";

    String TYPE = "TYPE";
    String GROUP_ID = "GROUP_ID";
    String User_ID = "User_ID";
    String IM_SMS_APPKEY="b0d2e2140697fd8f18e41ae442262a88";
    int IM_SMS_APPID= 1400050669;

     String BASE_URL="http://47.94.85.7:8080/mychat/";
    //String BASE_URL="http://192.168.43.241:8080/";
    //String BASE_URL="http://192.168.191.1:8080/";//宿舍wifi
    //String BASE_URL="http://192.168.1.117:8080/";//实验室wifi
    //String BASE_URL="http://192.168.1.160:8888/";//李思明ip
    //String BASE_URL="http://192.168.155.1:8888/";//李思明ip


    String LOGIN_ID="id";
    String LOGIN_TEL="telephone";
    String LOGIN_PWD="password";
    String LOGIN_NICK="userName";
    String LOGIN_HEAD="headUrl";
    String LOGIN_SING="signature";
    String LOGIN_SEX="sex";
    String LOGIN_LOCATION="location";
    String LOGIN_BIRTHDAY="birthday";
    String LOGIN_TYPE="type";
    String LOGIN_BACKGROUND="backgroundUrl";

    String LOGIN_URL="User/login?";
    String GET_INFO="User/search?";
    String GET_FRIENDS="User/searchFriendsList?";

    String USERINFO="userinfo";

    String HEAD_URL="head_url";
    String UPDATE_NICK="User/UpdateUserName?";
    String UPDATE_SEX="User/UpdateSex?";
    String UPDATE_PWD="User/UpdatePwd?";
    String UPDATE_HEAD="User/UpdateHeadUrl?";
    String UPDATE_SIGN="User/UpdateSignature?";
    String UPDATE_BACKGROUND="User/UpdateBackgroundUrl?";
    String UPDATE_LOCATION="User/UpdateLocation?";
    String SEARCH_BY_ID="User/search?";
    String SEARCH_MOMENTS="Moments/searchMoments?";
    String INSERT_MOMENTS="Moments/insert?";
    String SEARCH_ALL_COMMENT="Comment/selectAllMomentsId?";
    String INSERT_COMMENT="Comment/insert?";

}
