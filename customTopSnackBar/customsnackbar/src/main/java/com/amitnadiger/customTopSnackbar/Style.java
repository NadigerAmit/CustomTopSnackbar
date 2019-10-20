package com.amitnadiger.customTopSnackbar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;


public class Style {
    public static final int NOT_SET = -1;

    public static final int holoRedLight = 0xffff4444;
    public static final int holoGreenLight = 0xff99cc00;
    public static final int holoBlueLight = Color.parseColor("#333333");
    /**
     * Default style for alerting the user.
     */
    public static final Style ALERT;
    /**
     * Default style for confirming an action.
     */
    public static final Style CONFIRM;
    /**
     * Default style for general information.
     */
    public static final Style INFO;

    static {
        ALERT = new Builder()
                .setBackgroundColorValue(holoRedLight)
                .build();
        CONFIRM = new Builder()
                .setBackgroundColorValue(holoGreenLight)
                .build();
        INFO = new Builder()
                .setBackgroundColorValue(holoBlueLight)
                .build();
    }

    final SnackBarConfiguration snackBarConfiguration;
    final int backgroundColorResourceId;
    final int backgroundDrawableResourceId;
    final int backgroundColorValue;
    /**
     * Whether we should isTileEnabled the backgroundResourceId or not.
     */
    final boolean isTileEnabled;
    /**
     * 0 sets the text colorResourceId to the system theme default.
     */
    final int textColorResourceId;

    final int textColorValue;
    final int heightInPixels;
    final int heightDimensionResId;
    final int widthInPixels;
    final int widthDimensionResId;
    final int gravity;
    final Drawable imageDrawable;
    final int imageResId;
    final ImageView.ScaleType imageScaleType;
    final int textSize;
    /**
     * The text shadow color's resource id
     */
    final int textShadowColorResId;
    /**
     * The text shadow radius
     */
    final float textShadowRadius;
    /**
     * The text shadow vertical offset
     */
    final float textShadowDy;
    /**
     * The text shadow horizontal offset
     */
    final float textShadowDx;

    /**
     * The text appearance resource id for the text.
     */
    final int textAppearanceResId;

    /**
     * The padding for the Snackbar view content in pixels
     */
    final int paddingInPixels;

    /**
     * The resource id for the padding for the view content
     */
    final int paddingDimensionResId;

    /**
     * The file path and font name for the view content
     */
    final String fontName;
    /**
     * The file path and font name resource id for the view content
     */
    final int fontNameResId;

    private Style(final Builder builder) {
        this.snackBarConfiguration = builder.snackBarConfiguration;
        this.backgroundColorResourceId = builder.backgroundColorResourceId;
        this.backgroundDrawableResourceId = builder.backgroundDrawableResourceId;
        this.isTileEnabled = builder.isTileEnabled;
        this.textColorResourceId = builder.textColorResourceId;
        this.textColorValue = builder.textColorValue;
        this.heightInPixels = builder.heightInPixels;
        this.heightDimensionResId = builder.heightDimensionResId;
        this.widthInPixels = builder.widthInPixels;
        this.widthDimensionResId = builder.widthDimensionResId;
        this.gravity = builder.gravity;
        this.imageDrawable = builder.imageDrawable;
        this.textSize = builder.textSize;
        this.textShadowColorResId = builder.textShadowColorResId;
        this.textShadowRadius = builder.textShadowRadius;
        this.textShadowDx = builder.textShadowDx;
        this.textShadowDy = builder.textShadowDy;
        this.textAppearanceResId = builder.textAppearanceResId;
        this.imageResId = builder.imageResId;
        this.imageScaleType = builder.imageScaleType;
        this.paddingInPixels = builder.paddingInPixels;
        this.paddingDimensionResId = builder.paddingDimensionResId;
        this.backgroundColorValue = builder.backgroundColorValue;
        this.fontName = builder.fontName;
        this.fontNameResId = builder.fontNameResId;
    }

    /**
     * Builder for the {@link Style} object.
     */
    public static class Builder {
        private SnackBarConfiguration snackBarConfiguration;
        private int backgroundColorValue;
        private int backgroundColorResourceId;
        private int backgroundDrawableResourceId;
        private boolean isTileEnabled;
        private int textColorResourceId;
        private int textColorValue;
        private int heightInPixels;
        private int heightDimensionResId;
        private int widthInPixels;
        private int widthDimensionResId;
        private int gravity;
        private Drawable imageDrawable;
        private int textSize;
        private int textShadowColorResId;
        private float textShadowRadius;
        private float textShadowDx;
        private float textShadowDy;
        private int textAppearanceResId;
        private int imageResId;
        private ImageView.ScaleType imageScaleType;
        private int paddingInPixels;
        private int paddingDimensionResId;
        private String fontName;
        private int fontNameResId;

        public Builder() {
            snackBarConfiguration = SnackBarConfiguration.DEFAULT;
            paddingInPixels = 10;
            backgroundColorResourceId = android.R.color.holo_blue_light;
            backgroundDrawableResourceId = 0;
            backgroundColorValue = NOT_SET;
            isTileEnabled = false;
            textColorResourceId = android.R.color.white;
            textColorValue = NOT_SET;
            heightInPixels = LayoutParams.WRAP_CONTENT;
            widthInPixels = LayoutParams.MATCH_PARENT;
            gravity = Gravity.CENTER;
            imageDrawable = null;//getResources().getDrawable(R.drawable.ic_cross_mark);
            imageResId = 0;
            imageScaleType = ImageView.ScaleType.FIT_XY;
            fontName = null;
            fontNameResId = 0;
        }

