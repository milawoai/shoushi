package com.learn.shoushi.fragment.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.LogUtils;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.aChangeView.CountTextView;

/**
 * Created by a0153-00401 on 15/11/10.
 */
public abstract class BaseFragment extends Fragment {

    private final String TAG = "BaseFragment";

    public Activity context;

    public View containerView;
    public View noNetworkView;

    public Button reLoadButton;

    //protected RenrenProgressDialog mProgressDialog;

    protected boolean isViewCreated = false;

    /* 预先配置 */
    protected void onPreConfigured() {
    }

    /* 生成通用主文件布局 */
    protected abstract int onSetContainerViewId();

    /* 初始化页面控件 */
    protected abstract void onInitView();

    /* 初始化监听*/
    protected void onInitListener() {
    }

    /* 注册广播接收者*/
    protected void onRegisterReceiver() {
    }

    protected void unRegisterReceiver() {
    }

    /* 加载网络数据 */
    protected void onLoadNetworkData() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        setMenuVisibility(false);
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtils.i("openFragment: " + this.getClass().getSimpleName());
        context = getActivity();
        onPreConfigured();
        containerView = inflater.inflate(onSetContainerViewId(), container, false);
        noNetworkView = inflater.inflate(R.layout.no_network_layout, null, false);

        onInitProgressBar();
        onInitView();
        onInitNoNetworkView();

        onInitListener();
        onRegisterReceiver();

        onLoadNetworkData();

