package com.learn.shoushi.userManager;

import android.content.ContentResolver;
import android.content.ContentValues;
import com.learn.shoushi.appManager.HelperApplication;

import java.util.List;

/**
 * Created by a0153-00401 on 15/11/10.
 */
public class UserInfo {

    private static UserInfo instance;

    public static  synchronized UserInfo getInstance()
    {
        if(instance == null)
        {
            instance = new UserInfo();
        }
        return instance;
    }

    private String account = "";                    //当前用户帐号
    private String loginPassword = "";                //当前用户登陆密码
    private String patternPassword = "";            //当前用户的手势密码
    private String realUserName = "";                //当前用户真实姓名
    private long userId = 0;                        // 当前用户id
    private boolean isLogIn = false;                //判断用户是否登录
    private boolean isLogInByPattern = false;        //是否用手势密码登录
    private long loginTime = 0;                        //用户的最新登录时间
    private int sex = 0;                            //男：0；女：1
    private boolean unlockActivityIsOpen = false;

    private List<RelativeMemberData> relativesLists;

    /**
     * @param isOpen 设置当前解锁页面 的打开状态
     */
    public void setUnlockActivityIsOpen(boolean isOpen) {
        this.unlockActivityIsOpen = isOpen;
    }

    public boolean getUnlockActivityIsOpen() {
        return unlockActivityIsOpen;
    }

    /**
     * 用户是否已登录
     *
     * @return
     */
    public boolean isLogin() {
        return isLogIn;
    }

    /**
     * 登录成功后，设置用户的登录状态
     *
     * @param
     */
    public void setCurrentIsLogin(boolean isLogin) {
        this.isLogIn = isLogin;
    }


    /**
     * 设置当前用户手势解锁的状态
     *
     * @return
     */
    public void setIsloginByPattern(boolean isLogInByPattern) {
        this.isLogInByPattern = isLogInByPattern;
    }

    /**
     * 获取当前用户是否设置了手势解锁
     *
     * @return
     */
    public Boolean getIsloginByPattern() {
        return isLogInByPattern;
    }

    /**
     * 获取当前用户ID
     *
     * @return
     */
    public long getCurrentUserId() {
        return userId;
    }

    /**
     * 设置当前用户ID
     *
     * @return
     */
    public void setCurrentUserId(int uid) {
        this.userId = uid;
    }


    public String getUserRealName() {
        return realUserName;
    }

    public void setUserRealName(String str) {
        realUserName = str;
        ContentResolver cr = HelperApplication.getContext().getContentResolver();
        try {
           /* ContentValues toUpdate = new ContentValues();
            toUpdate.put(AccountColumn.USER_REAL_NAME, realUserName);
            cr.update(AccountModel.getInstance().getUri(), toUpdate,
                    AccountColumn.UID + "=" + UserInfo.getInstance().getCurrentUserId(), null);*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 设置当前用户的帐号
     */
    public void setCurrentAccount(String account) {
        this.account = account;
    }

    /**
     * 获取当前用户的帐号
     */
    public String getCurrentAccount() {
        return account;
    }

    /**
     * 设置当前用户的手势密码
     */
    public void setCurrentPatternPassword(String patternPassword) {
        this.patternPassword = patternPassword;
        ContentResolver cr = HelperApplication.getContext().getContentResolver();
        try {
           /* ContentValues toUpdate = new ContentValues();
            toUpdate.put(AccountColumn.USER_PATTERN_PWD, patternPassword);
            cr.update(AccountModel.getInstance().getUri(), toUpdate,
                    AccountColumn.UID + "=" + UserInfo.getInstance().getCurrentUserId(), null);*/
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前用户的手势密码
     */
    public String getCurrentPatternPassword() {
        return patternPassword;
    }

    /**
     * 设置当前用户的密码
     */
    public void setCurrentLoginPassword(String password) {
        this.loginPassword = password;
    }

    /**
     * 获取当前用户的密码
     */
    public String getCurrentLoginPassword() {
        return loginPassword;
    }


}
