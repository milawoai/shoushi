package com.learn.shoushi.Utils;

import com.learn.shoushi.R;
import com.learn.shoushi.appManager.AppInfo;

import java.util.HashMap;

/**
 * Created by a0153-00401 on 15/11/10.
 */
public class PublicDataType {
   public static HashMap<String,Integer> RelationShipNameToID = new HashMap<String,Integer>(){
       {
           put(AppInfo.getAppContext().getString(R.string.father),0);
           put(AppInfo.getAppContext().getString(R.string.mother),1);
           put(AppInfo.getAppContext().getString(R.string.son),2);
           put(AppInfo.getAppContext().getString(R.string.daughter),3);
       }
   };

    public static HashMap<Integer,String> RelationShipIDToName = new HashMap<Integer,String>(){
        {
            put(0,AppInfo.getAppContext().getString(R.string.father));
            put(1,AppInfo.getAppContext().getString(R.string.mother));
            put(2,AppInfo.getAppContext().getString(R.string.son));
            put(3,AppInfo.getAppContext().getString(R.string.daughter));
        }
    };

}
