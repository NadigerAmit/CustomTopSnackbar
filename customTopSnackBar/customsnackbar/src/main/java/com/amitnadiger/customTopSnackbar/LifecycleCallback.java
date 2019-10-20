package com.amitnadiger.customTopSnackbar;


public interface LifecycleCallback {
    /**
     * Will be called when your snackBar has been displayed.
     */
     void onDisplayed();

    /**
     * Will be called when your snackBar has been removed.
     */
     void onRemoved();
}
