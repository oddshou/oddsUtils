/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.odds.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class ClicpImageProgress extends AppCompatImageView {





    float width,height;
    Path path;

    public static final int radious = 18;

    public ClicpImageProgress(Context context) {
        this(context, null);
    }

    public ClicpImageProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClicpImageProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        path = new Path();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (width > radious && height > radious) {
            path.reset();
            path.moveTo(radious, 0);
            path.lineTo(width - radious, 0);
            path.quadTo(width, 0, width, radious);
            path.lineTo(width, height - radious);
            path.quadTo(width, height, width - radious, height);
            path.lineTo(radious, height);
            path.quadTo(0, height, 0, height - radious);
            path.lineTo(0, radious);
            path.quadTo(0, 0, radious, 0);
            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