        public Builder(final Style baseStyle) {
            snackBarConfiguration = baseStyle.snackBarConfiguration;
            backgroundColorValue = baseStyle.backgroundColorValue;
            backgroundColorResourceId = baseStyle.backgroundColorResourceId;
            backgroundDrawableResourceId = baseStyle.backgroundDrawableResourceId;
            isTileEnabled = baseStyle.isTileEnabled;
            textColorResourceId = baseStyle.textColorResourceId;
            textColorValue = baseStyle.textColorValue;
            heightInPixels = baseStyle.heightInPixels;
            heightDimensionResId = baseStyle.heightDimensionResId;
            widthInPixels = baseStyle.widthInPixels;
            widthDimensionResId = baseStyle.widthDimensionResId;
            gravity = baseStyle.gravity;
            imageDrawable = baseStyle.imageDrawable;
            textSize = baseStyle.textSize;
            textShadowColorResId = baseStyle.textShadowColorResId;
            textShadowRadius = baseStyle.textShadowRadius;
            textShadowDx = baseStyle.textShadowDx;
            textShadowDy = baseStyle.textShadowDy;
            textAppearanceResId = baseStyle.textAppearanceResId;
            imageResId = baseStyle.imageResId;
            imageScaleType = baseStyle.imageScaleType;
            paddingInPixels = baseStyle.paddingInPixels;
            paddingDimensionResId = baseStyle.paddingDimensionResId;
            fontName = baseStyle.fontName;
            fontNameResId = baseStyle.fontNameResId;
        }

        public Builder setSnackBarConfiguration(SnackBarConfiguration snackBarConfiguration) {
            this.snackBarConfiguration = snackBarConfiguration;
            return this;
        }

        public Builder setBackgroundColor(int backgroundColorResourceId) {
            this.backgroundColorResourceId = backgroundColorResourceId;

            return this;
        }

        public Builder setBackgroundColorValue(int backgroundColorValue) {
            this.backgroundColorValue = backgroundColorValue;
            return this;
        }

        public Builder setBackgroundDrawable(int backgroundDrawableResourceId) {
            this.backgroundDrawableResourceId = backgroundDrawableResourceId;

            return this;
        }

        public Builder setHeight(int height) {
            this.heightInPixels = height;

            return this;
        }

        public Builder setHeightDimensionResId(int heightDimensionResId) {
            this.heightDimensionResId = heightDimensionResId;

            return this;
        }

        public Builder setWidth(int width) {
            this.widthInPixels = width;

            return this;
        }

        public Builder setWidthDimensionResId(int widthDimensionResId) {
            this.widthDimensionResId = widthDimensionResId;

            return this;
        }

        public Builder setTileEnabled(boolean isTileEnabled) {
            this.isTileEnabled = isTileEnabled;

            return this;
        }

        public Builder setTextColor(int textColor) {
            this.textColorResourceId = textColor;

            return this;
        }

        public Builder setTextColorValue(int textColorValue) {
            this.textColorValue = textColorValue;
            return this;
        }


        public Builder setGravity(int gravity) {
            this.gravity = gravity;

            return this;
        }

        public Builder setImageDrawable(Drawable imageDrawable) {
            this.imageDrawable = imageDrawable;

            return this;
        }

        public Builder setImageResource(int imageResId) {
            this.imageResId = imageResId;

            return this;
        }

        /**
         * The text size in sp.
         */
        public Builder setTextSize(int textSize) {
            this.textSize = textSize;
            return this;
        }

        /**
         * The text shadow color resource id.
         */
        public Builder setTextShadowColor(int textShadowColorResId) {
            this.textShadowColorResId = textShadowColorResId;
            return this;
        }

        /**
         * The text shadow radius.
         */
        public Builder setTextShadowRadius(float textShadowRadius) {
            this.textShadowRadius = textShadowRadius;
            return this;
        }

        /**
         * The text shadow horizontal offset.
         */
        public Builder setTextShadowDx(float textShadowDx) {
            this.textShadowDx = textShadowDx;
            return this;
        }

        /**
         * The text shadow vertical offset.
         */
        public Builder setTextShadowDy(float textShadowDy) {
            this.textShadowDy = textShadowDy;
            return this;
        }

        /**
         * The text appearance resource id for the text.
         */
        public Builder setTextAppearance(int textAppearanceResId) {
            this.textAppearanceResId = textAppearanceResId;
            return this;
        }

        /**
         * The {@link ImageView.ScaleType} for the image.
         */
        public Builder setImageScaleType(ImageView.ScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
            return this;
        }

        /**
         * The padding for the Snackbar view's content in pixels.
         */
        public Builder setPaddingInPixels(int padding) {
            this.paddingInPixels = padding;
            return this;
        }

        /**
         * The resource id for the padding for the Snackbar view's content.
         */
        public Builder setPaddingDimensionResId(int paddingResId) {
            this.paddingDimensionResId = paddingResId;
            return this;
        }

        /**
         * The file path and name of the font for the Snackbar view's content.
         */
        public Builder setFontName(String fontName) {
            this.fontName = fontName;
            return this;
        }

        /**
         * The resource id for the file path and name of the font for the Snackbar view's content.
         */
        public Builder setFontNameResId(int fontNameResId) {
            this.fontNameResId = fontNameResId;
            return this;
        }

        public Style build() {
            return new Style(this);
        }
    }

}