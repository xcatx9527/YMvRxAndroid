package com.xile.script.utils.captureX;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import com.xile.script.base.ScriptApplication;
import com.xile.script.imagereact.ScreenShotFb;

import java.util.ArrayList;

import static script.tools.config.LogConfig.LOG_TAG;

/**
 * @author cy
 * @time 2019/12/6$
 * @descrition:
 */
public class GetPurplesTask extends AsyncTask<String, Integer, Integer> {

    ArrayList<Rect> rects = new ArrayList<>();
    int score;
    boolean isFirst = true;
    boolean isEmpty;
    ResultCallBack resultCallBack;

    public GetPurplesTask(ResultCallBack resultCallBack) {
        this.resultCallBack = resultCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).startVirtual();
        SystemClock.sleep(100);
    }

    @Override
    protected Integer doInBackground(String... args) {
        float scale = Float.valueOf(args[1]);
        int x = Integer.valueOf(args[2]);
        int y = Integer.valueOf(args[3]);
        int endx = Integer.valueOf(args[4]);
        int endy = Integer.valueOf(args[5]);
        int addGraysThreshold = Integer.valueOf(args[6]);
        int addGraysInterval = Integer.valueOf(args[7]);
        int addPurplesThreshold = Integer.valueOf(args[8]);
        int addPurplesInterval = Integer.valueOf(args[9]);
        int times = Integer.valueOf(args[10]);
        int tscore = Integer.valueOf(args[11]);
        for (int i = 0; i < times; i++) {
            Bitmap bitmap =ScreenShotFb.getInstance(ScriptApplication.getInstance()).startCaptureX(scale, x, y, endx, endy);
            if (bitmap != null) {
                isEmpty = addGrays(bitmap, addGraysThreshold, addGraysInterval);
                if ((score > tscore || !isFirst) && !isEmpty) {
                    if (isFirst) {
                        score = 0;
                        isFirst = false;
                    }
                    if (score < tscore) {
                        addPurples(bitmap, addPurplesThreshold, addPurplesInterval,scale,x,y);
                    } else {
                        Log.e(LOG_TAG, "已下一轮，跳出");
                        break;
                    }

                }
            }
        }
        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        resultCallBack.onResult(rects);
        for (int i = 0; i < rects.size(); i++) {
            Log.e("LOG_TAG", rects.get(i).centerX() + "--" + rects.get(i).centerY());
        }
        ScreenShotFb.getInstance(ScriptApplication.getInstance()).closeObject();
    }

    public boolean addGrays(Bitmap largeBitmap, int threshold, int interval) {
        if (largeBitmap == null) {
            return false;
        }
        int bw = largeBitmap.getWidth();
        int bh = largeBitmap.getHeight();
        int[] pixs = new int[bh * bw];
        largeBitmap.getPixels(pixs, 0, bw, 0, 0, bw, bh);
        for (int i = interval * bw; i < pixs.length - interval * bw; i++) {
            if (i < pixs.length - 2 * interval * bw) {
                if (isGray(pixs[i], threshold) && isGray(pixs[i + interval], threshold) && isGray(pixs[i + 2 * interval], threshold)
                        && isGray(pixs[i + interval * bw], threshold) && isGray(pixs[i + interval * bw + interval], threshold) && isGray(pixs[i + interval * bw + 2 * interval], threshold)
                        && isGray(pixs[i + 2 * interval * bw], threshold) && isGray(pixs[i + 2 * interval * bw + interval], threshold) && isGray(pixs[i + 2 * interval * bw + 2 * interval], threshold)) {
                    score++;
//                    Log.e(LOG_TAG, "找到空白图:" + score);
                    return true;
                }
            }

        }
        return false;
    }

    public Rect addPurples(Bitmap largeBitmap, int threshold, int interval, float scale, int cutx, int cuty) {
        if (largeBitmap == null) {
            return null;
        }
        int bw = largeBitmap.getWidth();
        int bh = largeBitmap.getHeight();
        int[] pixs = new int[bh * bw];
        largeBitmap.getPixels(pixs, 0, bw, 0, 0, bw, bh);
        for (int i = interval * bw; i < pixs.length - interval * bw; i++) {
            //判断中心外四个点也是紫色
            if (isPurple(pixs[i], threshold) && isPurple(pixs[i - interval], threshold) && isPurple(pixs[i + interval], threshold) && isPurple(pixs[i - interval * bw], threshold) && isPurple(pixs[i + interval * bw], threshold)) {
                Rect rect = new Rect();
                rect.left = (int) (i % bw/scale+cutx);
                rect.right = (int) (i % bw/scale+cutx);
                rect.top = (int) (i / bw/scale+cuty);
                rect.bottom = (int) (i / bw/scale+cuty);
                if (rects.size() == 0) {
                    rects.add(rect);
                } else if (Math.abs(rects.get(rects.size() - 1).centerY() - rect.centerY()) > 30 || Math.abs(rects.get(rects.size() - 1).centerX() - rect.centerX()) > 30) {
                    rects.add(rect);
                }
//                Log.e(LOG_TAG, (rect.centerX()) + "--" + (rect.centerY()));
                return rect;
            }
        }
        return null;
    }

    public boolean isPurple(int a, int threshold) {
        return Color.blue(a) - Color.green(a) > threshold;
    }

    public boolean isGray(int a, int threshold) {
        return Math.abs(Color.blue(a) - threshold) < 10 && Math.abs(Color.green(a) - threshold) < 10 && Math.abs(Color.red(a) - threshold) < 10;
    }

    public interface ResultCallBack {
        void onResult(ArrayList<Rect> var1);
    }
}