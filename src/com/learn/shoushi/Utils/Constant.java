package com.learn.shoushi.Utils;

import android.app.Activity;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class Constant {



   public static final int API_MIN_LEVEL = 8;
        /**
         * 人人的包名
         */
   public final static String RENREN_PKG_NAME = "com.learn.shoushi";


    public static final String WEB_CHART_URL = "http://10.4.32.134:18080/chart/parvenuFutureIncome/api";

    //服务条款
    public static final String SERVICE_TERMS = "http://www.sofund.com/share_h5/service.html";

    public static final String FAST_WITHDRAW_H5 = "http://www.sofund.com/share_h5/service2.html";

    //公司简介
    public static final String COMPANY_PROFILE = "http://www.sofund.com/share_h5/service.html";

    public static final int QUORA_REQUEST_CODE = 1000;

    public static final String QUORA_FINISH = "quora_finish";
    public static final String QUORA_SCORE = "quora_score";

    public static final String REFRESH_ALL_TAB = "refresh_all_tab";
    public static final String REFRESH_FINANCE_TAB = "refresh_finance_tab";
    public static final String REFRESH_MAIN_TAB = "refresh_main_tab";
    public static final String REFRESH_OPTIONAL_TAB = "refresh_optional_tab";
    public static final String REFRESH_BUY_TAB = "refresh_buy_tab";

    /* 银行卡接口 */
 //   public static final String BANK_CARD_DOMAIN = ServiceProvider.DEPLOYMENT_TEST;  // http://10.4.35.29:9000
    // 获取个人绑定银行卡列表信息
    public static final String GET_CARD_LIST = "/bank/card/list";
    // 添加银行卡
    public static final String ADD_CARD = "/bank/card/add";
    // 添加银行卡, 验证持卡人姓名和卡号
    public static final String VERIFY_CARD = "/bank/card/verifyCard";
    // 添加银行卡, 获取手机验证码
    public static final String GRT_VERIFY_CORD = "/bank/card/getVerifyCode";
    // 取消绑定银行卡
    public static final String CARD_UNBIND = "/bank/card/unBind";
    // 获取银行卡限额信息
    public static final String CARD_LIMIT = "/bank/card/bankLimit";

    /***
     * 获取验证码类型４种，注册:1，重置密码:2，修改密码:3，绑定银行卡:4，修改交易密码:5
     **/

    public static final int VERIFY_REGISTER = 1;

    public static final int VERIFY_RESET_PASSWORD = 2;

    public static final int VERIFY_CHANGE_PASSWORD = 3;

    public static final int VERIFY_BIND_BANKCARD = 4;

    public static final int VERIFY_CHANGE_TRADE_PASSWORD = 5;


    /********
     * 钱包
     *********/
    public static final String WalletBalanceDrawCash = "wallet_balance_draw_cash";

    public static final int WALLET_DRAW_CASH = 2000;
    public static final int WALLET_RECHARGE_CASH = 3000;

    public static final int DEMAND_TREASURE_DRAW_CASH = 2001;
    public static final int DEMAND_TREASURE_RECHARGE_CASH = 3001;

    /*************************
     * 开户流程的请求码和结果码
     *****************************************/

    public static final int RESULT_FAILED = Activity.RESULT_CANCELED;

    public static final int RESULT_OK = Activity.RESULT_OK;

    public static final int REQUEST_OPEN_ACCOUNT = 1;

    public static final int REQUEST_ADD_BANK_CARD = 2;

    public static final int REQUEST_SET_TRADE_PWD = 3;

    public static final int REQUEST_CHANGE_TRADE_PASSWORD = 4;

    public static final int REQUEST_BUY_FUND = 8;

    public static final int REQUEST_BUY_COMBINE = 9;

    public static final int REQUEST_TRANSFER = 50;      //调仓

    public static final int REQUEST_DIVIDEND = 60;      //分红方式


    /*************************
     * 找回交易密码和修改交易,修改手势密码，密码的请求码
     ****************************/

    public static final int REQUEST_RETRIVE_TRADE_PASSWORD = 5;

    public static final int REQUEST_CHANGE_PATTERN_PASSWORD = 6;

    public static final int REQUEST_FORGET_PASSWORD = 7;

    public static final int UNBIND_BANK_CARD = 10;

    public static final int REQUEST_FUND_TRADE = 20;

    public static final int REQUEST_PRIVILEGE_PRINCIPAL = 0x30;

    public static final int REQUEST_PRIVILEGE_PRINCIPAL_WITH_DRAW = 0x31;

    public static final int REQUEST_PRIVILEGE_PRINCIPAL_TRADE = 0x32;

    public static final int REQUEST_PRIVILEGE_QUOTA = 0x33;

    public static final int REQUEST_SHARE_THIRD = 0x40;

    public static final int REQUEST_LOGIN = 0x50;

    /*********
     * 密码相关常量
     **********/
    public static final int PASSWORD_MIN_LENG = 8;
    public static final int PASSWORD_MAX_LENG = 20;

}
