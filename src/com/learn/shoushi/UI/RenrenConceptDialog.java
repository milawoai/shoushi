package com.learn.shoushi.UI;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.learn.shoushi.R;
import com.learn.shoushi.Utils.AppMethods;
import com.learn.shoushi.Utils.AppMethods;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by a0153-00401 on 15/11/15.
 */
public class RenrenConceptDialog extends Dialog {

    private Context mContext;

    private LayoutInflater mInflater;

    private View mDialogView;

    private ListView mListView;
    private View mContentView;
    private View mButtonView;
    private TextView mTitleView;
    private TextView mMessageView;
    private SearchEditText mEditText;
    private View mCheckView;
    private CheckBox mCheckBox;
    private TextView mCheckMessageView;
    private Button mCancelBtn;
    private Button mOkBtn;
    private View mBtnDivider;
    private View mDiliver;

    private android.view.View.OnClickListener mNegativeBtnClickListener;
    private android.view.View.OnClickListener mPositiveBtnClickListener;

    private BinderOnClickListener mNegativeBinderOnClickListener;
    private BinderOnClickListener mPositiveBinderOnClickListener;

    private Binder mBinder;

    public RenrenConceptDialog(Context context, int theme) {
        super(context, theme);
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView(mInflater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(mDialogView);
    }

    private void initView(LayoutInflater inflater) {
        mDialogView = inflater.inflate(R.layout.renren_dialog_layout, null);
        mContentView = mDialogView.findViewById(R.id.renren_dialog_content_layout);
        mButtonView = mDialogView.findViewById(R.id.button_view);
        mTitleView = (TextView) mDialogView.findViewById(R.id.renren_dialog_title_view);
        mMessageView = (TextView) mDialogView.findViewById(R.id.renren_dialog_message_view);
        mEditText = (SearchEditText) mDialogView.findViewById(R.id.renren_dialog_edit_text);
        mCheckView = mDialogView.findViewById(R.id.renren_dialog_check_layout);
        mCheckBox = (CheckBox) mDialogView.findViewById(R.id.renren_dialog_check_box);
        mCheckMessageView = (TextView) mDialogView.findViewById(R.id.renren_dialog_check_message_view);
        mCancelBtn = (Button) mDialogView.findViewById(R.id.renren_dialog_cancel_btn);
        mOkBtn = (Button) mDialogView.findViewById(R.id.renren_dialog_ok_btn);

        mBtnDivider = mDialogView.findViewById(R.id.renren_dialog_btn_divider);
        mDiliver = mDialogView.findViewById(R.id.renren_dialog_divider);
        mListView = (ListView) mDialogView.findViewById(R.id.renren_dialog_list_view);
        mListView.setVerticalFadingEdgeEnabled(false);
        mListView.setScrollingCacheEnabled(false);
        mListView.setCacheColorHint(0);


        mEditText.setIsShowLeftIcon(false);
        mCancelBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppMethods.hideSoftInputMethods(mEditText);
                dismiss();
                if (mNegativeBtnClickListener != null) {
                    mNegativeBtnClickListener.onClick(v);
                }
                if (mNegativeBinderOnClickListener != null
                        && mBinder != null) {
                    mNegativeBinderOnClickListener.OnClick(v, mBinder);
                }
            }
        });

        mOkBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppMethods.hideSoftInputMethods(mEditText);
                dismiss();
                if (mPositiveBtnClickListener != null) {
                    mPositiveBtnClickListener.onClick(v);
                }
                if (mPositiveBinderOnClickListener != null
                        && mBinder != null) {
                    mPositiveBinderOnClickListener.OnClick(v, mBinder);
                }
            }
        });
    }

    public void setCancleBtnVisibility(boolean visible) {
        if (visible) {
            mCancelBtn.setVisibility(View.VISIBLE);
        } else {
            mCancelBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 当只显示ok或只显示cancle时不需要显示中间的divider
     */
    public boolean isNeedShowBtnDivider() {
        if (mCancelBtn.getVisibility() == View.VISIBLE
                && mOkBtn.getVisibility() == View.VISIBLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设置两按钮中间的divider的可见性
     */
    public void setBtnDividerVisibility(boolean visible) {
        if (visible) {
            mBtnDivider.setVisibility(View.VISIBLE);
        } else {
            mBtnDivider.setVisibility(View.GONE);
        }
    }

    /**
     * 设置两按钮中间的divider的可见性
     */
    public void setmDividerBg(int color) {
        mDiliver.setBackgroundColor(color);
    }

    /**
     * 设置两按钮的背景色和字体色
     */
    public void setButtonBg(int res, int color) {
        mOkBtn.setTextColor(color);
        mCancelBtn.setTextColor(color);
        mOkBtn.setBackgroundResource(res);
        mCancelBtn.setBackgroundResource(res);
    }

    /**
     * 最简单的dialogure设置圆角背景
     * 在没有title的时候使用
     */
    public void setDialogRoundBg() {
        setMessage(null,16);
        mContentView.setBackgroundResource(R.drawable.common_radius_top_white_background);
        mOkBtn.setBackgroundResource(R.drawable.common_radius_bottom_right_white_background);
        mCancelBtn.setBackgroundResource(R.drawable.common_radius_bottom_lift_white_background);
        mButtonView.setBackgroundResource(R.drawable.common_radius_bottom_white_background);
    }

    public final void setOkBtnVisibility(boolean visible) {
        if (visible) {
            mOkBtn.setVisibility(View.VISIBLE);
        } else {
            mOkBtn.setVisibility(View.GONE);
        }
    }

    public void setTitle(String title) {
        mTitleView.setVisibility(View.VISIBLE);
        mTitleView.setText(title);
    }

    public void setMessage(String message) {
        setMessage(message, 0, 0);
    }

    public void setMessage(String message, int size) {
        setMessage(message, size, 0);
    }

    public void setMessage(String message, int size, int color) {
        mMessageView.setVisibility(View.VISIBLE);
        if (message != null) {
            mMessageView.setText(message);
        }
        if (size != 0) {
            mMessageView.setTextSize(size);
        }
        if (color != 0) {
            mMessageView.setTextColor(color);
        }
    }

    public void setEditText(String text, String hintText, int leftIconResId) {
        mEditText.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(hintText)) {
            mEditText.setHint(hintText);
        }
        if (!TextUtils.isEmpty(text)) {
            mEditText.setText(text);
        }
        if (leftIconResId != 0) {
            mEditText.setIsShowLeftIcon(true);
            mEditText.setLeftIcon(leftIconResId);
        }
    }

    public void setCheckMessage(String checkMessage) {
        mCheckView.setVisibility(View.VISIBLE);
        mCheckMessageView.setText(checkMessage);
    }

    public void setCheckBoxChecked(boolean isChecked) {
        mCheckBox.setChecked(isChecked);
    }

    public void setItems(String[] items, final AdapterView.OnItemClickListener onItemClickListener) {
        setItems(items, onItemClickListener, null);
    }

    /**
     * @param items
     * @param onItemClickListener
     * @param specialIndexes      需要标红的item的索引（从上到下从0计数）
     */
    public void setItems(String[] items, final AdapterView.OnItemClickListener onItemClickListener, int[] specialIndexes) {
        mContentView.setVisibility(View.GONE);
        mListView.setVisibility(View.VISIBLE);

        mListView.setAdapter(new RenrenDialogListAdapter(items, specialIndexes));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dismiss();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    public void setNegativeButton(String text, android.view.View.OnClickListener onClickListener) {
        if (!TextUtils.isEmpty(text)) {
            mCancelBtn.setText(text);
        }
        mNegativeBtnClickListener = onClickListener;
    }

    public void setPositiveButton(String text, android.view.View.OnClickListener onClickListener) {
        mOkBtn.setVisibility(View.VISIBLE);
        mBtnDivider.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) {
            mOkBtn.setText(text);
        }
        mPositiveBtnClickListener = onClickListener;
    }

    public void setNegativeBinderButton(BinderOnClickListener onClickListener, Binder binder) {
        mNegativeBinderOnClickListener = onClickListener;
        mBinder = binder;
    }

    public void setPositiveBinderButton(BinderOnClickListener onClickListener, Binder binder) {
        mOkBtn.setVisibility(View.VISIBLE);
        mBtnDivider.setVisibility(View.VISIBLE);
        mPositiveBinderOnClickListener = onClickListener;
        mBinder = binder;
    }

    public boolean getCheckBoxState() {
        return mCheckBox.isChecked();
    }

    public String getEditTextInputString() {
        return mEditText.getEditableText().toString();
    }

    private class RenrenDialogListAdapter extends BaseAdapter {

        private String[] mItems;

        private Set<Integer> mSpecialItems = new HashSet<Integer>();

        public RenrenDialogListAdapter(String[] items, int[] specialItems) {
            mItems = items;
            if (specialItems != null) {
                for (int i : specialItems) {
                    mSpecialItems.add(i);
                }
            }
        }

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public String getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.renren_dialog_list_item, null);
            ((TextView) convertView).setText(getItem(position));
            if (mSpecialItems.size() > 0 && mSpecialItems.contains(position)) {
                ((TextView) convertView).setBackgroundResource(R.drawable.renren_dialog_list_special_item_bg_selector);
                ((TextView) convertView).setTextColor(mContext.getResources().getColorStateList(R.drawable.renren_dialog_list_special_item_text_selector));
            }
            return convertView;
        }

    }

    public static class Builder {

        private Context mContext;

        private String mTitleString;

        private String mMessageString;

        private String mCheckMessageString;

        private boolean isShowEditText = false;

        private String mEditTextContent;

        private String mEditTextHint;

        private int mEditTextLeftIconResId;

        private String[] mItemsStrings;

        private int[] mSpecialIndexes;

        private String mNegativeBtnTextString;
        private android.view.View.OnClickListener mNegativeClickListener;

        private BinderOnClickListener mNegativeBinderOnClickListener;

        private String mPositiveBtnTextString;
        private android.view.View.OnClickListener mPositiveClickListener;

        private BinderOnClickListener mPositiveBinderOnClickListener;

        private AdapterView.OnItemClickListener mOnItemClickListener;

        private OnCancelListener mOnCancelListener;

        private boolean isCanceledOnTouchOutside = true;

        private boolean checkBoxDefaultState = false;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setTitle(int resId) {
            mTitleString = mContext.getResources().getString(resId);
            return this;
        }

        public Builder setTitle(String title) {
            mTitleString = title;
            return this;
        }

        public Builder setMessage(int resId) {
            mMessageString = mContext.getResources().getString(resId);
            return this;
        }

        public Builder setMessage(String message) {
            mMessageString = message;
            return this;
        }

        /**
         * 显示一个输入框
         *
         * @param text          输入框要显示的内容，没有则传null
         * @param hintText      输入框要显示的默认文案，没有则传null
         * @param leftIconResId 输入框内部左侧显示的icon的资源ID，没有则传0
         * @return
         */
        public Builder setEditText(String text, String hintText, int leftIconResId) {
            isShowEditText = true;
            mEditTextContent = text;
            mEditTextHint = hintText;
            mEditTextLeftIconResId = leftIconResId;
            return this;
        }

        public Builder setCheckMessage(int resId) {
            mCheckMessageString = mContext.getResources().getString(resId);
            return this;
        }

        public Builder setCheckMessage(String checkMessage) {
            mCheckMessageString = checkMessage;
            return this;
        }

        public Builder setItems(String[] items, AdapterView.OnItemClickListener onItemClickListener) {
            mItemsStrings = items;
            mOnItemClickListener = onItemClickListener;
            return this;
        }

        public Builder setItems(String[] items, AdapterView.OnItemClickListener onItemClickListener, int[] specialIndexes) {
            mSpecialIndexes = specialIndexes;
            return this.setItems(items, onItemClickListener);
        }

        public Builder setNegativeButton(int stringId, android.view.View.OnClickListener onClickListener) {
            mNegativeBtnTextString = mContext.getResources().getString(stringId);
            mNegativeClickListener = onClickListener;
            return this;
        }

        public Builder setNegativeButton(String text, android.view.View.OnClickListener onClickListener) {
            mNegativeBtnTextString = text;
            mNegativeClickListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(int stringId, android.view.View.OnClickListener onClickListener) {
            mPositiveBtnTextString = mContext.getResources().getString(stringId);
            mPositiveClickListener = onClickListener;
            return this;
        }

        public Builder setPositiveButton(String text, android.view.View.OnClickListener onClickListener) {

            mPositiveBtnTextString = text;
            mPositiveClickListener = onClickListener;
            return this;
        }

        public Builder setNegativeBinderButton(int stringId, BinderOnClickListener onClickListener) {
            mNegativeBtnTextString = mContext.getResources().getString(stringId);
            mNegativeBinderOnClickListener = onClickListener;
            return this;
        }

        public Builder setNegativeBinderButton(String text, BinderOnClickListener onClickListener) {
            mNegativeBtnTextString = text;
            mNegativeBinderOnClickListener = onClickListener;
            return this;
        }

        public Builder setPositiveBinderButton(int stringId, BinderOnClickListener onClickListener) {
            mPositiveBtnTextString = mContext.getResources().getString(stringId);
            mPositiveBinderOnClickListener = onClickListener;
            return this;
        }

        public Builder setPositiveBinderButton(String text, BinderOnClickListener onClickListener) {
            mPositiveBtnTextString = text;
            mPositiveBinderOnClickListener = onClickListener;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setCanceledOnTouchOutside(boolean cancel) {
            this.isCanceledOnTouchOutside = cancel;
            return this;
        }

        public Builder setCheckBoxDefaultState(boolean isChecked) {
            this.checkBoxDefaultState = isChecked;
            return this;
        }

        public RenrenConceptDialog create() {
            RenrenConceptDialog dialog = new RenrenConceptDialog(mContext, R.style.RenrenConceptDialog);

            if (!TextUtils.isEmpty(mTitleString)) {
                dialog.setTitle(mTitleString);
            }

            if (!TextUtils.isEmpty(mMessageString)) {
                dialog.setMessage(mMessageString);
            }

            if (isShowEditText) {
                dialog.setEditText(mEditTextContent, mEditTextHint, mEditTextLeftIconResId);
            }

            if (!TextUtils.isEmpty(mCheckMessageString)) {
                dialog.setCheckMessage(mCheckMessageString);
            }

            if (mItemsStrings != null && mItemsStrings.length > 0 && mOnItemClickListener != null) {
                dialog.setItems(mItemsStrings, mOnItemClickListener, mSpecialIndexes);
            }

            if (!TextUtils.isEmpty(mNegativeBtnTextString)
                    || mNegativeClickListener != null
                    || mNegativeBinderOnClickListener != null) {

                dialog.setNegativeButton(mNegativeBtnTextString, mNegativeClickListener);

                if (mNegativeBinderOnClickListener != null) {
                    dialog.setNegativeBinderButton(mNegativeBinderOnClickListener, new Binder(dialog));
                }
            }

            if (!TextUtils.isEmpty(mPositiveBtnTextString)
                    || mPositiveClickListener != null
                    || mPositiveBinderOnClickListener != null) {

                dialog.setPositiveButton(mPositiveBtnTextString, mPositiveClickListener);

                if (mPositiveBinderOnClickListener != null) {
                    dialog.setPositiveBinderButton(mPositiveBinderOnClickListener, new Binder(dialog));
                }
            }

            if (mOnCancelListener != null) {
                dialog.setOnCancelListener(mOnCancelListener);
            }

            dialog.setCanceledOnTouchOutside(isCanceledOnTouchOutside);

            dialog.setCheckBoxChecked(checkBoxDefaultState);

            return dialog;
        }
    }

    public static interface BinderOnClickListener {
        void OnClick(View v, Binder binder);
    }

    public static class Binder {

        private RenrenConceptDialog mDialog;

        public Binder(RenrenConceptDialog dialog) {
            this.mDialog = dialog;
        }

        public boolean getCheckBoxState() {
            if (mDialog != null) {
                return mDialog.getCheckBoxState();
            }
            return false;
        }

        public String getEditTextString() {
            if (mDialog != null) {
                return mDialog.getEditTextInputString();
            }
            return null;
        }
    }

    //  added by gaozhan.zhang 2013-9-4
    public SearchEditText getEditText() {
        return mEditText;
    }

}
