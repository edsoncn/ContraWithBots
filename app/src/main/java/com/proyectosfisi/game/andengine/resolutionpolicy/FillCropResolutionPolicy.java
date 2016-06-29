package com.proyectosfisi.game.andengine.resolutionpolicy;

import android.view.View;

import org.andengine.engine.options.resolutionpolicy.BaseResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;

/**
 * Created by edson on 21/06/2016.
 */
public class FillCropResolutionPolicy extends BaseResolutionPolicy {

    private final float desiredWidth;
    private final float desiredHeight;

    private float userWidth;
    private float userHeight;

    private float left;
    private float right;
    private float top;
    private float bottom;

    public FillCropResolutionPolicy(final float pWidth, final float pHeight) {
        desiredWidth = pWidth;
        desiredHeight = pHeight;
    }

    @Override
    public void onMeasure(final IResolutionPolicy.Callback pResolutionPolicyCallback, final int pWidthMeasureSpec, final int pHeightMeasureSpec) {

        final int measuredWidth = View.MeasureSpec.getSize(pWidthMeasureSpec);
        final int measuredHeight = View.MeasureSpec.getSize(pHeightMeasureSpec);

        final float desiredRatio = (float) desiredWidth / (float) desiredHeight;
        float scaleRatio;

        float resultWidth;
        float resultHeight;

        // Scale to fit height, width will crop
        resultWidth = measuredHeight * desiredRatio;
        resultHeight = measuredHeight;
        scaleRatio = desiredHeight / resultHeight;

        userWidth = measuredWidth * scaleRatio;
        userHeight = measuredHeight * scaleRatio;

        left = (desiredWidth - userWidth) / 2f;
        right = userWidth + left;
        bottom = (desiredHeight - userHeight) / 2f;
        top = userHeight + bottom;

        pResolutionPolicyCallback.onResolutionChanged(Math.round(resultWidth), Math.round(resultHeight));

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
