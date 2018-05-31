package com.tuwan.yuewan.utils;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;

import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.VideoPlayEntity;
import com.tuwan.yuewan.ui.activity.VideoPlayActivity;


/**
 * Created by shuyu on 2016/11/11.
 */
public class JumpUtils {

    /**
     * 跳转到视频播放
     *
     * @param activity
     * @param view
     */
    public static void goToVideoPlayer(Activity activity, View view, VideoPlayEntity entity) {
        Intent intent = new Intent(activity, VideoPlayActivity.class);
        intent.putExtra(VideoPlayActivity.TRANSITION, true);
        intent.putExtra("entity", entity);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Pair pair = new Pair<>(view, VideoPlayActivity.IMG_TRANSITION);
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity, pair);
            ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
        } else {
            activity.startActivity(intent);
            activity.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        }
    }


}
