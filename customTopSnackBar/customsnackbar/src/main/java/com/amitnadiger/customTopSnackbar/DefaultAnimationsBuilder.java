package com.amitnadiger.customTopSnackbar;



import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


final public class DefaultAnimationsBuilder {
    private static final long DURATION = 400;
    private static Animation slideInDownAnimation, slideOutUpAnimation;
    private static int lastInAnimationHeight, lastOutAnimationHeight;

    private DefaultAnimationsBuilder() {
        /* no-op */
    }

    static Animation buildDefaultSlideInDownAnimation(View snackBarView) {
        if (!areLastMeasuredInAnimationHeightAndCurrentEqual(snackBarView) || (slideInDownAnimation != null)) {
            slideInDownAnimation = new TranslateAnimation(
                    0, 0,  // X: from, to
                    -snackBarView.getMeasuredHeight(), 0); // Y: from, to
            slideInDownAnimation.setDuration(DURATION);
            setLastInAnimationHeight(snackBarView.getMeasuredHeight());
        }
        return slideInDownAnimation;
    }

    static Animation buildDefaultSlideOutUpAnimation(View snackBarView) {
        if (!areLastMeasuredOutAnimationHeightAndCurrentEqual(snackBarView) || (slideOutUpAnimation != null)) {
            slideOutUpAnimation = new TranslateAnimation(
                    0, 0,   // X: from, to
                    0, -snackBarView.getMeasuredHeight()  // Y: from, to
            );
            slideOutUpAnimation.setDuration(DURATION);

            setLastOutAnimationHeight(snackBarView.getMeasuredHeight());
        }
        return slideOutUpAnimation;
    }

    private static boolean areLastMeasuredOutAnimationHeightAndCurrentEqual(View snackBarView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastOutAnimationHeight, snackBarView);
    }

    private static boolean areLastMeasuredInAnimationHeightAndCurrentEqual(View SnackbarView) {
        return areLastMeasuredAnimationHeightAndCurrentEqual(lastInAnimationHeight, SnackbarView);
    }

    private static boolean areLastMeasuredAnimationHeightAndCurrentEqual(int lastHeight, View snackBarView) {
        return lastHeight == snackBarView.getMeasuredHeight();
    }

    private static void setLastInAnimationHeight(int lastInAnimationHeight) {
        DefaultAnimationsBuilder.lastInAnimationHeight = lastInAnimationHeight;
    }

    private static void setLastOutAnimationHeight(int lastOutAnimationHeight) {
        DefaultAnimationsBuilder.lastOutAnimationHeight = lastOutAnimationHeight;
    }
}

