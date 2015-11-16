package com.learn.shoushi.aChangeView;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.Constant;
import com.learn.shoushi.Utils.LogUtils;
import com.learn.shoushi.Utils.Methods;
import com.learn.shoushi.appManager.HelperApplication;
import com.learn.shoushi.userManager.RelativeMemberData;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by a0153-00401 on 15/11/13.
 */
public class TabHeaderView extends RelativeLayout {

    public Context context;

    private PopupWindow popupWindow;
    private LinearLayout headerTopLayout;
   // private PieChart mPieChart;
    //private PieChartConfig pieChartConfig;
    private TextView financeProduct;
    private TextView historyTitle;
    private CountTextView historyRate;
   // private PortfolioDetailDataV3 portfolioData;
    private TextView riskText;
    private CountTextView riskScore;
    private LinearLayout assetLayout;
    private TextView riskComment;
    private ImageView issueImage;
    private LinearLayout riskLayout;
    private LinearLayout historyLayout;
    //    private TextView mBuyDate;
    private TextView transferNotice;

    private RelativeMemberData relativeMemberData;

   // private int JumpType = GoodFinancialDetailFragment.QUORA_JUMP;

    public TabHeaderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TabHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public TabHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {

        LayoutInflater.from(context).inflate(R.layout.tab_header_layout, this);
        headerTopLayout = (LinearLayout) findViewById(R.id.header_top_layout);
        //mPieChart = (PieChart) findViewById(R.id.pie_chart);
        // mPieChart.setTouchEnabled(false);
        // mPieChart.setNoDataText("");
        financeProduct = (TextView) findViewById(R.id.finance_product);
        historyTitle = (TextView) findViewById(R.id.history_title);
//        mBuyDate = (TextView) findViewById(R.id.fund_buy_time);
        historyRate = (CountTextView) findViewById(R.id.history_rate);
        historyRate.setShowType(CountTextView.PERCENT_TYPE_DECIMAL_2);
        assetLayout = (LinearLayout) findViewById(R.id.fund_type_layout);
        riskComment = (TextView) findViewById(R.id.risk_comment);
        riskText = (TextView) findViewById(R.id.risk_text);
        riskScore = (CountTextView) findViewById(R.id.risk_score);
        issueImage = (ImageView) findViewById(R.id.issue_image);
        riskLayout = (LinearLayout)findViewById(R.id.risk_layout);
        historyLayout = (LinearLayout)findViewById(R.id.history_layout);

        transferNotice = (TextView) findViewById(R.id.tranfer_position_notice);
        performViewClick();
    }

    /**
     * 调仓提醒
     *
     * @param visibilyty
     */
    public void setNoticeVisibilyty(boolean visibilyty) {
        if (visibilyty) {
            transferNotice.setVisibility(VISIBLE);
        } else {
            transferNotice.setVisibility(GONE);
        }
    }

