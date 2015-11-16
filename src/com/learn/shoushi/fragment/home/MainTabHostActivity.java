package com.learn.shoushi.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.LogUtils;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.Utils.SettingManager;
import com.learn.shoushi.activity.BaseFragmentActivity;
import com.learn.shoushi.activity.SignInActivity;
import com.learn.shoushi.appManager.ActivityStack;
import com.learn.shoushi.appManager.AppInfo;
import com.learn.shoushi.fragment.base.BaseFragment;
import com.learn.shoushi.fragment.base.BaseFragmentTabHost;
import com.learn.shoushi.service.Screenservice;
import com.learn.shoushi.userManager.UserInfo;

/**
 * Created by a0153-00401 on 15/11/10.
 */
public class MainTabHostActivity extends BaseFragmentActivity implements
        TabHost.OnTabChangeListener, View.OnClickListener {

    private static final String TAG = "MainActivity";
    public static final String ARG_ACTIVITY_ARGS = "arg_activity_args";
    public static final String MORE_TAB_INIT_INDEX = "more_tab_init_index";
    private int totalMessageNumber;
    private enum TAB {
        MAIN("帮助"), ALBUM("相册"), SETTING("设置"), MINE("我的");

        TAB(String cName) {
            this.cName = cName;
        }

        public String cName;

        public String getName() {
            return cName;
        }
    }

    private String sCurrentTab = TAB.SETTING.getName();

    private BaseFragmentTabHost mTabHost;

    // Tab 图片View
    private View mainView;
    private View albumView;
    private View settingView;
    private View mineView;

    // Tab 文字TextView
    private TextView mainText;
    private TextView albumText;
    private TextView settingText;
    private TextView mineText;
    private TextView financeTextNumber;

    private boolean doubleBackToExitPressedOnce = false;

    private int mDefaultTabIndex = 3;

    public boolean smsTriggerTransferNotf;

    /**
     * 监测屏幕的锁屏和解锁事件
     */
    private long LOCK_TIME = 1000 * 60 * 2;
    private long lockTime = 0;
    private Screenservice mScreenservice;

    public static void openHomeActivity(Context context) {
        openHomeActivity(context, 0, null);
    }


    public static void openHomeActivity(Context context, int defaultIndex, Bundle args) {
        if (context instanceof MainTabHostActivity) {
            ((MainTabHostActivity) context).setCurrentTab(defaultIndex);
            return;
        }
        // 登陆成功, 打开首页
        Intent intent = new Intent(context, MainTabHostActivity.class);
        if (args != null) {
            intent.putExtra(ARG_ACTIVITY_ARGS, args);
        }
        intent.putExtra(MORE_TAB_INIT_INDEX, defaultIndex);
        if (context instanceof Activity) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
        ActivityStack.getInstance().finishAllActivityExcept(MainTabHostActivity.class);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 检查session有效性
       /* if (!checkSessionValid(intent)) {
            // 需要跳到登录页
            SignInActivity.openSignInActivity(this, UnLockPatternActivity.OPEN_LOGIN);
            return;
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       /* super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);
        mTabHost = (BaseFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        TabHost原版演示-1
        setContentView(R.layout.tabhost_change);
        TabHost th=(TabHost)findViewById(R.id.change_tabhost);
        th.setup();            //初始化TabHost容器

        //在TabHost创建标签，然后设置：标题／图标／标签页布局
        th.addTab(th.newTabSpec("tab1").setIndicator("标签1",getResources().getDrawable(R.drawable.app_logo)).setContent(R.id.tab1));
        th.addTab(th.newTabSpec("tab2").setIndicator("标签2", null).setContent(R.id.tab2));
        th.addTab(th.newTabSpec("tab3").setIndicator("标签3", null).setContent(R.id.tab3));*/

          /*TabHost原版演示-2
        setContentView(R.layout.tabtest);
        TabHost m = (TabHost)findViewById(R.id.tabhost);
        m.setup();

        LayoutInflater i= LayoutInflater.from(this);
        i.inflate(R.layout.tab1, m.getTabContentView());
        i.inflate(R.layout.tab2, m.getTabContentView());//动态载入XML，而不需要Activity

        m.addTab(m.newTabSpec("tab1").setIndicator("标签1").setContent(R.id.LinearLayout01));
        m.addTab(m.newTabSpec("tab2").setIndicator("标签2").setContent(R.id.LinearLayout02));*/


        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabhost);
        mTabHost = (BaseFragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        getArgs();
        onInitView();
        addTabFragment();

        mScreenservice = new Screenservice(getApplicationContext());
        mScreenservice.requestScreenStateUpdate(new Screenservice.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                Log.d("mScreenservice", "------------->onScreenOn is called");
                long time = SettingManager.getInstance().getScreeLockTime();
                lockTime = System.currentTimeMillis() - time;
                Log.d("mScreenservice", "------------->" + lockTime);
                Log.d("mScreenservice", "------------->" + time);
                if (lockTime >= LOCK_TIME && time != 0) {
                    showUnlockActivity();
                    SettingManager.getInstance().setScreenLockTime(0);
                }
            }

            @Override
            public void onScreenOff() {
                Log.d("mScreenservice", "------------->onScreenOff is called");
                SettingManager.getInstance().setScreenLockTime(System.currentTimeMillis());
            }
        });

       /* if (!UserInfo.getInstance().getIsExperienced()){
            getCountUnread();
        }*/

    }

    /**
     * 检查Session有效性
     *
     * @param intent
     * @return false:session无效, 需要注销
     */
    private boolean checkSessionValid(Intent intent) {
        /*if (intent != null) {
            boolean isSessionKeyFailure = intent.getBooleanExtra(ServiceError.ARG_BOOL_SESSION_KEY_FAILURE, false);
            if (isSessionKeyFailure) {
                UserInfo.getInstance().logout(MainTabHostActivity.this);
                return false;
            }
        }*/
        return true;
    }

    private void showUnlockActivity() {
        Log.v(TAG, " ------ " + UserInfo.getInstance().isLogin() + "   ");
        if (UserInfo.getInstance().isLogin() && !UserInfo.getInstance().getUnlockActivityIsOpen()
                /*&& !UserInfo.getInstance().getIsExperienced()*/) {
           /* if (!TextUtils.isEmpty(UserInfo.getInstance().getCurrentPatternPassword())) {
                Intent intent = new Intent(getApplicationContext(), UnLockPatternActivity.class);
                intent.putExtra(UnLockPatternActivity.mOpenIntent, UnLockPatternActivity.OPEN_BACK);
                startActivity(intent);
            }*/
        }
    }


    private void getArgs() {

        mDefaultTabIndex = 0;
        sCurrentTab = TAB.MINE.getName();
        Intent intent = getIntent();
        if (intent != null) {
            mDefaultTabIndex = intent.getIntExtra(MORE_TAB_INIT_INDEX, 0);
            Bundle args = intent.getBundleExtra(ARG_ACTIVITY_ARGS);
            if (mDefaultTabIndex == 0) {
                sCurrentTab = TAB.MAIN.getName();
            } else if (mDefaultTabIndex == 1) {
                sCurrentTab = TAB.ALBUM.getName();
            } else if (mDefaultTabIndex == 2) {
                sCurrentTab = TAB.SETTING.getName();
            } else if (mDefaultTabIndex == 3) {
                sCurrentTab = TAB.MINE.getName();
            }
            if (args != null) {
                smsTriggerTransferNotf = args.getBoolean("smsTriggerTransferNotf", false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }

    private void onInitView() {
        Intent intent = getIntent();
        // 检查session有效性
      /*  if (!checkSessionValid(intent)) {
            // 需要跳到登录页
            SignInActivity.openSignInActivity(this, UnLockPatternActivity.OPEN_LOGIN);
            return;
        }*/
//        if (!UserInfo.getInstance().isLogin()) {
//            // 欢迎页面已显示, 用户未登陆的话，显示登陆页面
//            SignInActivity.openSignInActivity(this);
//            return;
//        }

        mainView = View.inflate(MainTabHostActivity.this, R.layout.tab, null);
        ((ImageView) mainView.findViewById(R.id.tab_imageview_icon))
                .setImageResource(R.drawable.tab_main);
        mainText = (TextView) mainView.findViewById(R.id.tab_textview_title);
        mainText.setText(TAB.MAIN.getName());

        albumView = View.inflate(MainTabHostActivity.this, R.layout.tab, null);
        ((ImageView) albumView.findViewById(R.id.tab_imageview_icon))
                .setImageResource(R.drawable.tab_album);
        albumText = (TextView) albumView.findViewById(R.id.tab_textview_title);
        albumText.setText(TAB.ALBUM.getName());

        settingView = View.inflate(MainTabHostActivity.this, R.layout.tab, null);
        ((ImageView) settingView.findViewById(R.id.tab_imageview_icon))
                .setImageResource(R.drawable.tab_setting);
        settingText = (TextView) settingView.findViewById(R.id.tab_textview_title);
        settingText.setText(TAB.SETTING.getName());

        mineView = View.inflate(MainTabHostActivity.this, R.layout.tab_more, null);
        ((ImageView) mineView.findViewById(R.id.tab_imageview_icon))
                .setImageResource(R.drawable.tab_mine);
        mineText = (TextView) mineView.findViewById(R.id.tab_textview_title);
        mineText.setText(TAB.MINE.getName());
      //  financeTextNumber = (TextView) mineView.findViewById(R.id.tab_imageview_toast);
    }

    private void addTabFragment() {
        mTabHost.addTab(mTabHost.newTabSpec(TAB.MAIN.getName()).setIndicator(mainView),
                MainTab3Fragment.class, null);

        /*mTabHost.addTab(mTabHost.newTabSpec(TAB.ALBUM.getName()).setIndicator(albumView),
                SelectionFragment.class, null);*/

    /*    mTabHost.addTab(
                mTabHost.newTabSpec(TAB.OPTIONAL.getName()).setIndicator(optionalView),
                MySelfServiceFragmentV2.class, null);

        mTabHost.addTab(mTabHost.newTabSpec(TAB.FINANCE.getName()).setIndicator(financeView),
                FinanceTabFragment.class, null);*/


        mTabHost.getTabWidget().setVisibility(View.VISIBLE);
        mTabHost.setOnTabChangedListener(this);
        setCurrentTab(mDefaultTabIndex);
    }

    private void getCountUnread() {
       /* ServiceProvider.countUnread(new INetResponse() {
            @Override
            public void response(INetRequest req, JsonValue obj) {
                if (obj == null) {
                    return;
                }
                JsonObject object = (JsonObject) obj;
                if (ServiceError.noError(object, false)) {
                    totalMessageNumber = (int) object.getNumString("totalCount");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showMessageBadage(totalMessageNumber);
                        }
                    });
                }
            }
        });*/
    }

    public void setCurrentTab(int index) {
        mTabHost.setCurrentTab(index);
    }

    public BaseFragment getCurrentFragment() {
        return mTabHost.getCurrentFragment();
    }

    /**
     * 财富tab图标显示消息红泡提醒
     */
    public void showMessageBadage(int totalMessageNum) {
        if(totalMessageNum > 0){
            financeTextNumber.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 财富tab图标隐藏消息红泡提醒
     */
    public void hideMessageBadge() {
        financeTextNumber.setVisibility(View.GONE);
    }

    /* TabHost.OnTabChangeListener */
    @Override
    public void onTabChanged(String tabTag) {

        FragmentManager manager = getSupportFragmentManager();

        if (manager.getBackStackEntryCount() > 0) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        sCurrentTab = tabTag;
        LogUtils.i(TAG, "sCurrentTab: " + sCurrentTab);
    }

    @Override
    protected void onStart() {
        if (AppInfo.isAppInBackground() && mTabHost != null) {
            Fragment fgm = mTabHost.getCurrentFragment();
           /* if (fgm != null) {
                if (fgm instanceof MainTab3Fragment) {
                    ((MainTab3Fragment) fgm).onTabSelected();
                } else if (fgm instanceof SelectionFragment) {
                    ((SelectionFragment) fgm).onTabSelected();
                } else if (fgm instanceof MySelfServiceFragmentV2) {
                    ((MySelfServiceFragmentV2) fgm).onTabSelected();
                } else if (fgm instanceof FinanceTabFragment) {
                    ((FinanceTabFragment) fgm).onTabSelected();
                }
            }*/
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        LogUtils.i(TAG, "MainTabHostActivity onStop... " + sCurrentTab);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtils.i(TAG, "MainTabHostActivity onDestroy... " + sCurrentTab);
        mScreenservice.stopScreenStateUpdate();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        LogUtils.i(TAG, "MainTabHostActivity onResume... " + sCurrentTab);
        super.onResume();
      //  Methods.judgePrivilegeNoticeState();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce
                || this.getSupportFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(MainTabHostActivity.this, getString(R.string.double_click_quit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }

        }, 2000);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

    /*    if (event.getAction() == KeyEvent.ACTION_UP) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_MENU) {
                getCurrentFragment().onKeyUp();
                return true;
            }
        }*/

        return super.onKeyUp(keyCode, event);
    }

    /* View.OnClickListener */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.start_btn:
//                questionLayout.setVisibility(View.GONE);
//                TerminalActivity.showFragment(this, InvestigateFragment.class, null);
//                break;
            default:
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode);
        if (getCurrentFragment() != null)
            getCurrentFragment().onActivityResult(requestCode, resultCode, data);
    }
}
