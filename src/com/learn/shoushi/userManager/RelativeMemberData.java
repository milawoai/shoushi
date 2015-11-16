package com.learn.shoushi.userManager;

import java.util.List;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class RelativeMemberData {


    public String RelativeName;
    public List<String> TelePhoneList;
    public List<String> QQNumList;
    public List<String> WeiXinList;
    public int RelativeToThisUser;//标记关系
    public int ThisUserToRelative;


    RelativeMemberData(String relativeName,List<String> telePhoneList, List<String> qQNumList,List<String> weiXinList,int relativeToThisUser,int thisUserToRelative)
    {
        RelativeName = relativeName;
        TelePhoneList = telePhoneList;
        QQNumList = qQNumList;
        WeiXinList = weiXinList;
        RelativeToThisUser = relativeToThisUser;
        ThisUserToRelative = thisUserToRelative;
    }
}
