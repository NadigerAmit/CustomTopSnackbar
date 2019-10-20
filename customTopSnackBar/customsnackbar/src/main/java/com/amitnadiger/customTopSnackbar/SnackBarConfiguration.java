package com.amitnadiger.customTopSnackbar;


public class SnackBarConfiguration {
    public static final int DURATION_INFINITE = -1;
    public static final int DURATION_SHORT = 3000;
    public static final int DURATION_LONG = 5000;

    public static final SnackBarConfiguration DEFAULT;

    static {
        DEFAULT = new Builder().setDuration(DURATION_SHORT).build();
    }

    final int durationInMilliseconds;
    final int inAnimationResId;
    final int outAnimationResId;

    private SnackBarConfiguration(Builder builder) {
        this.durationInMilliseconds = builder.durationInMilliseconds;
        this.inAnimationResId = builder.inAnimationResId;
        this.outAnimationResId = builder.outAnimationResId;
    }

    @Override
    public String toString() {
        return "SnackBarConfiguration{" +
                "durationInMilliseconds=" + durationInMilliseconds +
                ", inAnimationResId=" + inAnimationResId +
                ", outAnimationResId=" + outAnimationResId +
                '}';
    }

    public static class Builder {
        private int durationInMilliseconds = DURATION_SHORT;
        private int inAnimationResId = 0;
        private int outAnimationResId = 0;

        public Builder setDuration(final int duration) {
            this.durationInMilliseconds = duration;

            return this;
        }

        public Builder setInAnimation(final int inAnimationResId) {
            this.inAnimationResId = inAnimationResId;

            return this;
        }

        public Builder setOutAnimation(final int outAnimationResId) {
            this.outAnimationResId = outAnimationResId;

            return this;
        }

        public SnackBarConfiguration build() {
            return new SnackBarConfiguration(this);
        }
    }

}
