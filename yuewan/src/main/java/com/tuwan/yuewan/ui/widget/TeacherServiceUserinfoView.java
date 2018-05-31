package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tuwan.common.ui.activity.base.BaseActivity;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.YApp;
import com.tuwan.yuewan.utils.AppUtils;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * 技能服务页顶部用户信息
 */
public class TeacherServiceUserinfoView extends RelativeLayout {

    private float mHeight = YApp.app.getResources().getDimension(R.dimen.dimen_65);

    public TeacherServiceUserinfoView(Context context) {
        this(context, null);
    }

    public TeacherServiceUserinfoView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public TeacherServiceUserinfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_service_userinfo, this);
        setBackgroundColor(0xffffffff);
        assignViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) mHeight, MeasureSpec.EXACTLY);// 高度包裹内容, wrap_content;当包裹内容时,参1表示尺寸最大值,暂写2000, 也可以是屏幕高度
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private ImageView mIvServiceAvart;
    private ImageView mTvServiceAttention,mTvServiceAttention2;
    private TextView mTvServiceName;
    private TextView mTvServiceAge;
    private TextView mTvServiceTag;

    private void assignViews() {
        mIvServiceAvart = (ImageView) findViewById(R.id.iv_service_avart);
        mTvServiceAttention = (ImageView) findViewById(R.id.tv_service_attention);
        mTvServiceAttention2 = (ImageView) findViewById(R.id.tv_service_attention2);
        mTvServiceName = (TextView) findViewById(R.id.tv_service_name);
        mTvServiceAge = (TextView) findViewById(R.id.tv_service_age);
        mTvServiceTag = (TextView) findViewById(R.id.tv_service_tag);
    }

    public ImageView getIvServiceAvart() {
        return mIvServiceAvart;
    }

    public ImageView getTvServiceAttention() {
        return mTvServiceAttention;
    }
    public ImageView getmTvServiceAttention2() {
        return mTvServiceAttention2;
    }

    public TextView getTvServiceName() {
        return mTvServiceName;
    }

    public TextView getTvServiceAge() {
        return mTvServiceAge;
    }

    public TextView getTvServiceTag() {
        return mTvServiceTag;
    }

    public void setUpData(BaseActivity mContext, String avatar, String nickname, int age, int sex) {
        ImageView ivServiceAvart = getIvServiceAvart();
        Glide.with(mContext).load(avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivServiceAvart);

        TextView tvServiceName = getTvServiceName();
        tvServiceName.setText(nickname);

        TextView tvServiceAge = getTvServiceAge();
        AppUtils.setDataAgeAndGender(age, sex, tvServiceAge,
                YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small), YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small));
    }

    public void setUpData(Context mContext, String avatar, String nickname, int age, int sex, int videocheck, int Attention) {
        ImageView ivServiceAvart = getIvServiceAvart();
        Glide.with(mContext)
                .load(avatar)
                .apply(bitmapTransform(new RoundedCornersTransformation(100, 0, RoundedCornersTransformation.CornerType.ALL)))
                .into(ivServiceAvart);

        TextView tvServiceName = getTvServiceName();
        tvServiceName.setText(nickname);

        TextView tvServiceAge = getTvServiceAge();
        AppUtils.setDataAgeAndGender(age, sex, tvServiceAge,
                YApp.getInstance().getResources().getDrawable(R.drawable.ic_boy_small), YApp.getInstance().getResources().getDrawable(R.drawable.ic_gril_small));

        //真人认证
        TextView tvServiceTag = getTvServiceTag();
        AppUtils.initVisiableWithGone(tvServiceTag, videocheck == 1);

        //关注
//        AppUtils.initAttentionIv(getTvServiceAttention(), Attention);
    }

    public void updateAttentionStatus(boolean isAttention){
        //关注
//        AppUtils.initAttentionIv(getTvServiceAttention(), isAttention?1:-1);
    }

    public void setImage(){
        mTvServiceAttention2.setVisibility(View.INVISIBLE);
    }

}
