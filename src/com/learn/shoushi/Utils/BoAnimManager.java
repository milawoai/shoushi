package com.learn.shoushi.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * Created by a0153-00401 on 15/11/16.
 */
public class BoAnimManager {

    private boolean isWorking = false;

    private ValueAnimator appearAnim;
    private ValueAnimator hiddenAnim;
    private boolean isHiding = false;

    public void setAnimationView(final View animationView) {

        hiddenAnim = ValueAnimator.ofFloat(0, animationView.getHeight());
        hiddenAnim.setDuration(600);
        hiddenAnim.setTarget(animationView);
        hiddenAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        hiddenAnim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                isHiding = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }
        });

        appearAnim = ValueAnimator.ofFloat(animationView.getHeight(), 0);
        appearAnim.setDuration(600);
        appearAnim.setTarget(animationView);
        appearAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationView.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

        appearAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isHiding = false;
            }

            @Override
            public void onAnimationStart(Animator animation) {
            }
        });
    }

    public void startHiddenAnimation() {
        if (isWorking && !hiddenAnim.isRunning() && !isHiding) {
            hiddenAnim.start();
        }
    }

    public void startAppearAnimation() {
        if (isWorking && !appearAnim.isRunning()) {
            appearAnim.start();
        }
    }

    public void setIsWorking(boolean isWorking) {
        this.isWorking = isWorking;
    }
}
