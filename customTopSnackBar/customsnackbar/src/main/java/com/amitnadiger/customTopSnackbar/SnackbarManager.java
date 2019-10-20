package com.amitnadiger.customTopSnackbar;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


public class SnackbarManager extends Handler {
    private static final String TAG = "SnackbarManager";
    private static SnackbarManager INSTANCE;
    private final Queue<TopSnackbar> mSnackbarQueue;
    private SnackbarManager() {
        mSnackbarQueue = new LinkedBlockingQueue<TopSnackbar>();
    }

    /**
     * @return The currently used instance of the {@link SnackbarManager}.
     */
    static synchronized SnackbarManager getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new SnackbarManager();
        }
        return INSTANCE;
    }

    /**
     * Generates and dispatches an SDK-specific spoken announcement.
     * <p>
     * For backwards compatibility, we're constructing an event from scratch
     * using the appropriate event type. If your application only targets SDK
     * 16+, you can just call View.announceForAccessibility(CharSequence).
     * </p>
     * <p/>
     * note: AccessibilityManager is only available from API lvl 4.
     * <p/>
     * Adapted from https://http://eyes-free.googlecode.com/files/accessibility_codelab_demos_v2_src.zip
     * via https://github.com/coreform/android-formidable-validation
     *
     * @param context Used to get {@link AccessibilityManager}
     * @param text    The text to announce.
     */
    public static void announceForAccessibilityCompat(Context context, CharSequence text) {
        if (Build.VERSION.SDK_INT >= 4) {
            AccessibilityManager accessibilityManager = null;
            if (null != context) {
                accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
            }
            if (null == accessibilityManager || !accessibilityManager.isEnabled()) {
                return;
            }

            // Prior to SDK 16, announcements could only be made through FOCUSED
            // events. Jelly Bean (SDK 16) added support for speaking text verbatim
            // using the ANNOUNCEMENT event type.
            final int eventType;
            if (Build.VERSION.SDK_INT < 16) {
                eventType = AccessibilityEvent.TYPE_VIEW_FOCUSED;
            } else {
                eventType = AccessibilityEvent.TYPE_ANNOUNCEMENT;
            }

            // Construct an accessibility event with the minimum recommended
            // attributes. An event without a class name or package may be dropped.
            final AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
            event.getText().add(text);
            event.setClassName(SnackbarManager.class.getName());
            event.setPackageName(context.getPackageName());

            // Sends the event directly through the accessibility manager. If your
            // application only targets SDK 14+, you should just call
            // getParent().requestSendAccessibilityEvent(this, event);
            accessibilityManager.sendAccessibilityEvent(event);
        }
    }

    /**
     * Inserts a {@link TopSnackbar} to be displayed.
     *
     * @param snackbar The {@link TopSnackbar} to be displayed.
     */
    void add(TopSnackbar snackbar) {
        mSnackbarQueue.add(snackbar);
        displayTopSnackbar();
    }

    /**
     * Displays the next {@link TopSnackbar} within the queue.
     */
    private void displayTopSnackbar() {
        if (mSnackbarQueue.isEmpty()) {
            return;
        }
        // First peek whether the snackbar has an activity.
        final TopSnackbar currentSnackbar = mSnackbarQueue.peek();
        // If both activity is null we poll the snackbar off the queue.
        if (null == currentSnackbar.getActivity()) {
            mSnackbarQueue.poll();
        }

        if (!currentSnackbar.isShowing()) {
            // Display the TopSnackbar
            sendMessage(currentSnackbar, Messages.ADD_TOPSNACKBAR_TO_VIEW);
            if (null != currentSnackbar.getLifecycleCallback()) {
                currentSnackbar.getLifecycleCallback().onDisplayed();
            }
        } else {
            sendMessageDelayed(currentSnackbar, Messages.DISPLAY_TOPSNACKBAR, calculateSnackBarDuration(currentSnackbar));
        }
    }

    private long calculateSnackBarDuration(TopSnackbar snackbar) {
        long snackbarDuration = snackbar.getSnackBarConfiguration().durationInMilliseconds;
        snackbarDuration += snackbar.getInAnimation().getDuration();
        snackbarDuration += snackbar.getOutAnimation().getDuration();
        return snackbarDuration;
    }

    /**
     * Sends a {@link TopSnackbar} within a {@link Message}.
     *
     * @param snackbar  The {@link TopSnackbar} that should be sent.
     * @param messageId The {@link Message} id.
     */
    private void sendMessage(TopSnackbar snackbar, final int messageId) {
        final Message message = obtainMessage(messageId);
        message.obj = snackbar;
        sendMessage(message);
    }

    /**
     * Sends a {@link TopSnackbar} within a delayed {@link Message}.
     *
     * @param snackbar  The {@link TopSnackbar} that should be sent.
     * @param messageId The {@link Message} id.
     * @param delay     The delay in milliseconds.
     */
    private void sendMessageDelayed(TopSnackbar snackbar, final int messageId, final long delay) {
        Message message = obtainMessage(messageId);
        message.obj = snackbar;
        sendMessageDelayed(message, delay);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.Handler#handleMessage(android.os.Message)
     */
    @Override
    public void handleMessage(Message message) {
        final TopSnackbar snackbar = (TopSnackbar) message.obj;
        if (null == snackbar) {
            return;
        }
        switch (message.what) {
            case Messages.DISPLAY_TOPSNACKBAR: {
                displayTopSnackbar();
                break;
            }

            case Messages.ADD_TOPSNACKBAR_TO_VIEW: {
                addTopSnackbarToView(snackbar);
                break;
            }

            case Messages.REMOVE_TOPSNACKBAR: {
                removeTopSnackbar(snackbar);
                if (null != snackbar.getLifecycleCallback()) {
                    snackbar.getLifecycleCallback().onRemoved();
                }
                break;
            }

            default: {
                super.handleMessage(message);
                break;
            }
        }
    }

    /**
     * Adds a {@link TopSnackbar} to the {@link TopSnackbar} of it's {@link Activity}.
     *
     * @param topSnackbar The {@link TopSnackbar} that should be added.
     */
    private void addTopSnackbarToView(final TopSnackbar topSnackbar) {
        // don't add if it is already showing
        if (topSnackbar.isShowing()) {
            return;
        }

        final View topSnackbarView = topSnackbar.getView();

        if (null == topSnackbarView.getParent()) {
            ViewGroup.LayoutParams params = topSnackbarView.getLayoutParams();
            if (null == params) {
                params =
                        new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
            // display topsnackbar in ViewGroup if it has been supplied
            if (null != topSnackbar.getViewGroup()) {
                final ViewGroup snackbarViewGroup = topSnackbar.getViewGroup();
                if (shouldAddViewWithoutPosition(snackbarViewGroup)) {
                    snackbarViewGroup.addView(topSnackbarView, params);
                } else {
                    snackbarViewGroup.addView(topSnackbarView, 0, params);
                }
            } else {

                Activity activity = topSnackbar.getActivity();
                if (null == activity || activity.isFinishing()) {
                    return;
                }
                handleTranslucentActionBar((ViewGroup.MarginLayoutParams) params, activity);
                handleActionBarOverlay((ViewGroup.MarginLayoutParams) params, activity);
                //todo : Layout is hard coaded to ConstraintLayout : should be detect the type of layout based on viewGroup. .
                if(((ViewGroup) topSnackbar.getFragmentView())!= null) {
                    ((ViewGroup) topSnackbar.getFragmentView()).addView(topSnackbarView, params);
                } else {
                    ((ViewGroup)activity.findViewById(android.R.id.content)).addView(topSnackbarView, params);
                }

            }
        }

        topSnackbarView.requestLayout(); // This is needed so the animation can use the measured with/height
        ViewTreeObserver observer = topSnackbarView.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                @TargetApi(16)
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        topSnackbarView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        topSnackbarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    if (topSnackbar.getInAnimation() != null) {
                        topSnackbarView.startAnimation(topSnackbar.getInAnimation());
                        announceForAccessibilityCompat(topSnackbar.getActivity(), topSnackbar.getText());
                        if (SnackBarConfiguration.DURATION_INFINITE != topSnackbar.getSnackBarConfiguration().durationInMilliseconds) {
                            sendMessageDelayed(topSnackbar, Messages.REMOVE_TOPSNACKBAR,
                                    topSnackbar.getSnackBarConfiguration().durationInMilliseconds + topSnackbar.getInAnimation().getDuration());
                        }
                    }
                }
            });
        }
    }

    private boolean shouldAddViewWithoutPosition(ViewGroup snackBarViewGroup) {
        return snackBarViewGroup instanceof FrameLayout || snackBarViewGroup instanceof AdapterView ||
                snackBarViewGroup instanceof RelativeLayout;
    }

    @TargetApi(19)
    private void handleTranslucentActionBar(ViewGroup.MarginLayoutParams params, Activity activity) {
        // Translucent status is only available as of Android 4.4 Kit Kat.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final int flags = activity.getWindow().getAttributes().flags;
            final int translucentStatusFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((flags & translucentStatusFlag) == translucentStatusFlag) {
                setActionBarMargin(params, activity);
            }
        }
    }

    @TargetApi(11)
    private void handleActionBarOverlay(ViewGroup.MarginLayoutParams params, Activity activity) {
        // ActionBar overlay is only available as of Android 3.0 Honeycomb.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final boolean flags = activity.getWindow().hasFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
            //  RankFragment fragment = activity.getFragmentManager().getFragment()
            if (flags) {
                setActionBarMargin(params, activity);
            }
        }
    }

    private void setActionBarMargin(ViewGroup.MarginLayoutParams params, Activity activity) {
        final int actionBarContainerId = Resources.getSystem().getIdentifier("action_bar_container", "id", "android");
        final View actionBarContainer = activity.findViewById(actionBarContainerId);
        // The action bar is present: the app is using a Holo theme.

        if (null != actionBarContainer) {
            params.topMargin = actionBarContainer.getBottom();
        }
    }

    /**
     * Removes the {@link TopSnackbar}'s view after it's display
     * durationInMilliseconds.
     *
     * @param TopSnackbar The {@link TopSnackbar} added to a {@link ViewGroup} and should be
     *                    removed.
     */
    protected void removeTopSnackbar(TopSnackbar snackbar) {
        // If the TopSnackbar hasn't been displayed yet a `TopSnackbar.hide()` will fail to hide
        // it since the DISPLAY message might still be in the queue. Remove all messages
        // for this snavbar.
        removeAllMessagesForTopSnackbar(snackbar);

        View snackbarView = snackbar.getView();

        ViewGroup snackbarParentView = (ViewGroup) snackbarView.getParent();

        if (null != snackbarParentView) {
            snackbarView.startAnimation(snackbar.getOutAnimation());

            // Remove the Snackbar from the queue.
            TopSnackbar removed = mSnackbarQueue.poll();

            // Remove the Snackbar from the view's parent.
            snackbarParentView.removeView(snackbarView);
            if (null != removed) {
                removed.detachActivity();
                removed.detachFragment();
                removed.detachViewGroup();
                if (null != removed.getLifecycleCallback()) {
                    removed.getLifecycleCallback().onRemoved();
                }
                removed.detachLifecycleCallback();
            }

            // Send a message to display the next Snackbar but delay it by the out
            // animation duration to make sure it finishes
            sendMessageDelayed(snackbar, Messages.DISPLAY_TOPSNACKBAR, snackbar.getOutAnimation().getDuration());
        }
    }

    /**
     * Removes a {@link TopSnackbar} immediately, even when it's currently being
     * displayed.
     *
     * @param topSnackbar The {@link TopSnackbar} that should be removed.
     */
    void removeTopSnackbarImmediately(TopSnackbar topSnackbar) {
        // if Snackbar has already been displayed then it may not be in the queue (because it was popped).
        // This ensures the displayed Snackbar is removed from its parent immediately, whether another instance
        // of it exists in the queue or not.
        // Note: topSnackbar.isShowing() is false here even if it really is showing, as SnackbarView object in
        // Snackbar seems to be out of sync with reality!
        if (null != topSnackbar.getActivity() && null != topSnackbar.getView() && null != topSnackbar.getView().getParent()) {
            ((ViewGroup) topSnackbar.getView().getParent()).removeView(topSnackbar.getView());

            // remove any messages pending for the topSnackbar
            removeAllMessagesForTopSnackbar(topSnackbar);
        }
        // remove any matching Snackbars from queue
        final Iterator<TopSnackbar> SnackbarIterator = mSnackbarQueue.iterator();
        while (SnackbarIterator.hasNext()) {
            final TopSnackbar c = SnackbarIterator.next();
            if (c.equals(topSnackbar) && (null != c.getActivity())) {
                // remove the topSnackbar from the content view
                removeTopSnackbarFromViewParent(topSnackbar);

                // remove any messages pending for the topSnackbar
                removeAllMessagesForTopSnackbar(c);

                // remove the topSnackbar from the queue
                SnackbarIterator.remove();

                // we have found our topSnackbar so just break
                break;
            }
        }
    }

    /**
     * Removes all {@link TopSnackbar}s from the queue.
     */
    void clearTopSnackbarQueue() {
        removeAllMessages();

        // remove any views that may already have been added to the activity's
        // content view
        for (TopSnackbar Snackbar : mSnackbarQueue) {
            removeTopSnackbarFromViewParent(Snackbar);
        }
        mSnackbarQueue.clear();
    }

    /**
     * Removes all {@link TopSnackbar}s for the provided activity. This will remove
     * Snackbar from {@link Activity}s content view immediately.
     */
    void clearTopSnackbarForActivity(Activity activity) {
        Iterator<TopSnackbar> SnackbarIterator = mSnackbarQueue.iterator();
        while (SnackbarIterator.hasNext()) {
            TopSnackbar Snackbar = SnackbarIterator.next();
            if ((null != Snackbar.getActivity()) && Snackbar.getActivity().equals(activity)) {
                // remove the Snackbar from the content view
                removeTopSnackbarFromViewParent(Snackbar);

                removeAllMessagesForTopSnackbar(Snackbar);

                // remove the Snackbar from the queue

                SnackbarIterator.remove();
            }
        }
    }

    private void removeTopSnackbarFromViewParent(TopSnackbar snackbar) {
        if (snackbar.isShowing()) {
            ViewGroup parent = (ViewGroup) snackbar.getView().getParent();
            if (null != parent) {
                parent.removeView(snackbar.getView());
            }
        }
    }

    private void removeAllMessages() {
        removeMessages(Messages.ADD_TOPSNACKBAR_TO_VIEW);
        removeMessages(Messages.DISPLAY_TOPSNACKBAR);
        removeMessages(Messages.REMOVE_TOPSNACKBAR);
    }

    private void removeAllMessagesForTopSnackbar(TopSnackbar snackbar) {
        removeMessages(Messages.ADD_TOPSNACKBAR_TO_VIEW, snackbar);
        removeMessages(Messages.DISPLAY_TOPSNACKBAR, snackbar);
        removeMessages(Messages.REMOVE_TOPSNACKBAR, snackbar);

    }

    @Override
    public String toString() {
        return "Manager{" +
                "SnackbarQueue=" + mSnackbarQueue +
                '}';
    }

    private static final class Messages {
        public static final int DISPLAY_TOPSNACKBAR = 0xc2007;
        public static final int ADD_TOPSNACKBAR_TO_VIEW = 0xc20074dd;
        public static final int REMOVE_TOPSNACKBAR = 0xc2007de1;
    }
}
