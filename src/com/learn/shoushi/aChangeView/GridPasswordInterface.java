package com.learn.shoushi.aChangeView;

/**
 * Created by a0153-00401 on 15/11/15.
 */
interface GridPasswordInterface {

    //void setError(String error);

    String getPassWord();

    void clearPassword();

    void setPassword(String password);

    void setPasswordVisibility(boolean visible);

    void togglePasswordVisibility();

    void setOnPasswordChangedListener(GridPasswordView.OnPasswordChangedListener listener);

    void setPasswordType(GridPasswordType passwordType);
}
