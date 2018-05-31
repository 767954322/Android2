package com.tuwan.yuewan.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.tuwan.common.net.CommonObserver;
import com.tuwan.common.net.ServiceFactory;
import com.tuwan.common.ui.fragment.base.AbsListFragment;
import com.tuwan.yuewan.entity.ServiceCommentBean;
import com.tuwan.yuewan.entity.ServiceDetialBean;
import com.tuwan.yuewan.service.YService;
import com.tuwan.yuewan.ui.item.TeacherServiceActivityHeaderProvider;
import com.tuwan.yuewan.ui.item.TeacherServiceCommentProvider;
import com.tuwan.yuewan.ui.widget.TeacherBtmView;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by zhangjie on 2017/10/17.
 */
@SuppressLint("ValidFragment")
public class TeacherServiceDetialFragment extends AbsListFragment {

    private int mSid;
    private ArrayList mData = new ArrayList();
    private TeacherServiceActivityHeaderProvider mHeaderProvider;
    private TeacherBtmView mTeacherBtmView;
    private int online;
    private int state = 0;

    public TeacherServiceDetialFragment(){
        super();
    }
    public TeacherServiceDetialFragment(TeacherBtmView teacherBtmView) {
        mTeacherBtmView = teacherBtmView;
    }

    @Override
    protected void registerItemProvider(MultiTypeAdapter adapter) {
        adapter.register(ServiceCommentBean.DataBean.class, new TeacherServiceCommentProvider(this));
        adapter.register(ServiceDetialBean.class, mHeaderProvider);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSid = activity.getIntent().getIntExtra("sid", 0);
        online = activity.getIntent().getIntExtra("online", 0);
        mHeaderProvider = new TeacherServiceActivityHeaderProvider(this);
    }

    @Override
    public void loadData(final int pageIndex) {

        Observable<ServiceCommentBean> observableComment = ServiceFactory.getNoCacheInstance()
                .createService(YService.class)
                .getCommentListApi_Content("json", mSid, pageIndex, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        if (pageIndex == 1) {
            Observable<ServiceDetialBean> observableServiceDetial = ServiceFactory.getShortCacheInstance()
                    .createService(YService.class)
                    .getServiceInfo_Content("json", mSid)
                    .subscribeOn(Schedulers.io());
            Observable.zip(observableComment, observableServiceDetial, new BiFunction<ServiceCommentBean, ServiceDetialBean, ServiceCommentBean>() {
                @Override
                public ServiceCommentBean apply(@NonNull ServiceCommentBean serviceCommentBean, @NonNull final ServiceDetialBean serviceDetialBean) throws Exception {
                    serviceDetialBean.CountNum = serviceCommentBean.CountNum;
                    serviceDetialBean.avg = serviceCommentBean.avg;
                    mData.add(serviceDetialBean);
                    mData.addAll(serviceCommentBean.data);
                    final int _imOnline = serviceDetialBean.info.im_online;
                    final String _gameName = serviceDetialBean.info.gamename;
                    final int _teacherId = serviceDetialBean.info.teacherid;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTeacherBtmView.setOnLines(online);
                            mTeacherBtmView. setOnlineStatus(_imOnline, _gameName, _teacherId);
                        }
                    });
                    return serviceCommentBean;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new CommonObserver<ServiceCommentBean>() {
                        @Override
                        public void onNext(@NonNull ServiceCommentBean result) {
                            super.onNext(result);
                            onDataSuccessReceived(pageIndex, mData, pageIndex >= result.CountPage);
                            disAbleRefresh();
                        }
                    });
        } else {
            observableComment
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new CommonObserver<ServiceCommentBean>() {
                        @Override
                        public void onNext(@NonNull ServiceCommentBean result) {
                            super.onNext(result);
                            mData.addAll(result.data);
//                            Log.e("eeeeeeeeeeeee",mData.size()+""+mData.get(1).toString());
                            onDataSuccessReceived(pageIndex, result.data, pageIndex >= result.CountPage);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            super.onError(e);
                        }
                    });
        }
    }

    @Override
    protected boolean disableLazyLoad() {
        return true;
    }

    @Override
    protected int getInitPageIndex() {
        return 1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mHeaderProvider != null) {
            mHeaderProvider.detachView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        mHeaderProvider.initClickEvent();
        if (state > 0) {
//            mHeaderProvider.detachView();
            mHeaderProvider.initClickEvent();
        }else {
            state = state + 1;
        }
    }
}
