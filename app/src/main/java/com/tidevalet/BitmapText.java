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


        public static void drawTextBitmap(Bitmap bg, String msg) {
            Canvas canvas = new Canvas(bg);

            TextPaint mStrokePaint = new TextPaint();
            mStrokePaint.setTextSize(15);
            mStrokePaint.setColor(Color.YELLOW);
            mStrokePaint.setAntiAlias(true);

            mStrokePaint.setStyle(Paint.Style.FILL);
            //mStrokePaint.setStrikeThruText(true);
            mStrokePaint.setHinting(Paint.HINTING_OFF);
            //mStrokePaint.setShadowLayer(10f, -1f, -1f, 0xffffffff);
            mStrokePaint.setSubpixelText(true);
            mStrokePaint.setFilterBitmap(false);
            mStrokePaint.setTypeface(Typeface.DEFAULT);

        /*mStrokePaint.setStrokeCap(Paint.Cap.BUTT);
        mStrokePaint.setStrokeJoin(Paint.Join.MITER);
        mStrokePaint.setStrokeMiter(10f);
        mStrokePaint.setStrokeWidth(10f);*/
           canvas.save();


            StaticLayout strokeLayout = new StaticLayout(msg, mStrokePaint, bg.getWidth() * 3 / 5,
                    Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);

            canvas.translate(bg.getWidth() / 5, bg.getHeight() - strokeLayout.getHeight() - 20);


            TextPaint mTextPaint = new TextPaint();
            mTextPaint.setTextSize(15);
            mTextPaint.setColor(Color.BLACK);
            mTextPaint.setAntiAlias(true);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setStrokeWidth(1);
           // mTextPaint.setShadowLayer(01f, 01f, 0, Color.YELLOW);
            mTextPaint.setFakeBoldText(true);
            mTextPaint.setFilterBitmap(false);
            mTextPaint.setTypeface(Typeface.DEFAULT);
            StaticLayout textLayout = new StaticLayout(msg, mTextPaint, bg.getWidth() * 3 / 5,
                    Alignment.ALIGN_NORMAL, 1.0f, 1.0f, false);

            textLayout.draw(canvas);
            strokeLayout.draw(canvas);
            //canvas.save();
            canvas.restore();
        }

    }
