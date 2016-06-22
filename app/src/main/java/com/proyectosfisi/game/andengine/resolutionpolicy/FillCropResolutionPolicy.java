package com.proyectosfisi.game.andengine.resolutionpolicy;

import android.view.View;

import org.andengine.engine.options.resolutionpolicy.BaseResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

/**
 * Created by edson on 21/06/2016.
 */
public class FillCropResolutionPolicy extends BaseResolutionPolicy {

    private final float mRatio;

    private float left;
    private float right;
    private float top;
    private float bottom;

    public FillCropResolutionPolicy(final float pWidthRatio, final float pHeightRatio) {
        this.mRatio = pWidthRatio / pHeightRatio;
        left = 0;
        top = pHeightRatio;
        right = pWidthRatio;
        bottom = 0;
    }

    @Override
    public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {
        BaseResolutionPolicy.throwOnNotMeasureSpecEXACTLY(pWidthMeasureSpec, pHeightMeasureSpec);

        final int specWidth = View.MeasureSpec.getSize(pWidthMeasureSpec);
        final int specHeight = View.MeasureSpec.getSize(pHeightMeasureSpec);

        final float desiredRatio = this.mRatio;
        final float realRatio = ((float) specWidth) / specHeight;

        int measuredWidth;
        int measuredHeight;
        if (realRatio < desiredRatio) {
            measuredWidth = specWidth;
            measuredHeight = Math.round(measuredWidth / desiredRatio);
        } else {
            measuredHeight = specHeight;
            measuredWidth = Math.round(measuredHeight * desiredRatio);
        }

        pResolutionPolicyCallback.onResolutionChanged(measuredWidth, measuredHeight);
    }

    public float getLeft() {
        return left;
    }

    public float getRight() {
        return right;
    }

    public float getTop() {
        return top;
    }

    public float getBottom() {
        return bottom;
    }

}
