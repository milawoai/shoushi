package com.learn.shoushi.fragment.home;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.learn.shoushi.R;
import com.learn.shoushi.UI.RenrenConceptDialog;
import com.learn.shoushi.Utils.*;
import com.learn.shoushi.aChangeView.*;
import com.learn.shoushi.activity.TerminalActivity;
import com.learn.shoushi.fragment.base.BaseFragment;
import com.learn.shoushi.service.QuoraDoneReceiver;
import com.learn.shoushi.userManager.RelativeMemberData;
import com.learn.shoushi.userManager.UserInfo;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created by a0153-00401 on 15/11/12.
 */
public class MainTab3Fragment extends BaseFragment implements XListView.IXListViewListener,
        View.OnClickListener, QuoraDoneReceiver.QuoraCallBack, AbsListView.OnScrollListener {


    private final String TAG = "MainTab3Fragment";

    private TopActionBar topActionBar;
    private XListView mListView;
    private Button investButton;

    private CustomHeaderView customHeaderView;
    private TabHeaderView tabHeaderView;

    private View footerView;
    private TextView pageTopBannerTextView;

   private ItemAdapter itemAdapter;

    private QuoraDoneReceiver quoraDoneReceiver;
    private RefreshReceiver refreshReceiver;

    private RelativeMemberData portfolioData;

    private BoAnimManager boAnimManager;

    //是否做过测试 &测试结果 &可否重新测试
    private int riskScore = -1; // 默认没有为-1

    private boolean canReQuora = false;
    private boolean isInvest;

    private boolean footerAdded = false;
    private boolean firstLoadData = true;//首次加载如果无网络显示相应界面,不加会白屏

    public static boolean goToAssetPageOnResume = false;
    private boolean loadNetworkDataOnResume;
    private FullScreenGuideView guideView1;
    private FullScreenGuideView guideView2;


    @Override
    protected int onSetContainerViewId() {
        return R.layout.fragment_tab_main_layout_v3;
    }

    @Override
    protected void onPreConfigured() {
//        riskScore = SettingManager.getInstance().getQuoraScore();
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
                        | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @Override
    public void onResume() {
        super.onResume();
      /*  if (goToAssetPageOnResume) {
            goToAssetPageOnResume = false;
            TerminalActivity.showFragment(getActivity(), AssetsManagerFragment.class, null);
        }*/
        if (loadNetworkDataOnResume) {
            //onLoadNetworkData();
            loadNetworkDataOnResume = false;
        }
    }

    @Override
    protected void onInitView() {

        topActionBar = (TopActionBar) containerView.findViewById(R.id.topbar);
        topActionBar.showRightButton(true);
        topActionBar.showRight2Button(true);

        customHeaderView = new CustomHeaderView(context);
        tabHeaderView = new TabHeaderView(context);

        footerView = LayoutInflater.from(getActivity()).inflate(
                R.layout.white_footer_view, null, false);

        mListView = (XListView) containerView.findViewById(R.id.list_view);
        mListView.setPullLoadEnable(true);
        mListView.setOnScrollListener(this);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);

        pageTopBannerTextView = (TextView) containerView.findViewById(R.id.page_top_banner_text_view);

        itemAdapter = new ItemAdapter();
        mListView.setAdapter(itemAdapter);
        investButton = (Button) containerView.findViewById(R.id.invest_btn);
        boAnimManager = new BoAnimManager();
      //  showNewUserGuide();
    }

    public void showNewUserGuide() {
       /* if (SettingManager.getInstance().isShowNewUserGuide1()) {
            SettingManager.getInstance().setShowNewUserGuide1(false);
            showNewUserGuide1();
        }*/

    }

  /*  public void showNewUserGuide1() {
        guideView1 = new FullScreenGuideView(context);
        float density = Methods.getDensity(context);
        guideView1.addImageView(R.drawable.newuser_guidebtn, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, Methods.computePixelsWithDensity(330, density), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideView1.dismiss();
                if (SettingManager.getInstance().isShowNewUserGuide2()) {
                    SettingManager.getInstance().setShowNewUserGuide2(false);
                    showNewUserGuide2();
                }
            }
        });
        guideView1.addImageView(R.drawable.newuser_guide1text, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, Methods.computePixelsWithDensity(260, density), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        guideView1.addImageView(R.drawable.newuser_guide1image, Gravity.RIGHT | Gravity.TOP, 0, Methods.computePixelsWithDensity(402, density), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        guideView1.show();
        guideView1.setAutoDismiss(false);
    }

    public void showNewUserGuide2() {
        guideView2 = new FullScreenGuideView(context);
        float density = Methods.getDensity(context);
        guideView2.addImageView(R.drawable.newuser_guidebtn, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, Methods.computePixelsWithDensity(420, density), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guideView2.dismiss();
            }
        });
        guideView2.addImageView(R.drawable.newuser_guide2text, Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, Methods.computePixelsWithDensity(320, density), 0, 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        guideView2.addImageView(R.drawable.newuser_guide2image, Gravity.RIGHT | Gravity.TOP, 0, Methods.computePixelsWithDensity(220, density), Methods.computePixelsWithDensity(10, density), 0, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        guideView2.show();
        guideView2.setAutoDismiss(false);
    }*/

    @Override
    protected void onInitListener() {

        customHeaderView.setViewClickListener(new CustomHeaderView.OnViewClickListener() {
            @Override
            public void onFunctionButtonClick(View view) {
               /* if (portfolioData == null) return;

                if (portfolioData.isRisk) {
                    Bundle bundle = new Bundle();
                    bundle.putLong("portfolioId", portfolioData.myPortfolioId);
                    ServiceProvider.isOpenAccount(Constant.REQUEST_BUY_COMBINE, bundle);
//                    TradeFragment.show(context, TradeFragment.BUY, TradeFragment.FUND_COMBINE, "", "", 0, 0);
                } else {
                   if (UserInfo.getInstance().showExperiencDialog()) {
                        return;
                    }
                   //z InvestigateFragment.show(context, GoodFinancialDetailFragment.QUORA_JUMP);
                }*/
            }

            @Override
            public void onDescriptionLayoutClick(View view) {
             /*   if (portfolioData.isRisk) {
//                    GoodFinancialDetailFragment.show(context, portfolioData.myPortfolioId, GoodFinancialDetailFragment.MY_COMBINE_JUMP);
                    String url = Constant.SERVICE_TERMS;
                    String title = context.getString(R.string.test_result);
                    Bundle bundle = new Bundle();
                    bundle.putString(WebFragment.ARGS_STRING_URL, url);
                    bundle.putString(WebFragment.ARGS_STRING_TITLE, title);
                    bundle.putBoolean("isInvest", false);
                    WebFragment.show(context, bundle);
                }*/
            }
        });

        topActionBar.setButtonClickListener(new TopActionBar.OnTopBarButtonClickListener() {
            @Override
            public void onLeftButtonClick(View view) {

            }

            @Override
            public void onRightButton2Click(View view) {
                sharePortfolio();
            }

            @Override
            public void onRightButtonClick(View view) {
                Log.i("RightButtonClick","Right1");
            /*    if (portfolioData.isTimeValid && !portfolioData.hasTransfered) {
                    // in the time window of transfer and the transfer has not been done yet,
                    // go to Investigation page for questionnaire.
                    InvestigateFragment.showForResult(context, 0, 0, InvestigateFragment.FROM_MAIN_TAB_3_FRAGMENT);
                } else if (portfolioData.isTimeValid && portfolioData.hasTransfered) {
                    // in the time window of transfer but the transfer has been done already,
                    // pop up a dialog to notify user.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.has_transfered_recently_so_no_questionnaire);
                    builder.setPositiveButton(R.string.confirm, null);
                    builder.create().show();
                } else {
                    // not in the time window of transfer, pop up a dialog too.
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(R.string.not_in_transfer_window_so_no_questionnaire);
                    builder.setPositiveButton(R.string.confirm, null);
                    builder.create().show();
                }

                /*if (portfolioData.canRestart) {
                    InvestigateFragment.show(context, GoodFinancialDetailFragment.QUORA_JUMP);
                } else {
                    Methods.showToast(getString(R.string.testing_once_monthly));
                }*/
            }
        });

        investButton.setOnClickListener(this);
    }

    @Override
    protected void onRegisterReceiver() {
        quoraDoneReceiver = new QuoraDoneReceiver(this);
        context.registerReceiver(quoraDoneReceiver, new IntentFilter(Constant.QUORA_FINISH));
        refreshReceiver = new RefreshReceiver();
        context.registerReceiver(refreshReceiver, new IntentFilter(Constant.REFRESH_MAIN_TAB));
        context.registerReceiver(refreshReceiver, new IntentFilter(Constant.REFRESH_ALL_TAB));
    }

    @Override
    protected void unRegisterReceiver() {
        if (quoraDoneReceiver != null) {
            context.unregisterReceiver(quoraDoneReceiver);
        }
        if (refreshReceiver != null) {
            context.unregisterReceiver(refreshReceiver);
        }
    }

    @Override
    protected void onLoadNetworkData() {

        if (!mListView.isPullRefreshing()) {
            showProgressBar();
        }

      /*  ServiceProvider.portfolioAllFunction("", riskScore, new INetResponse() {
            @Override
            public void response(INetRequest req, JsonValue obj) {
                dismissProgressBar();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mListView.stopRefresh();
                    }
                });
                if (obj == null) {
                    return;
                }
                JsonObject result = (JsonObject) obj;
                Logger.json(result.toJsonString());
                if (ServiceError.noError(result, true)) {
                    showNetworkView(false);
                    portfolioData = new PortfolioDataV3(result);
//                    riskScore = SettingManager.getInstance().getQuoraScore();
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setViewData();
                            checkPortfolioState();
                            firstLoadData = false;
                        }
                    });
                } else {
                    if (result.getNum(ServiceError.CODE_NAME) == ErrorCode.NETWORK_UNAVAIABLE && firstLoadData) {
                        showNetworkView(true);
                        topActionBar.showRightButton(false);
                        topActionBar.showRight2Button(false);
                    }
                }
            }
        });*/

    }

    /**
     * 普通快速调仓窗口
     */
    private void showTransferPositionDialog1() {
        RenrenConceptDialog.Builder builder = new RenrenConceptDialog.Builder(getActivity());
        final RenrenConceptDialog dialog = builder.create();
        dialog.setOkBtnVisibility(true);
        dialog.setMessage("HEHE", getResources().getColor(R.color.black));
        dialog.setPositiveButton("look", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // TransferDetailsFragment.showForResult(getActivity(), portfolioData.transferType, 0);
            }
        });
        dialog.setNegativeButton("ignore", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setmDividerBg(getResources().getColor(R.color.common_gray_bg));
        dialog.setButtonBg(R.drawable.renren_dialog_btn_bg_selector_2, getResources().getColor(R.color.blue5));
        dialog.setDialogRoundBg();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 追加投资的快速调仓提醒
     */
    private void showTransferPositionDialog2() {
        RenrenConceptDialog.Builder builder = new RenrenConceptDialog.Builder(getActivity());
        final RenrenConceptDialog dialog = builder.create();
        dialog.setOkBtnVisibility(true);
        dialog.setMessage("123", getResources().getColor(R.color.black));
        dialog.setPositiveButton("123", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TransferDetailsFragment.showForResult(getActivity(), portfolioData.transferType, true, portfolioData.lastedportfolioId, 0);
            }
        });
        dialog.setNegativeButton("456", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              /*  Bundle bundle = new Bundle();
                bundle.putLong("portfolioId", portfolioData.portfolioId);
                ServiceProvider.isOpenAccount(Constant.REQUEST_BUY_COMBINE, bundle);*/
            }
        });
        dialog.setmDividerBg(getResources().getColor(R.color.common_gray_bg));
        dialog.setButtonBg(R.drawable.renren_dialog_btn_bg_selector_2, getResources().getColor(R.color.blue5));
        dialog.setDialogRoundBg();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 止损的快速调仓提醒
     */
    private void showTransferPositionDialog3() {
        RenrenConceptDialog.Builder builder = new RenrenConceptDialog.Builder(getActivity());
        final RenrenConceptDialog dialog = builder.create();
        dialog.setOkBtnVisibility(true);
        dialog.setMessage("banjue", getResources().getColor(R.color.black));
        dialog.setPositiveButton("34", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordVerifyDialog();
            }
        });
        dialog.setNegativeButton("67", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setmDividerBg(getResources().getColor(R.color.common_gray_bg));
        dialog.setButtonBg(R.drawable.renren_dialog_btn_bg_selector_2, getResources().getColor(R.color.blue5));
        dialog.setDialogRoundBg();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 重测风险度变更后的快速调仓提醒
     */
    private void showTransferPositionDialog4() {
        RenrenConceptDialog.Builder builder = new RenrenConceptDialog.Builder(getActivity());
        final RenrenConceptDialog dialog = builder.create();
        dialog.setOkBtnVisibility(true);
        dialog.setMessage("quora", getResources().getColor(R.color.black));
        dialog.setPositiveButton("123", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TransferDetailsFragment.showForResult(getActivity(), portfolioData.transferType, 0);
            }
        });
        dialog.setNegativeButton("456", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setmDividerBg(getResources().getColor(R.color.common_gray_bg));
        dialog.setButtonBg(R.drawable.renren_dialog_btn_bg_selector_2, getResources().getColor(R.color.blue5));
        dialog.setDialogRoundBg();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private String mPassword;

    /**
     * 交易密码验证窗口
     */
    private void showPasswordVerifyDialog() {
        final AlertDialog.Builder build = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_password_input, null);
        build.setView(dialogView);
        final Dialog payPasswordDialog = build.show();
        final TextView tvDes = (TextView) dialogView.findViewById(R.id.input_pay_password_des);
        tvDes.setText(getResources().getString(R.string.input_trade_password_des));
        final GridPasswordView passwordView = (GridPasswordView) dialogView.findViewById(R.id.withdraw_cash_password);
        Button cancel = (Button) dialogView.findViewById(R.id.withdraw_cancel);
        final Button confirm = (Button) dialogView.findViewById(R.id.withdraw_confirm);
        confirm.setEnabled(true);
        confirm.setTextColor(context.getResources().getColor(R.color.common_gray_text_color_cccccc));

        passwordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onChanged(String psw) {
                mPassword = psw;
                if (!TextUtils.isEmpty(mPassword)) {
                    confirm.setTextColor(context.getResources().getColor(R.color.common_orange_text));
                } else {
                    confirm.setTextColor(context.getResources().getColor(R.color.common_gray_text_color_cccccc));
                }
            }

            @Override
            public void onMaxLength(String psw) {
                mPassword = psw;
                confirm.setEnabled(true);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payPasswordDialog.dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mPassword)) {
                    Methods.showToast("请输入六位交易密码");
                    return;
                }
                if (mPassword.length() < 6) {
                    Methods.showToast("请输入六位交易密码");
                    return;
                }
                payPasswordDialog.dismiss();
              /* ServiceProvider.transfer(mPassword, new INetResponse() {
                    @Override
                    public void response(INetRequest req, JsonValue obj) {
                        dismissProgressBar();
                        mPassword = "";
                        if (obj == null) {
                            return;
                        }
                        final JsonObject object = (JsonObject) obj;
                        passwordView.clearPassword();
                        if (ServiceError.noError(object, false)) {
                            loadNetworkDataOnResume = true;
                            TerminalActivity.showFragment(context, AssetsManagerFragment.class, null);
                        } else {//交易密码错误
                            Methods.showToast("交易密码错误");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showpasswordErrorDialog();
                                }
                            });

                        }

                    }
                });*/

            }
        });
        passwordView.postDelayed(new Runnable() {

            @Override
            public void run() {
                passwordView.performClick();
            }
        }, 250);
    }

    /**
     * 密码错误窗口
     */
    private void showpasswordErrorDialog() {
        RenrenConceptDialog.Builder builder = new RenrenConceptDialog.Builder(getActivity());
        final RenrenConceptDialog dialog = builder.create();
        dialog.setOkBtnVisibility(true);
        dialog.setMessage("password_error_prompt", getResources().getColor(R.color.black));
        dialog.setPositiveButton("12", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordVerifyDialog();
            }
        });
        dialog.setNegativeButton("back", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setmDividerBg(getResources().getColor(R.color.common_gray_bg));
        dialog.setButtonBg(R.drawable.renren_dialog_btn_bg_selector_2, getResources().getColor(R.color.blue5));
        dialog.setDialogRoundBg();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    private void setViewData() {

       /* topActionBar.showRightButton(portfolioData.isInvest);
        topActionBar.showRight2Button(portfolioData.isInvest);
        mListView.removeHeaderView(portfolioData.isInvest ? customHeaderView : tabHeaderView);
        mListView.addHeaderView(portfolioData.isInvest ? tabHeaderView : customHeaderView);
        if (portfolioData.isInvest) {
            tabHeaderView.setPortfolioData(PortfolioDataV3.convert2DetailData(portfolioData));
        } else {
            customHeaderView.setPortfolioData(portfolioData);
        }
        addFooterView();

        boAnimManager.setIsWorking(portfolioData.isInvest);
        investButton.setVisibility(portfolioData.isInvest ? View.VISIBLE : View.GONE);
        itemAdapter.setInvestState(portfolioData.isInvest);
        itemAdapter.setDataList(portfolioData.itemDataList);*/
    }

    private void checkPortfolioState() {
       /* Log.d(TAG, "checkPortfolioState :: isTimeValid = " + portfolioData.isTimeValid);
        Log.d(TAG, "checkPortfolioState :: isMsgShow = " + portfolioData.isMsgShow);
        Log.d(TAG, "checkPortfolioState :: transferType = " + portfolioData.transferType);
        Log.d(TAG, "checkPortfolioState :: hasTransfered = " + portfolioData.hasTransfered);
        Log.d(TAG, "checkPortfolioState :: isTransferEnd = " + portfolioData.isTransferEnd);*/

        // separate logic about page top banner.
       /* if (portfolioData.transferType == 3 && portfolioData.hasTransfered && !portfolioData.isTransferEnd) {
            pageTopBannerTextView.setVisibility(View.VISIBLE);
        } else {
            pageTopBannerTextView.setVisibility(View.GONE);
        }*/

        if (getActivity() instanceof MainTabHostActivity
                && ((MainTabHostActivity) getActivity()).smsTriggerTransferNotf) {
            //　从短信进入，不理会服务器下发的isMsgShow字段，强制弹提醒
          /*  if (portfolioData.isTimeValid && !portfolioData.hasTransfered) {
                showTransferPositionDialog3();
            } else {
                if (portfolioData.hasTransfered) {
                    Toast.makeText(getActivity(), getString(R.string.transfer_positions_message_immediate_has_transfered),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getString(R.string.transfer_positions_message_immediate_not_in_valid_time),
                            Toast.LENGTH_SHORT).show();
                }
            }
            ((MainTabHostActivity) getActivity()).smsTriggerTransferNotf = false;*/
        } else {
            // 非从短信进入，正常流程进行判断
          /*  switch (portfolioData.transferType) {
                case 1:
                    // normal transfer
                    if (portfolioData.isMsgShow && portfolioData.isTimeValid && !portfolioData.hasTransfered) {
                        showTransferPositionDialog1();
                    }
                    break;
                case 2:
                    // Quick Transfer after doing questionnaire again
                    if (portfolioData.isMsgShow) {
                        showTransferPositionDialog4();
                    }
                    break;
                case 3:
                    // stop bleeding
                    if (portfolioData.isMsgShow && portfolioData.isTimeValid && !portfolioData.hasTransfered) {
                        showTransferPositionDialog3();
                    }
                    break;
                default:
                    break;
            }*/
        }

//        TransferDetailsFragment.showForResult(getActivity(), 1, 0);
    }

    private void addFooterView() {
        if (!footerAdded) {
            mListView.addFooterView(footerView);
            footerAdded = true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.invest_btn:
                //在调仓期,未调仓
              /*  if (portfolioData.isTimeValid && !portfolioData.hasTransfered) {
                    showTransferPositionDialog2();
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putLong("portfolioId", portfolioData.portfolioId);
                ServiceProvider.isOpenAccount(Constant.REQUEST_BUY_COMBINE, bundle);*/
                break;
            default:
                break;
        }
    }

    void sharePortfolio() {
        Log.i("sharePortfolio","Right2");
     /*   String shareUrl = ShareConstants.SHARE_H5_MY_PORTFOLIO_URL + "?portfolioId=" + portfolioData.portfolioId;
        String shareTitle = getString(R.string.share_fund_title);
        String shareDes = portfolioData.name + "," + "近一年收益" + portfolioData.historyIncome + "%";
        Methods.share(context, shareUrl, shareTitle, shareDes, null);*/
    }

    @Override
    public void quoraFinish(Intent intent) {
//        riskScore = intent.getIntExtra(Constant.QUORA_SCORE, -1);
//        SettingManager.getInstance().setQuoraScore(riskScore);
        LogUtils.i(TAG, "quoraFinish");
        onLoadNetworkData();
    }

    public void onTabSelected() {
        if (isViewCreated) {
            onLoadNetworkData();
        }
    }



    /**
     * Adapter...
     */
    class ItemAdapter extends BaseAdapter {

        private boolean isInvest;

        private ArrayList<RelativeMemberData> itemDataList = new ArrayList<RelativeMemberData>();

        public ItemAdapter() {
        }

        @Override
        public int getCount() {
            return itemDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return itemDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        //
        @Override
        public int getItemViewType(int position) {
            return isInvest ? 0 : 1;
        }

        //
        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final RelativeMemberData fundItemData = itemDataList.get(position);
            ViewHolder riskViewHolder;
            ViewHolder investViewHolder;

            if (convertView == null) {
                if (isInvest) {
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_item_main_fund_v3, null);
                    investViewHolder = new ViewHolder();
                    investViewHolder.titleText = (TextView) convertView.findViewById(R.id.title_text);
                    investViewHolder.valueText = (CountTextView) convertView.findViewById(R.id.value_text);
                    investViewHolder.valueTitleText = (TextView) convertView.findViewById(R.id.value_title_text);
                    investViewHolder.circleTextView = (CircleTextView) convertView.findViewById(R.id.circle_text);
                    convertView.setTag(investViewHolder);

                  //  investViewHolder.titleText.setText(fundItemData.name);
                    //investViewHolder.valueText.setText(Methods.currencyFormat(fundItemData.proportion) + "%");
                  //  investViewHolder.valueText.setTextColor(fundItemData.color);
                   // investViewHolder.circleTextView.setText(fundItemData.investType.replace("型", ""));
                   // investViewHolder.circleTextView.setBackgroundColor(fundItemData.color);

                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(investViewHolder.circleTextView, scaleX, scaleY).setDuration(500).start();
                    ObjectAnimator.ofFloat(investViewHolder.titleText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(investViewHolder.valueText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(investViewHolder.valueTitleText, "alpha", 0f, 1f).setDuration(600).start();
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // FundDetailFragment.show(getActivity(), (int) fundItemData.id,fundItemData.code, fundItemData.name, fundItemData.investType.contains("货币"));
                        }
                    });
                } else {
                    convertView = LayoutInflater.from(context).inflate(R.layout.list_item_main_combination, null);
                    riskViewHolder = new ViewHolder();
                    riskViewHolder.titleText = (TextView) convertView.findViewById(R.id.title_text);
                    riskViewHolder.commentText = (TextView) convertView.findViewById(R.id.des_text);
                    riskViewHolder.combinationCountText = (TextView) convertView.findViewById(R.id.combination_count_text);
                    riskViewHolder.incomeHistoryText = (CountTextView) convertView.findViewById(R.id.income_history_text);
                    riskViewHolder.colorLayout = (RelativeLayout) convertView.findViewById(R.id.color_layout);
                    convertView.setTag(riskViewHolder);

                    //riskViewHolder.colorLayout.setBackgroundColor(fundItemData.color);
                    //riskViewHolder.incomeHistoryText.setText(Methods.doubleFormat(fundItemData.itemHistoryRate) + "%");
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.7f, 1f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.7f, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(riskViewHolder.colorLayout, scaleX, scaleY).setDuration(500).start();
                   // riskViewHolder.titleText.setText(fundItemData.name);
                   // riskViewHolder.commentText.setText(fundItemData.comment);
                   // riskViewHolder.combinationCountText.setText(String.valueOf(fundItemData.size) + "支基金");
                    ObjectAnimator.ofFloat(riskViewHolder.titleText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(riskViewHolder.commentText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(riskViewHolder.combinationCountText, "alpha", 0f, 1f).setDuration(600).start();
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // GoodFinancialDetailFragment.show(context, fundItemData.portfolioId, GoodFinancialDetailFragment.COMBINE_JUMP);
                        }
                    });
                }
            } else {
                if (isInvest) {
                    investViewHolder = (ViewHolder) convertView.getTag();
                    //investViewHolder.titleText.setText(fundItemData.name);
                    //investViewHolder.valueText.setText(Methods.currencyFormat(fundItemData.proportion) + "%");
                    //investViewHolder.valueText.setTextColor(fundItemData.color);
                    //investViewHolder.circleTextView.setText(fundItemData.investType.replace("型", ""));
                    //investViewHolder.circleTextView.setBackgroundColor(fundItemData.color);

                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(investViewHolder.circleTextView, scaleX, scaleY).setDuration(500).start();
                    ObjectAnimator.ofFloat(investViewHolder.titleText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(investViewHolder.valueText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(investViewHolder.valueTitleText, "alpha", 0f, 1f).setDuration(600).start();
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          //  FundDetailFragment.show(getActivity(), (int) fundItemData.id, fundItemData.code, fundItemData.name, fundItemData.investType.contains("货币"));
                        }
                    });
                } else {
                    riskViewHolder = (ViewHolder) convertView.getTag();
                  // riskViewHolder.colorLayout.setBackgroundColor(fundItemData.color);
                  // riskViewHolder.incomeHistoryText.setText(Methods.doubleFormat(fundItemData.itemHistoryRate) + "%");
                    PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0.7f, 1f);
                    PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0.7f, 1f);
                    ObjectAnimator.ofPropertyValuesHolder(riskViewHolder.colorLayout, scaleX, scaleY).setDuration(500).start();
                    //riskViewHolder.titleText.setText(fundItemData.name);
                    //riskViewHolder.commentText.setText(fundItemData.comment);
                   // riskViewHolder.combinationCountText.setText(String.valueOf(fundItemData.size) + "支基金");
                    ObjectAnimator.ofFloat(riskViewHolder.titleText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(riskViewHolder.commentText, "alpha", 0f, 1f).setDuration(600).start();
                    ObjectAnimator.ofFloat(riskViewHolder.combinationCountText, "alpha", 0f, 1f).setDuration(600).start();
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           // GoodFinancialDetailFragment.show(context, fundItemData.portfolioId, GoodFinancialDetailFragment.COMBINE_JUMP);
                        }
                    });
                }
            }

            return convertView;
        }

        public void setDataList(ArrayList<RelativeMemberData> itemDataList) {
            this.itemDataList = itemDataList;
            notifyDataSetChanged();
        }

        public void setInvestState(boolean isInvest) {
            this.isInvest = isInvest;
        }
    }


    private class ViewHolder {
        //Risk
        public TextView titleText;
        public TextView commentText;
        public TextView combinationCountText;
        public CountTextView incomeHistoryText;
        public RelativeLayout colorLayout;
        //Invest
        public CircleTextView circleTextView;
        public CountTextView valueText;
        public TextView valueTitleText;
    }


    /* XListView onRefreshListener... */
    @Override
    public void onRefresh() {
        LogUtils.i(TAG, "onRefresh");
        onLoadNetworkData();
    }

    @Override
    public void onLoadMore() {
    }

    @Override
    public void onDeleteItem(int index) {
    }

    private boolean measure = false;

    /* XListView OnScrollListener... */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (!measure) {
            boAnimManager.setAnimationView(investButton);
            measure = true;
        }
        if (scrollState == SCROLL_STATE_IDLE) { //stop
            boAnimManager.startAppearAnimation();
        } else {                                // scrolling
            boAnimManager.startHiddenAnimation();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            LogUtils.i(TAG, "onActivityResult RESULT_OK ... ");
            onLoadNetworkData();
        }
    }

    public class RefreshReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtils.i(TAG, "RefreshReceiver Get ... " + action);
            if (Constant.REFRESH_MAIN_TAB.equals(action) || Constant.REFRESH_ALL_TAB.equals(action)) {
                onLoadNetworkData();
            }
        }
    }
}
