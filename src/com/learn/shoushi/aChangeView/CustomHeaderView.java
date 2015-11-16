package com.learn.shoushi.aChangeView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.Methods;

import java.text.DecimalFormat;

/**
 * Created by a0153-00401 on 15/11/13.
 */
public class CustomHeaderView extends RelativeLayout {

    private final String TAG = "CustomHeaderView";

    protected int layoutResId = R.layout.custom_header_layout;

    private Context context;
    private LayoutInflater inflater;

    private ImageView headerRecyclingImage;

    private TextView titleText;
    private TextView rateTitleText;
    private CountTextView historyRateText;
    private Button functionButton;

    private LinearLayout desLayout;
    private TextView investmentDes;
    private LinearLayout incomeLayout;
    private CountTextView riskDegreeText;
    private TextView incomeRateTitleText;

    private View headerDivisionView;

   // private PortfolioDataV3 portfolioData;

    public CustomHeaderView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public CustomHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CustomHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    void initView() {
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResId, this);

        headerRecyclingImage = (ImageView) findViewById(R.id.header_image);
        titleText = (TextView) findViewById(R.id.title_text);
        rateTitleText = (TextView) findViewById(R.id.rate_title_text);
        historyRateText = (CountTextView) findViewById(R.id.history_rate_text);
        historyRateText.setShowType(CountTextView.PERCENT_TYPE_DECIMAL_2);
        functionButton = (Button) findViewById(R.id.fun_btn);

        desLayout = (LinearLayout) findViewById(R.id.des_layout);
        investmentDes = (TextView) findViewById(R.id.investment_des);
        incomeLayout = (LinearLayout) findViewById(R.id.income_layout);
        riskDegreeText = (CountTextView) findViewById(R.id.risk_degree_text);
        incomeRateTitleText = (TextView) findViewById(R.id.income_rate_title_text);

        headerDivisionView = findViewById(R.id.header_division_view);

        performViewClick();
    }

    void refreshView() {
        refreshQuoraState();

      //  Methods.setAutoAttachRecyclingImageView(headerRecyclingImage, portfolioData.themePicDataList.get(0).headerImageUrl);
    }

    void refreshQuoraState() {

       /* if (portfolioData.isRisk) { //已测试
            titleText.setText(context.getResources().getString(R.string.my_custom_combination));
            rateTitleText.setText(context.getResources().getString(R.string.benefit_rate_6_month));
            historyRateText.showNumberWithAnimation((float) portfolioData.myHistoryIncome);
            functionButton.setText(context.getResources().getString(R.string.buy_come_on));

            incomeLayout.setVisibility(VISIBLE);
            investmentDes.setText("根据测试结果");
            riskDegreeText.setText(new DecimalFormat("###0.0").format(portfolioData.myRiskScore));
            incomeRateTitleText.setText("适合" + Methods.getManType(portfolioData.myRiskScore));

        } else {
            titleText.setText(context.getResources().getString(R.string.my_custom_combination));
            rateTitleText.setText(context.getResources().getString(R.string.benefit_rate_average));
            functionButton.setText(context.getResources().getString(R.string.custom_come_on));
            incomeLayout.setVisibility(GONE);
            historyRateText.showNumberWithAnimation((float) portfolioData.historyRate);
        }*/
    }


    void performViewClick() {
        functionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onViewClickListener != null) {
                    onViewClickListener.onFunctionButtonClick(view);
                }
            }
        });

        desLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onViewClickListener != null) {
                    onViewClickListener.onDescriptionLayoutClick(view);
                }
            }
        });
    }

    private OnViewClickListener onViewClickListener;

    public interface OnViewClickListener {
        void onFunctionButtonClick(View view);

        void onDescriptionLayoutClick(View view);
    }

    public void setViewClickListener(OnViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }

    //没内容别显示灰的分隔线
    public void showDivisionView(boolean is2Show) {
        if (is2Show) {
            headerDivisionView.setVisibility(VISIBLE);
        } else {
            headerDivisionView.setVisibility(GONE);
        }
    }

  /*  public void setPortfolioData(PortfolioDataV3 portfolioData) {
        this.portfolioData = portfolioData;
        refreshView();
    }*/

}
