package com.tuwan.yuewan.ui.widget;

import android.util.Log;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.yuewan.entity.IsCompletes;
import com.tuwan.yuewan.service.YService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/5.
 */

public class IsComplete {
    private static IsComplete sInstance;
    private int iscomplete;
    public static IsComplete getInstance() {
        if (sInstance == null) {
            sInstance = new IsComplete();
        }
        return sInstance;
    }
    public int resultof()  {
        ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .iscomplete("json","app")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<IsCompletes>() {
                    @Override
                    public void onNext(@NonNull IsCompletes result) {
                        super.onNext(result);
                        Log.d("result", result.getCheck() + "");
                        int check = result.getCheck();
                        iscomplete=check;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        super.onError(e);
                    }
                });
        return iscomplete;
    }
}
