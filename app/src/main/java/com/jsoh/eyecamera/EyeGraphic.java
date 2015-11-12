package com.jsoh.eyecamera;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.SoundPool;

import com.google.android.gms.vision.face.Face;
import com.jsoh.eyecamera.ui.camera.GraphicOverlay;
import com.jsoh.eyecamera.utils.SoundUtil;

/**
 * Created by junsuk on 2015. 11. 12..
 */
public class EyeGraphic extends FaceGraphic {
    private final Paint mEyePaint;
    private Paint mMouthPaint;

    private SoundPool mSoundPool;

    EyeGraphic(GraphicOverlay overlay) {
        super(overlay);

        mEyePaint = new Paint();
        mEyePaint.setColor(Color.BLACK);
        mEyePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mMouthPaint = new Paint();
        mMouthPaint.setColor(Color.YELLOW);
        mMouthPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mSoundPool = SoundUtil.buildSoundPool(2);
    }

    @Override
    public void draw(Canvas canvas) {
        Face face = mFace;
        if (face == null) {
            return;
        }

        // 중심점
        float centerX = canvas.getWidth() / 2.0f;
        float centerY = canvas.getHeight() / 2.0f - 100;

        // Draws a circle at the position of the detected face, with the face's track id below.
        float x = translateX(face.getPosition().x + face.getWidth() / 2);
        float y = translateY(face.getPosition().y + face.getHeight() / 2);

        // 중점과 얼굴의 좌표 차
        float deltaX = x - centerX;
        float deltaY = y - centerY;

        // 최소값, 최대값 제한
        if (deltaX < -90) deltaX = -90;
        if (deltaX > 90) deltaX = 90;
        if (deltaY < -90) deltaY = -90;
        if (deltaY > 90) deltaY = 90;

        // 눈알 그리기
        canvas.drawCircle(centerX - 200 + deltaX, centerY + deltaY, 90, mEyePaint);
        canvas.drawCircle(centerX + 200 + deltaX, centerY + deltaY, 90, mEyePaint);

        // 입 그리기
        if (face.getIsSmilingProbability() < 0.1f) {
            RectF oval = new RectF(centerX - 200, centerY + 400, centerX + 200, centerY + 460);
            canvas.drawRect(oval, mMouthPaint);
        } else {
            RectF oval = new RectF(centerX - 200, centerY + 200, centerX + 200, centerY + 460);
            canvas.drawArc(oval, 0, 180, true, mMouthPaint);
        }
    }
}
