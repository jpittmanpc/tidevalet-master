package com.tidevalet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;

/**
 * Created by Justin on 11/22/15.
 */
public class BitmapText {
        public static void drawTextBitmap(Bitmap image, String msg) {
            Canvas canvas = new Canvas(image);
            TextPaint mStrokePaint = new TextPaint();
            mStrokePaint.setTextSize(30);
            mStrokePaint.setColor(Color.YELLOW);
            mStrokePaint.setStyle(Paint.Style.FILL);
            mStrokePaint.setTypeface(Typeface.DEFAULT);
            canvas.save();
            StaticLayout strokeLayout = new StaticLayout(msg, mStrokePaint, image.getWidth() * 3 / 5,
                    Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);
            canvas.translate(image.getWidth() / 5, image.getHeight() - strokeLayout.getHeight() - 20);
            strokeLayout.draw(canvas);
            canvas.save();
            canvas.restore();
        }
}
