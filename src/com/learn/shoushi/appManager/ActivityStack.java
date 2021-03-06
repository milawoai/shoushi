package com.learn.shoushi.appManager;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by a0153-00401 on 15/11/5.
 */
public class ActivityStack {
    private  static  ActivityStack instance;

    final private Stack<Activity> stack;

    public  synchronized static ActivityStack getInstance()
    {
        if(instance == null)
        {
            instance = new ActivityStack();
        }
        return instance;
    }

    ActivityStack()
    {
        stack = new Stack<Activity>();
    }

    public Stack<Activity> getStackCopy()
    {
        Stack<Activity> stackCopy = new Stack<Activity>();
        if (stack != null) {
            stackCopy.addAll(stack);
        }
        return stackCopy;
    }


    /**
     * 添加
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (activity != null) {
            stack.add(activity);
        }
    }

    /**
     * 删除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if (activity != null) {
            stack.remove(activity);
        }
    }

    /**
     * finish 掉所有activity
     *
     * @deprecated 不推荐依赖Activity栈, 因为系统可能会把后台的Activity回调掉, 但这种Activity会从Activity栈内删除掉
     */
    public void finishAllActivity() {
        Activity[] activityList = new Activity[stack.size()];
        stack.copyInto(activityList);
        for (Activity activity : activityList) {
            activity.finish();
        }
        stack.clear();
    }

    /**
     * finish 掉所有activity，只保存exceptActivity
     *
     * @param exceptActivity
     * @deprecated 不推荐依赖Activity栈, 因为系统可能会把后台的Activity回调掉, 但这种Activity会从Activity栈内删除掉
     */
    public void finishAllActivityExcept(Activity exceptActivity) {
        boolean exist = false;
        Activity[] activityList = new Activity[stack.size()];
        stack.copyInto(activityList);
        for (Activity activity : activityList) {
            if (activity != exceptActivity) {
                activity.finish();
            } else {
                exist = true;
            }
        }
        stack.clear();
        if (exist) {
            stack.add(exceptActivity);
        }
    }

    /**
     * finish 掉所有activity，只保存exceptActivity
     *
     * @param exceptActivityCls
     * @deprecated 不推荐依赖Activity栈, 因为系统可能会把后台的Activity回调掉, 但这种Activity会从Activity栈内删除掉
     */
    public void finishAllActivityExcept(Class<? extends Activity> exceptActivityCls) {
        Activity[] activityList = new Activity[stack.size()];
        stack.copyInto(activityList);
        Activity exceptActivity = null;
        for (Activity activity : activityList) {
            if (activity.getClass() != exceptActivityCls) {
                activity.finish();
            } else {
                if (exceptActivity != null && exceptActivity != activity) {
                    exceptActivity.finish();
                }
                exceptActivity = activity;
            }
        }
        stack.clear();
        if (exceptActivity != null) {
            stack.add(exceptActivity);
        }
    }


    /**
     * 获取当前的顶层activity
     */
    public Activity getTopActivity() {
        return stack.lastElement();
    }

    /**
     * 获取堆栈大小
     *
     * @return
     * @deprecated 不推荐依赖Activity栈, 因为系统可能会把后台的Activity回调掉, 但这种Activity会从Activity栈内删除掉
     */
    public int getSize() {
        return stack.size();
    }



}