        return containerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // onAttach(getActivity());
        afterActivityCreated();
        super.onActivityCreated(savedInstanceState);
        onFragmentShow();
    }

    protected void onInitProgressBar() {
      /*  mProgressDialog = new RenrenProgressDialog(context);
        mProgressDialog.setCancelable(false);*/
    }

    protected void showProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                  /*  if (mProgressDialog != null && !mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }*/
                }
            });
        }
    }

    protected void dismissProgressBar() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                   /* if (mProgressDialog != null) {
                        mProgressDialog.dismiss();
                    }*/
                }
            });
        }
    }

    protected void onInitNoNetworkView() {
        ((ViewGroup) containerView).addView(noNetworkView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        reLoadButton = (Button) noNetworkView.findViewById(R.id.no_network_button);
        noNetworkView.setVisibility(View.GONE);
        reLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoadNetworkData();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.d(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onFragmentShow() {
        LogUtils.d(TAG, "onFragmentShow: " + this.getClass().getName());
    }

    public void onFragmentHide() {
        LogUtils.d(TAG, "onFragmentHide: " + this.getClass().getName());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        LogUtils.d(TAG, "onHiddenChanged: " + this.getClass().getName());

        if (!hidden) {
            onFragmentShow();
        } else {
            onFragmentHide();
        }
    }

    public void showNetworkView(boolean isShow) {
        final int visible = isShow ? View.VISIBLE : View.GONE;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                noNetworkView.setVisibility(visible);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
//        LogUtils.d(TAG, "onResume: " + this.getClass().getName());
        super.onResume();
        // onFragmentResume();
    }

    protected void afterActivityCreated() {
        isViewCreated = true;
    }

    @Override
    public void onPause() {
        LogUtils.d(TAG, "onPause: " + this.getClass().getName());
        super.onPause();
        // onFragmentPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroyView() {
        LogUtils.d(TAG, "onDestroyView: " + this.getClass().getName());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy: " + this.getClass().getName());
        unRegisterReceiver();
       /* if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }*/
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    protected void finish() {
        getActivity().finish();
    }

    /* 无聊的方法 Down */

    // TextView赋值
    public void setText(int resourceId, String text) {
        ((TextView) containerView.findViewById(resourceId)).setText(text);
    }

    public <T> void setCurrencyText(TextView textView, T currency) {

        if (currency instanceof Double) {
            if (textView instanceof CountTextView) {
                ((CountTextView) textView).setCountText(Methods.currencyFormat((Double) currency));
            } else {
                textView.setText(Methods.currencyFormat((Double) currency));
            }
        } else if (currency instanceof String) {
            if (TextUtils.isEmpty((String) currency)) {
                textView.setText("");
                return;
            }
            try {
                if (textView instanceof CountTextView) {
                    ((CountTextView) textView).setCountText(
                            Methods.currencyFormat(Double.parseDouble((String) currency)));
                } else {
                    textView.setText(Methods.currencyFormat(Double.parseDouble((String) currency)));
                }
            } catch (NumberFormatException e) {
                if (textView instanceof CountTextView) {
                    ((CountTextView) textView).setCountText((String) currency);
                } else {
                    textView.setText((String) currency);
                }
            }
        } else {
            textView.setText("");
        }
    }

    // TextView 金额赋值 带颜色
    public <T> void setCurrencyColorText(TextView textView, T currency, int type) {
        setCurrencyColorText(textView, currency, "", type, true);
    }

    // TextView 金额赋值 带颜色
    public <T> void setCurrencyColorText(TextView textView, T currency, String suffix) {
        setCurrencyColorText(textView, currency, suffix, 1, true);
    }

    // TextView 金额赋值 带颜色
    public <T> void setCurrencyColorText(TextView textView, T currency) {
        setCurrencyColorText(textView, currency, "", 1, true);
    }

    public <T> void setCurrencyColorText(TextView textView, T currency, boolean isAdjustTextSize) {
        setCurrencyColorText(textView, currency, "", 1, isAdjustTextSize);
    }

    public <T> void setCurrencyColorText(TextView textView, T currency, String suffix, int type, boolean isAdjustTextSize) {
        int color = 0;
        switch (type) {
            case 0:
                color = R.color.common_white;
                break;
            case 1:
                color = R.color.common_orange_text;
                break;
            case 2:
                color = R.color.common_black_text;
                break;
            default:
                color = R.color.common_white;
                break;
        }
        if (currency instanceof Double) {
            if ((Double) currency > 0) {
                textView.setTextColor(getResources().getColor(color));
            } else if ((Double) currency < 0) {
                textView.setTextColor(getResources().getColor(R.color.common_green_text));
            }
            if (textView instanceof CountTextView && isAdjustTextSize) {
                ((CountTextView) textView).setCountText(Methods.currencyFormat((Double) currency) + suffix);
            } else {
                textView.setText(Methods.currencyFormat((Double) currency) + suffix);
            }
        } else if (currency instanceof String) {
            if (TextUtils.isEmpty((String) currency)) {
                textView.setText("");
                return;
            }
            try {
                double value = Double.parseDouble((String) currency);
                if (value > 0) {
                    textView.setTextColor(getResources().getColor(color));
                } else if (value < 0) {
                    textView.setTextColor(getResources().getColor(R.color.common_green_text));
                }
                if (textView instanceof CountTextView && isAdjustTextSize) {
                    ((CountTextView) textView).setCountText(Methods.currencyFormat(value) + suffix);
                } else {
                    textView.setText(Methods.currencyFormat(value) + suffix);
                }
            } catch (NumberFormatException e) {
                if (textView instanceof CountTextView) {
                    ((CountTextView) textView).setCountText(currency + suffix);
                } else {
                    textView.setText(currency + suffix);
                }
            }
        } else {
            textView.setText("");
        }
    }


    // 刷新 Header
 //  public void addRefreshHeader(PtrFrameLayout ptrFrameLayout) {
       /*StoreHouseHeader header = new StoreHouseHeader(context);
        header.setPadding(0, Methods.dp2px(context, 15), 0, Methods.dp2px(context, 15));
        header.initWithString(getString(R.string.refresh_word));
        ptrFrameLayout.setDurationToCloseHeader(1000);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);*/

/*        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(context);
        ptrFrameLayout.setHeaderView(header);
        ptrFrameLayout.addPtrUIHandler(header);
    }*/

}
