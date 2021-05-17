package com.project.chatter.constant;

import io.agora.rtc.video.BeautyOptions;

public class ConstantsChatter {

    private static final int BEAUTY_EFFECT_DEFAULT_CONTRAST = BeautyOptions.LIGHTENING_CONTRAST_HIGH;
    private static final float BEAUTY_EFFECT_DEFAULT_LIGHTNESS = 0.5f;
    private static final float BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = 1f;
    private static final float BEAUTY_EFFECT_DEFAULT_REDNESS = 1f;

    public static final BeautyOptions DEFAULT_BEAUTY_OPTIONS = new BeautyOptions(
            BEAUTY_EFFECT_DEFAULT_CONTRAST,
            BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
            BEAUTY_EFFECT_DEFAULT_SMOOTHNESS,
            BEAUTY_EFFECT_DEFAULT_REDNESS);
}
