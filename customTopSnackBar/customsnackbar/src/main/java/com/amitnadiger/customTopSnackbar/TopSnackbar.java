package com.amitnadiger.customTopSnackbar;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public final class TopSnackbar {
    private static final String TAG = "TopSnackbar";
    private static final String NULL_PARAMETERS_ARE_NOT_ACCEPTED = "Null parameters are not acceptable";
    private static final int IMAGE_ID = 0x100;
    private static final int TEXT_ID = 0x101;
    private final CharSequence text;
    private final Style style;
    private final View customView;
    private SnackBarConfiguration snackBarConfiguration = null;
    private OnClickListener onClickListener;

    private Activity activity;
    private ViewGroup viewGroup;
    private FrameLayout topSnackbarView;
    private Animation inAnimation;
    private Animation outAnimation;
    private View mFragmentView;
    private LifecycleCallback lifecycleCallback = null;

    /**
     * Creates the TopSnackBar
     *
     * @param activity The  Activity that the TopSnackBar should be attached
     *                 to.
     * @param text     The text you want to display.
     * @param style    The style that this TopSnackBar should be created with.
     */
    private TopSnackbar(final  Activity activity, final CharSequence text, final Style style) {
        if ((activity == null) || (text == null) || (style == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.viewGroup = null;
        this.text = text;
        this.style = style;
        this.customView = null;
        this.mFragmentView = null;
    }

    /**
     * Creates the TopSnackBar
     *
     * @param activity The  Activity that the TopSnackBar should be attached
     *                 to.
     * @param text     The text you want to display.
     * @param style    The style that this TopSnackBar should be created with.
     *                 * @param viewGroup
     *                 *     The ViewGroup that this TopSnackBar should be added to.
     */
    private TopSnackbar(final Activity activity,  CharSequence text,  Style style,  ViewGroup viewGroup) {
        if ((activity == null) || (text == null) || (style == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.text = text;
        this.style = style;
        this.viewGroup = viewGroup;
        this.customView = null;
        this.mFragmentView = null;
    }

    /**
     * Creates the TopSnackbar.
     *
     * @param activity   The Activity that the TopSnackbar should be attached
     *                   to.
     * @param customView The custom View to display
     */
    private TopSnackbar( Activity activity,  View customView) {
        if ((activity == null) || (customView == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.viewGroup = null;
        this.customView = customView;
        this.style = new Style.Builder().build();
        this.text = null;
        this.mFragmentView = null;
    }

    private TopSnackbar( Activity activity,  View fragmentView,  View customView) {
        if ((fragmentView == null) || (customView == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.viewGroup = null;
        this.customView = customView;
        this.style = new Style.Builder().build();
        this.text = null;
        this.mFragmentView = fragmentView;
    }

    /**
     * Creates the TopSnackbar.
     *
     * @param activity      The Activity that represents the context in which the snackbar should exist.
     * @param customView    The custom  View to display
     * @param viewGroup     The ViewGroup that this TopSnackbar should be added to.
     * @param snackBarConfiguration The SnackBarConfiguration for this TopSnackbar.
     */
    private TopSnackbar( Activity activity,  View customView,  ViewGroup viewGroup,
                         SnackBarConfiguration snackBarConfiguration) {
        if ((activity == null) || (customView == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.customView = customView;
        this.viewGroup = viewGroup;
        this.style = new Style.Builder().build();
        this.text = null;
        this.snackBarConfiguration = snackBarConfiguration;
        this.mFragmentView = null;
    }

    private TopSnackbar( Activity activity,  View parentFragmentView,  View customView,  ViewGroup viewGroup,
                        final SnackBarConfiguration snackBarConfiguration) {
        if ((activity == null) || (customView == null)) {
            throw new IllegalArgumentException(NULL_PARAMETERS_ARE_NOT_ACCEPTED);
        }
        this.activity = activity;
        this.customView = customView;
        this.viewGroup = viewGroup;
        this.style = new Style.Builder().build();
        this.text = null;
        this.snackBarConfiguration = snackBarConfiguration;
        this.mFragmentView = parentFragmentView;
    }

    /**
     * Creates the TopSnackbar.
     *
     * @param activity   The Activity that represents the context in which the Snackbar should exist.
     * @param customView The custom View} to display
     * @param viewGroup  The  ViewGroup that this TopSnackbar should be added to.
     */
    private TopSnackbar( Activity activity,  View customView,  ViewGroup viewGroup) {
        this(activity, customView, viewGroup, SnackBarConfiguration.DEFAULT);
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given
     * activity.
     *
     * @param activity The Activity that the TopSnackbar should be attached
     *                 to.
     * @param text     The text you want to display.
     * @param style    The style that this TopSnackbar should be created with.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar makeText(Activity activity, CharSequence text, Style style) {
        return new TopSnackbar(activity, text, style);
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given
     * activity.
     *
     * @param activity  The Activity that represents the context in which the Snackbar should exist.
     * @param text      The text you want to display.
     * @param style     The style that this TopSnackbar should be created with.
     * @param viewGroup The ViewGroup that this TopSnackbar should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar makeText(Activity activity, CharSequence text, Style style, ViewGroup viewGroup) {
        return new TopSnackbar(activity, text, style, viewGroup);
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given
     * activity.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param text           The text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroupResId The resource id of the ViewGroup that this TopSnackbar should be added to.
     * @return The created TopSnackbar.
     */


    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity       The Activity that the TopSnackbar should be attached
     *                       to.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @return The created TopSnackbar.
     */


    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity       The  Activity that represents the context in which the Snackbar should exist.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroup      The ViewGroup that this TopSnackbar should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar makeText(Activity activity, int textResourceId, Style style, ViewGroup viewGroup) {
        return makeText(activity, activity.getString(textResourceId), style, viewGroup);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity       The {@link Activity} that represents the context in which the Snackbar should exist.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroupResId The resource id of the {@link ViewGroup} that this TopSnackbar should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar makeText(Activity activity, int textResourceId, Style style, int viewGroupResId) {
        return makeText(activity, activity.getString(textResourceId), style,
                (ViewGroup) activity.findViewById(viewGroupResId));
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity   The Activity that the TopSnackbar should be attached
     *                   to.
     * @param customView The custom View to display
     * @return The created TopSnackbar.
     */
    public static TopSnackbar make(Activity activity, View customView) {
        return new TopSnackbar(activity, customView);
    }

    public static TopSnackbar make(Activity activity, View fragmentView, View customView) {
        return new TopSnackbar(activity, fragmentView, customView);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity   The Activity that represents the context in which the Snackbar should exist.
     * @param customView The custom View to display
     * @param viewGroup  The ViewGroup that this  should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar make(Activity activity, View customView, ViewGroup viewGroup) {
        return new TopSnackbar(activity, customView, viewGroup);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param fragmentView The fragmentView that represents the context in which the Snackbar should exist.This is specially for fragment.
     * @param customView   The custom View to display
     * @param viewGroup    The ViewGroup that this  should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar make(Activity activity, View fragmentView, View customView, ViewGroup viewGroup, final SnackBarConfiguration snackBarConfiguration) {
        return new TopSnackbar(activity, fragmentView, customView, viewGroup, snackBarConfiguration);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param customView     The custom View to display
     * @param viewGroupResId The resource id of the { ViewGroup that this TopSnackbar should be added to.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar make(Activity activity, View customView, int viewGroupResId) {
        return new TopSnackbar(activity, customView, activity.findViewById(viewGroupResId));
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param customView     The custom View to display
     * @param viewGroupResId The resource id of the ViewGroup that this TopSnackbar should be added to.
     * @param snackBarConfiguration  The snackBarConfiguration for this snackbar.
     * @return The created TopSnackbar.
     */
    public static TopSnackbar make(Activity activity, View customView, int viewGroupResId,
                                   final SnackBarConfiguration snackBarConfiguration) {
        return new TopSnackbar(activity, customView, (ViewGroup) activity.findViewById(viewGroupResId), snackBarConfiguration);
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity The {@link Activity} that the TopSnackbar should
     *                 be attached to.
     * @param text     The text you want to display.
     * @param style    The style that this TopSnackbar should be created with.
     */
    public static void showText(Activity activity, CharSequence text, Style style) {
        makeText(activity, text, style).show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity  The Activity that represents the context in which the Snackbar should exist.
     * @param text      The text you want to display.
     * @param style     The style that this TopSnackbar should be created with.
     * @param viewGroup The ViewGroup that this TopSnackbar should be added to.
     */
    public static void showText(Activity activity, CharSequence text, Style style, ViewGroup viewGroup) {
        makeText(activity, text, style, viewGroup).show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param text           The text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroupResId The resource id of the ViewGroup that this TopSnackbar should be added to.
     */
    public static void showText(Activity activity, CharSequence text, Style style, int viewGroupResId) {
        makeText(activity, text, style, (ViewGroup) activity.findViewById(viewGroupResId)).show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param text           The text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroupResId The resource id of the  ViewGroup that this TopSnackbar should be added to.
     * @param snackBarConfiguration  The snackBarConfiguration for this Snackbar.
     */
    public static void showText(Activity activity, CharSequence text, Style style, int viewGroupResId,
                                final SnackBarConfiguration snackBarConfiguration) {
        makeText(activity, text, style, (ViewGroup) activity.findViewById(viewGroupResId)).setSnackBarConfiguration(snackBarConfiguration)
                .show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity   The  android.app.Activity that the TopSnackbar should
     *                   be attached to.
     * @param customView The custom  View to display
     */
    public static void show(Activity activity, View customView) {
        make(activity, customView).show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity   The Activity that represents the context in which the Snackbar should exist.
     * @param customView The custom  View to display
     * @param viewGroup  The ViewGroup that this TopSnackbar should be added to.
     */
    public static void show(Activity activity, View customView, ViewGroup viewGroup) {
        make(activity, customView, viewGroup).show();
    }

    /**
     * Creates a TopSnackbar with provided text and style for a given activity
     * and displays it directly.
     *
     * @param activity       The  Activity that represents the context in which the Snackbar should exist.
     * @param customView     The custom View to display
     * @param viewGroupResId The resource id of the ViewGroup that this TopSnackbar should be added to.
     */
    public static void show(Activity activity, View customView, int viewGroupResId) {
        make(activity, customView, viewGroupResId).show();
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity and displays it directly.
     *
     * @param activity       The Activity that the TopSnackbar should be attached
     *                       to.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     */
    public static void showText(Activity activity, int textResourceId, Style style) {
        showText(activity, activity.getString(textResourceId), style);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity and displays it directly.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroup      The ViewGroup that this TopSnackbar should be added to.
     */
    public static void showText(Activity activity, int textResourceId, Style style, ViewGroup viewGroup) {
        showText(activity, activity.getString(textResourceId), style, viewGroup);
    }

    /**
     * Creates a TopSnackbar with provided text-resource and style for a given
     * activity and displays it directly.
     *
     * @param activity       The Activity that represents the context in which the Snackbar should exist.
     * @param textResourceId The resource id of the text you want to display.
     * @param style          The style that this TopSnackbar should be created with.
     * @param viewGroupResId The resource id of the ViewGroup that this TopSnackbar should be added to.
     */
    public static void showText(Activity activity, int textResourceId, Style style, int viewGroupResId) {
        showText(activity, activity.getString(textResourceId), style, viewGroupResId);
    }

    /**
     * Allows hiding of a previously displayed TopSnackbar.
     *
     * @param snackbar The TopSnackbar you want to hide.
     */
    public static void hide(TopSnackbar snackbar) {
        snackbar.hide();
    }

    /**
     * Cancels all queued TopSnackbars. If there is a TopSnackbar
     * displayed currently, it will be the last one displayed.
     */
    public static void cancelAllTopSnackbars() {
        SnackbarManager.getInstance().clearTopSnackbarQueue();
    }

    /**
     * Clears (and removes from Activity's content view, if necessary) all
     * Snackbars for the provided activity
     *
     * @param activity - The Activity to clear the Snackbars for.
     */
    public static void clearTopSnackbarForActivity(Activity activity) {
        SnackbarManager.getInstance().clearTopSnackbarForActivity(activity);
    }

    /**
     * Cancels a TopSnackbar immediately.
     */
    public void cancel() {
        SnackbarManager manager = SnackbarManager.getInstance();
        manager.removeTopSnackbarImmediately(this);
    }

    /**
     * Displays the TopSnackbar. If there's another TopSnackbar visible at
     * the time, this TopSnackbar will be displayed afterwards.
     */
    public void show() {
        SnackbarManager.getInstance().add(this);
    }


    public Animation getInAnimation() {
        if ((null == this.inAnimation) && (null != this.activity)) {

            if (getSnackBarConfiguration().inAnimationResId > 0) {
                this.inAnimation = AnimationUtils.loadAnimation(getActivity(), getSnackBarConfiguration().inAnimationResId);
            } else {
                measureTopSnackbarView();
                this.inAnimation = DefaultAnimationsBuilder.buildDefaultSlideInDownAnimation(getView());
            }
        }
        return inAnimation;
    }

    public Animation getOutAnimation() {
        if ((null == this.outAnimation) && (null != this.activity)) {

            if (getSnackBarConfiguration().outAnimationResId > 0) {
                this.outAnimation = AnimationUtils.loadAnimation(getActivity(), getSnackBarConfiguration().outAnimationResId);
            } else {
                this.outAnimation = DefaultAnimationsBuilder.buildDefaultSlideOutUpAnimation(getView());
            }
        }
        return outAnimation;
    }

    /**
     * Removes this {TopSnackbar.
     *
     * @since 1.9
     */
    public void hide() {
        SnackbarManager.getInstance().removeTopSnackbar(this);
    }

    /**
     * Allows setting of an  OnClickListener directly to a TopSnackbar without having to use a custom view.
     *
     * @param onClickListener The OnClickListener to set.
     * @return this TopSnackbar.
     */
    public TopSnackbar setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        return this;
    }

    @Override
    public String toString() {
        return "Snackbar{" +
                "text=" + text +
                ", style=" + style +
                ", snackBarConfiguration=" + snackBarConfiguration +
                ", customView=" + customView +
                ", onClickListener=" + onClickListener +
                ", activity=" + activity +
                ", viewGroup=" + viewGroup +
                ", SnackbarView=" + topSnackbarView +
                ", inAnimation=" + inAnimation +
                ", outAnimation=" + outAnimation +
                ", lifecycleCallback=" + lifecycleCallback +
                '}';
    }

    /**
     * @return <code>true</code> if the TopSnackbar is being displayed, else
     * <code>false</code>.
     */
    boolean isShowing() {
        return ((null != activity) || (null != mFragmentView)) && (isTopSnackbarViewNotNull() || isCustomViewNotNull());
    }

    private boolean isTopSnackbarViewNotNull() {
        return (null != topSnackbarView) && (null != topSnackbarView.getParent());
    }

    //////////////////////////////////////////////////////////////////////////////////////
    // internal API of TopSnackBar.
    //////////////////////////////////////////////////////////////////////////////////////

    private boolean isCustomViewNotNull() {
        return (null != customView) && (null != customView.getParent());
    }

    /**
     * Removes the activity reference this TopSanckbar is holding
     */
    void detachActivity() {
        activity = null;
    }

    /**
     * Removes the fragment reference this TopSanckbar is holding
     */
    void detachFragment() {
        mFragmentView = null;
    }

    /**
     * Removes the viewGroup reference this TopSanckbar is holding
     */
    void detachViewGroup() {
        viewGroup = null;
    }

    /**
     * Removes the lifecycleCallback reference this TopSanckbar is holding
     */
    void detachLifecycleCallback() {
        lifecycleCallback = null;
    }

    /**
     * @return the lifecycleCallback
     */
    LifecycleCallback getLifecycleCallback() {
        return lifecycleCallback;
    }

    /**
     * @param lifecycleCallback Callback object for notable events in the life of a Snackbar.
     */
    public void setLifecycleCallback(LifecycleCallback lifecycleCallback) {
        this.lifecycleCallback = lifecycleCallback;
    }

    /**
     * @return the style
     */
    Style getStyle() {
        return style;
    }

    /**
     * @return this TopSanckbar snackBarConfiguration
     */
    SnackBarConfiguration getSnackBarConfiguration() {
        if (null == snackBarConfiguration) {
            snackBarConfiguration = getStyle().snackBarConfiguration;
        }
        return snackBarConfiguration;
    }

    /**
     * Set the  SnackBarConfiguration on this TopSnackbar, prior to showing it.
     *
     * @param snackBarConfiguration a  SnackBarConfiguration built using the SnackBarConfiguration.Builder.
     * @return this TopSnackbar.
     */
    public TopSnackbar setSnackBarConfiguration(final SnackBarConfiguration snackBarConfiguration) {
        this.snackBarConfiguration = snackBarConfiguration;
        return this;
    }

    /**
     * @return the activity
     */
    Activity getActivity() {
        return activity;
    }

    /**
     * @return the viewGroup
     */
    View getFragmentView() {
        return mFragmentView;
    }

    /**
     * @return the viewGroup
     */
    ViewGroup getViewGroup() {
        return viewGroup;
    }


    /**
     * @return the text
     */
    CharSequence getText() {
        return text;
    }

    /**
     * @return the view
     */
    View getView() {
        // return the custom view if one exists
        if (null != this.customView) {
            return this.customView;
        }

        // if already setup return the view
        if (null == this.topSnackbarView) {
            initializeTopSnackbarView();
        }

        return topSnackbarView;
    }

    private void measureTopSnackbarView() {
        View view = getView();
        int widthSpec;
        if (null != viewGroup) {
            widthSpec = View.MeasureSpec.makeMeasureSpec(viewGroup.getMeasuredWidth(), View.MeasureSpec.AT_MOST);
        } else {
            widthSpec = View.MeasureSpec.makeMeasureSpec(activity.getWindow().getDecorView().getMeasuredWidth(),
                    View.MeasureSpec.AT_MOST);
        }
        view.measure(widthSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
    }

    private void initializeTopSnackbarView() {
        Resources resources = this.activity.getResources();

        this.topSnackbarView = initializeSnackbarViewGroup(resources);

        // create content view
        RelativeLayout contentView = initializeContentView(resources);
        this.topSnackbarView.addView(contentView);
    }

    private FrameLayout initializeSnackbarViewGroup(Resources resources) {
        FrameLayout snackbarView = new FrameLayout(this.activity);

        if (null != onClickListener) {
            snackbarView.setOnClickListener(onClickListener);
        }

        final int height;
        if (this.style.heightDimensionResId > 0) {
            height = resources.getDimensionPixelSize(this.style.heightDimensionResId);
        } else {
            height = this.style.heightInPixels;
        }

        final int width;
        if (this.style.widthDimensionResId > 0) {
            width = resources.getDimensionPixelSize(this.style.widthDimensionResId);
        } else {
            width = this.style.widthInPixels;
        }

        snackbarView.setLayoutParams(
                new FrameLayout.LayoutParams(width != 0 ? width : FrameLayout.LayoutParams.MATCH_PARENT, height));

        // set background
        if (this.style.backgroundColorValue != Style.NOT_SET) {
            snackbarView.setBackgroundColor(this.style.backgroundColorValue);
        } else {
            snackbarView.setBackgroundColor(resources.getColor(this.style.backgroundColorResourceId));
        }

        // set the background drawable if set. This will override the background
        // color.
        if (this.style.backgroundDrawableResourceId != 0) {
            Bitmap background = BitmapFactory.decodeResource(resources, this.style.backgroundDrawableResourceId);
            BitmapDrawable drawable = new BitmapDrawable(resources, background);
            if (this.style.isTileEnabled) {
                drawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
            }
            snackbarView.setBackgroundDrawable(drawable);
        }
        return snackbarView;
    }

    private RelativeLayout initializeContentView(final Resources resources) {
        RelativeLayout contentView = new RelativeLayout(this.activity);
        // Amit contentView.setPadding(0,100,0,0);
        contentView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
        // set padding
        int padding = this.style.paddingInPixels;

        // if a padding dimension has been set, this will overwrite any padding
        // in pixels
        if (this.style.paddingDimensionResId > 0) {
            padding = resources.getDimensionPixelSize(this.style.paddingDimensionResId);
        }
        contentView.setPadding(padding, padding, padding, padding);

        // only setup image if one is requested
        ImageView image = null;
        if ((null != this.style.imageDrawable) || (0 != this.style.imageResId)) {
            image = initializeImageView();
            contentView.addView(image, image.getLayoutParams());
        }

        TextView text = initializeTextView(resources);

        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        if (null != image) {
            textParams.addRule(RelativeLayout.RIGHT_OF, image.getId());
        }

        if ((this.style.gravity & Gravity.CENTER) != 0) {
            textParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        } else if ((this.style.gravity & Gravity.CENTER_VERTICAL) != 0) {
            textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        } else if ((this.style.gravity & Gravity.CENTER_HORIZONTAL) != 0) {
            textParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        }

        contentView.addView(text, textParams);
        return contentView;
    }

    private TextView initializeTextView(final Resources resources) {
        TextView text = new TextView(this.activity);
        text.setId(TEXT_ID);
        if (this.style.fontName != null) {
            setTextWithCustomFont(text, this.style.fontName);
        } else if (this.style.fontNameResId != 0) {
            setTextWithCustomFont(text, resources.getString(this.style.fontNameResId));
        } else {
            text.setText(this.text);
        }
        text.setTypeface(Typeface.DEFAULT_BOLD);
        text.setGravity(this.style.gravity);

        // set the text color if set
        if (this.style.textColorValue != Style.NOT_SET) {
            text.setTextColor(this.style.textColorValue);
        } else if (this.style.textColorResourceId != 0) {
            text.setTextColor(resources.getColor(this.style.textColorResourceId));
        }

        // Set the text size. If the user has set a text size and text
        // appearance, the text size in the text appearance
        // will override this.
        if (this.style.textSize != 0) {
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, this.style.textSize);
        }

        // Setup the shadow if requested
        if (this.style.textShadowColorResId != 0) {
            initializeTextViewShadow(resources, text);
        }

        // Set the text appearance
        if (this.style.textAppearanceResId != 0) {
            text.setTextAppearance(this.activity, this.style.textAppearanceResId);
        }
        return text;
    }

    private void setTextWithCustomFont(TextView text, String fontName) {
        if (this.text != null) {
            SpannableString s = new SpannableString(this.text);
            s.setSpan(new CustomFont(text.getContext(), fontName), 0, s.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setText(s);
        }
    }

    private void initializeTextViewShadow(final Resources resources, final TextView text) {
        int textShadowColor = resources.getColor(this.style.textShadowColorResId);
        float textShadowRadius = this.style.textShadowRadius;
        float textShadowDx = this.style.textShadowDx;
        float textShadowDy = this.style.textShadowDy;
        text.setShadowLayer(textShadowRadius, textShadowDx, textShadowDy, textShadowColor);
    }

    private ImageView initializeImageView() {
        ImageView image;
        image = new ImageView(this.activity);
        image.setId(IMAGE_ID);
        image.setAdjustViewBounds(true);
        image.setScaleType(this.style.imageScaleType);

        // set the image drawable if not null
        if (null != this.style.imageDrawable) {
            image.setImageDrawable(this.style.imageDrawable);
        }

        // set the image resource if not 0. This will overwrite the drawable
        // if both are set
        if (this.style.imageResId != 0) {
            image.setImageResource(this.style.imageResId);
        }

        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        imageParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        imageParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

        image.setLayoutParams(imageParams);

        return image;
    }

}