    private void performViewClick() {
//        headerTopLayout.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LogUtils.logInfo("TabHeaderView", "headerTopLayout Click");
//                if (portfolioData.isInvest) {
//                    AssetsManagerFragment.show(context);
//                }
//            }
//        });

        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.logInfo("TabHeaderView", "historyLayout Click");
              /*  if (portfolioData.isInvest) {
                    AssetsManagerFragment.show(context);
                }*/
            }
        });

        issueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.logInfo("TabHeaderView", "issueImage Click");
                showPopupWindow(v);
            }
        });
        riskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    Bundle bundle = new Bundle();
                bundle.putString(WebFragment.ARGS_STRING_URL, Constant.SERVICE_TERMS);
                bundle.putString(WebFragment.ARGS_STRING_TITLE,context.getString(R.string.test_result));
                bundle.putBoolean("isInvest", false);
                WebFragment.show(context,bundle);*/
            }
        });

    }

    public View getViewById(int id) {
        return findViewById(id);
    }

    private void showPopupWindow(View view) {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.fund_popwindow_layout, null);
            TextView textView = (TextView) contentView.findViewById(R.id.fund_popwindow_text);
           // textView.setText(portfolioData.notice);
            popupWindow = new PopupWindow(contentView, Methods.dp2px(HelperApplication.getContext(), 155), RelativeLayout.LayoutParams.WRAP_CONTENT);
            //设置点击窗口外边窗口消失
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setAnimationStyle(R.style.popupwindow_anim_style);
        }

        popupWindow.showAsDropDown(view, Methods.dp2px(context, -70), 10);
    }

    /*private void initPieChart() {
        pieChartConfig = new PieChartConfig();
        pieChartConfig.animationShowed = true;
        pieChartConfig.pieRadiusPercent = 30f;
        pieChartConfig.arrayList = initBasePieChartList();
        ChartUtil.initPieChart(mPieChart, pieChartConfig);
    }*/

  /*  private ArrayList<BasePieChartData> initBasePieChartList() {
        ArrayList<BasePieChartData> arrayList = new ArrayList<BasePieChartData>();
        Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (int i = 0; i < portfolioData.fundDataList.size(); i++) {
            PortfolioDetailDataV3.FundItem itemData = portfolioData.fundDataList.get(i);
            if (map.containsKey(itemData.color)) {
                map.put(itemData.color, map.get(itemData.color) + itemData.proportion);
            } else {
                map.put(itemData.color, itemData.proportion);
            }
        }
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            BasePieChartData data = new BasePieChartData();
            Integer key = (Integer) iterator.next();
            data.itemName = "";
            data.itemColor = key;
            data.itemPercent = (float) (double) map.get(key);
            arrayList.add(data);
        }
        return arrayList;
    }*/

    private void refreshView() {
       /* if (portfolioData == null)
            return;
        financeProduct.setText(portfolioData.combinationName);
        // historyRate.showNumberWithAnimation((float) portfolioData.historyIncome);

        int color = R.color.common_black_text;
        if (portfolioData.userIncome > 0) {
            color = R.color.common_orange_text;
        } else if (portfolioData.userIncome < 0) {
            color = R.color.common_green_text;
        }

        historyRate.setTextColor(getResources().getColor(color));
        historyRate.showNumberWithAnimation((float) portfolioData.userIncome);
        if (portfolioData.riskScore == -1) {
            riskText.setVisibility(GONE);
            riskScore.setVisibility(GONE);
        } else {
            riskScore.setText(new DecimalFormat("###0.0").format(portfolioData.riskScore));
        }

        if (JumpType == GoodFinancialDetailFragment.COMBINE_JUMP) {
            riskComment.setText(portfolioData.comment);
            issueImage.setVisibility(GONE);
            riskText.setVisibility(GONE);
            riskScore.setVisibility(GONE);
        } else {
            riskComment.setText("适合" + Methods.getManType(portfolioData.riskScore));
            issueImage.setVisibility(VISIBLE);
            riskText.setVisibility(VISIBLE);
        }

        if (assetLayout.getChildCount() == 0) {
            addAssetView(portfolioData.fundDataList, assetLayout);
        }

        if (portfolioData.isInvest) {
            // historyTitle.setText(R.string.trend_income_actual_rate);
            historyTitle.setText(R.string.trend_income_user_rate);

//            mBuyDate.setText(Methods.timeLong2String3(portfolioData.buyDate));
        } else {
            historyTitle.setText(R.string.trend_income_history_rate);
        }

        // initPieChart();*/
    }

    public void addAssetView(List<RelativeMemberData> list, LinearLayout parent) {
        ArrayList<Integer> colorArray = new ArrayList<Integer>();
        LayoutInflater inflater = LayoutInflater.from(context);
        for (int i = 0; i < list.size(); i++) {
            View view = inflater.inflate(R.layout.cousel_fund_asset_type_item, null);

            CircleView circleView = (CircleView) view.findViewById(R.id.textView);
            TextView assetName = (TextView) view.findViewById(R.id.asset_name);
            RelativeMemberData itemData = list.get(i);
           /* if (colorArray.contains(itemData.color))
                continue;
            colorArray.add(itemData.color);
            circleView.setColor(itemData.color);
            assetName.setText(itemData.investType.replace("型", ""));*/
            parent.addView(view);
        }
    }

    public void setPortfolioData(RelativeMemberData relativeMemberData) {
        this.relativeMemberData = relativeMemberData;
        refreshView();
    }

   /* public void setJumpType(int JumpType) {
        this.JumpType = JumpType;
    }*/
}