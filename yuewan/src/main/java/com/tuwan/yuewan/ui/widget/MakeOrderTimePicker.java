package com.tuwan.yuewan.ui.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.IntDef;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.tuwan.common.LibraryApplication;
import com.tuwan.common.utils.DateUtils;
import com.tuwan.common.utils.LogUtil;
import com.tuwan.common.utils.StringUtils;
import com.tuwan.yuewan.R;
import com.tuwan.yuewan.entity.MakeOrderRecentOrderBean;
import com.tuwan.yuewan.entity.TimePikcerEntity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.tuwan.common.utils.DateUtils.yyyyMMddHHmmss;

/**
 * Created by zhangjie on 2017/10/20.
 * 下订单页选择时间的侧滑菜单
 */
public class MakeOrderTimePicker extends FrameLayout implements RulerTimeView.PositionListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private RadioGroup mRgDay;
    private RadioButton mRb1, mRb2, mRb3;
    private FlexboxLayout mFlexbox;
    private TextView mTvTimeLength;
    private RulerTimeView mRulerView;

    private TextView mTvReset;
    private TextView mTvConfirm;

    private final int mNodeArrayLength = 144;

    ColorStateList colorStateList = getResources().getColorStateList(R.color.selector_textcolor_makeorder_starttime);
    /**
     * 保存所有的checkbox
     */
    private ArrayList<CheckBox> mCbs = new ArrayList<>();
    private TimePikcerEntity[] mNodeArray = new TimePikcerEntity[mNodeArrayLength];
    private List<TimePikcerEntity> mNodeList;//mNodeList是mNodeArray asList获取的
    private TimePikcerEntity mCheckedEntity;

    private ArrayList<Integer> mDayList = new ArrayList<>();

    int pickerWidth = LibraryApplication.SCREEN_WIDTH * 625 / 750;

    private MakeOrderRecentOrderBean mHttpResult;
    private int mStartPosition;
    private int mEndPosition;
    private int mDayChecked;

    public MakeOrderTimePicker(Context context) {
        super(context);
        initView(context);
    }


    public MakeOrderTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MakeOrderTimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(pickerWidth, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.widget_makeorder_time_picker, this);
        setBackgroundColor(0xffffffff);
        assignViews();
        mNodeList = Arrays.asList(mNodeArray);

        mRgDay.setOnCheckedChangeListener(this);

        //选择时长初始化
        initRulerView(-1);
        mRulerView.setOnPositionListener(this);

        //这里计算了flexbox的宽度
        int flexBoxContentWidth = pickerWidth - getResources().getDimensionPixelOffset(R.dimen.dimen_15) * 2;
        int cbWidth = flexBoxContentWidth / 6;
        int cbHeight = cbWidth * 50 / 86;


        int childCount = mFlexbox.getChildCount();
        for (int position = 0; position < childCount; position++) {
            CheckBox childAt = (CheckBox) mFlexbox.getChildAt(position);
            ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
            //没有考虑CheckBox之间的距离
            layoutParams.width = cbWidth;
            layoutParams.height = cbHeight;

            mCbs.add(childAt);
            childAt.setOnCheckedChangeListener(this);
        }
    }

    /**
     * 时长控件初始话状态
     */
    private void initRulerView(final int hours) {
        mRulerView.post(new Runnable() {
            @Override
            public void run() {
                int disableData = hours + 1;
                if (hours == -1) {
                    disableData = -1;
                }
                mRulerView.smoothScrollTo(1);
                mRulerView.setDisableData(disableData);
            }
        });
    }

    //所有的连续NODE_LIFELESS出现的前一个node都是NODE_UNACTIVE
    public static final int NODE_ACTIVE = 101;
    public static final int NODE_UNACTIVE = 102;//不要使用该字段进行操作
    public static final int NODE_LIFELESS = 103;

    @IntDef({NODE_ACTIVE, NODE_UNACTIVE, NODE_LIFELESS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NODE_TYPE {
    }


    private void assignViews() {
        mRgDay = (RadioGroup) findViewById(R.id.rg_day);
        mRb1 = (RadioButton) findViewById(R.id.rb1);
        mRb2 = (RadioButton) findViewById(R.id.rb2);
        mRb3 = (RadioButton) findViewById(R.id.rb3);
        mFlexbox = (FlexboxLayout) findViewById(R.id.flexbox);
        mTvTimeLength = (TextView) findViewById(R.id.tv_widget_makeorder_time_length);
        mRulerView = (RulerTimeView) findViewById(R.id.ruler_view);
        mTvReset = (TextView) findViewById(R.id.tv_widget_makeorder_time_reset);
        mTvConfirm = (TextView) findViewById(R.id.tv_widget_makeorder_time_confirm);
    }


    /**
     * 向外暴露的方法，设置数据给当前控件
     */
    public void setUpData(MakeOrderRecentOrderBean httpResult) {
        this.mHttpResult = httpResult;

        int startHour = Integer.valueOf(mHttpResult.teachinfo.timeStart.substring(0, 2));
        int startMinute = Integer.valueOf(mHttpResult.teachinfo.timeStart.substring(3, 5));
        int endHour = Integer.valueOf(mHttpResult.teachinfo.timeEnd.substring(0, 2));
        int endMinute = Integer.valueOf(mHttpResult.teachinfo.timeEnd.substring(3, 5));
        mStartPosition = startHour * 2 + startMinute / 30;
        mEndPosition = endHour * 2 + endMinute / 30;

        if (mHttpResult.date != null) {

            for (int i = 0; i < 3; i++) {
                MakeOrderRecentOrderBean.DateBean dateBean = mHttpResult.date.get(i);
                RadioButton rb = (RadioButton) mRgDay.getChildAt(i);
                rb.setText(dateBean.datestr);
                rb.setVisibility(View.VISIBLE);

                String date = dateBean.date;
                mDayList.add(Integer.valueOf(date.substring(date.length() - 2)));
            }
        }

        computeHour(mHttpResult);

    }

    /**
     * @param hour     小时
     * @param min      分钟
     * @param dayIndex （例子：0 表示是今天）范围是0-2
     * @return
     */
    private int getNodePosition(int hour, int min, int dayIndex) {
        //一天有48个节点
        int dayNodeOffset = 48;
        return hour * 2 + min / 30 + dayIndex * dayNodeOffset;
    }


    /**
     * 计算三天中哪些小时置灰，并获得这些时间
     * 之前的半小时
     * <p>
     * 3天一共是 3*48个时间节点（又分为3类）：可选节点，被约节点（置灰并setEnabled false），不可选节点（不置灰，setEnabled false）
     * 并记录每个节点最长可约时间
     */
    private void computeHour(MakeOrderRecentOrderBean mHttpResult) {

//        测试用代码
//        MakeOrderRecentOrderBean.RecentOrderBean recentOrderBean1 = new MakeOrderRecentOrderBean.RecentOrderBean();
//        recentOrderBean1.endTimes = 1508760000;
//        recentOrderBean1.startTimes = 1508756400;
//        recentOrderBean1.id = 44285;
//        recentOrderBean1.startTime = "19:00";
//        recentOrderBean1.endTime = "20:00";
//        recentOrderBean1.dateEnd = "2017-10-23";
//        mHttpResult.RecentOrder = new ArrayList<>();
//        mHttpResult.RecentOrder.add(recentOrderBean1);

        if (mHttpResult.RecentOrder != null) {
            for (MakeOrderRecentOrderBean.RecentOrderBean recentOrderBean : mHttpResult.RecentOrder) {

                Date date = new Date();
                date.setTime(Long.valueOf(recentOrderBean.startTimes) * 1000);
                String dateStr = DateUtils.formatDate(date, yyyyMMddHHmmss);
                int day = Integer.valueOf(dateStr.substring(8, 10));
                int dayIndex = mDayList.indexOf(day);
                if (dayIndex == -1) {
                    //由于上一段代码是从开始时间考虑的，没有考虑跨天的情况。
                    //这里从结束时间来计算
                    if (Long.valueOf(recentOrderBean.endTimes) * 1000 > DateUtils.getTodayZero()) {
                        date.setTime(DateUtils.getTodayZero());
                        dateStr = DateUtils.formatDate(date, yyyyMMddHHmmss);
                        day = Integer.valueOf(dateStr.substring(8, 10));
                        dayIndex = mDayList.indexOf(day);
                    } else {
                        break;
                    }
                }
                int hour = Integer.valueOf(dateStr.substring(11, 13));
                int minute = Integer.valueOf(dateStr.substring(14, 16));
                //获取被约了的时间的position
                int nodePosition = getNodePosition(hour, minute, dayIndex);
                LogUtil.e("day:" + day + "hour:" + hour + " minute:" + minute + " dayIndex:" + dayIndex + " nodePosition:" + nodePosition);

                //被约了的时间的前一个node是unactive
                //第一天00:00的处理
                if (nodePosition != 0) {
                    mNodeArray[nodePosition - 1] = new TimePikcerEntity(NODE_UNACTIVE);
                }

                //已经被约了的时间，这些时间都是lifeless
                mNodeArray[nodePosition] = new TimePikcerEntity();
                //被约的小时数，因为是小时数，所以每次for循环增加两个node
                int hourNumber = (recentOrderBean.endTimes - recentOrderBean.startTimes) / 60 / 60;//1
                for (int i = 1; i < hourNumber + 1; i++) {
                    //超过的时间不能添加
                    if (nodePosition + i <= mNodeArrayLength - 1) {
                        mNodeArray[nodePosition + i] = new TimePikcerEntity();
                        if (nodePosition + i * 2 <= mNodeArrayLength - 1 && i != hourNumber) {
                            //当i == hourNumber 时，不能添加。例子:约单是7点结束。那么7点可以立马被约单
                            mNodeArray[nodePosition + i * 2] = new TimePikcerEntity();
                        }
                    }
                }
            }
        }


        //获得当前时间的position
        int currentTimeNodePosition = getNodePosition(DateUtils.getHour(), DateUtils.getMinute(), 0);
        //当前时间的position小的都是lifeless
        for (int position = 0; position < mNodeArrayLength; position++) {
            if (currentTimeNodePosition >= position) {
                mNodeArray[position] = new TimePikcerEntity();
                continue;
            }
        }

        //导师自己的不接单时间,都是lifeless
        for (int arrayPosition = 0; arrayPosition < mNodeList.size(); arrayPosition++) {
            TimePikcerEntity timePikcerEntity = mNodeArray[arrayPosition];
            if (timePikcerEntity == null) {
                boolean teacherOnline = isTeacherOnline(arrayPosition);
                if (!teacherOnline) {
                    //同理前一个node是NODE_UNACTIVE状态
                    if (arrayPosition != 0 && mNodeArray[arrayPosition - 1] == null) {
                        mNodeArray[arrayPosition - 1] = new TimePikcerEntity(NODE_UNACTIVE);
                    }

                    mNodeArray[arrayPosition] = new TimePikcerEntity();
                }
            }
        }

        //这里才是最终的导师接单时间
        for (int arrayPosition = 0; arrayPosition < mNodeList.size(); arrayPosition++) {
            TimePikcerEntity timePikcerEntity = mNodeArray[arrayPosition];
            if (timePikcerEntity == null) {

                String date = mHttpResult.date.get(arrayPosition / 48).date;
                String timestart = getTimestart(arrayPosition);
                int hours = 24;

                //在00：00允许选择到23：00  arrayPosition=0，允许选择最大47
                //所以innerArrayPosition < arrayPosition+48
                for (int innerArrayPosition = arrayPosition + 1; innerArrayPosition < arrayPosition + 48 && innerArrayPosition < mNodeList.size(); innerArrayPosition++) {
                    TimePikcerEntity innerTimePikcerEntity = mNodeArray[innerArrayPosition];
                    if (innerTimePikcerEntity != null && innerTimePikcerEntity.type == NODE_LIFELESS) {
                        //那么该innerTimePikcerEntity.type==NODE_UNACTIVE
                        //例子：选择第一天时02：00休息。选择01：00，hours=1。那么arrayPosition = 2,innerArrayPosition=4
                        hours = (innerArrayPosition - arrayPosition) / 2;
                        break;
                    }
                }
                mNodeArray[arrayPosition] = new TimePikcerEntity(NODE_ACTIVE, date, timestart, hours);
            }
        }
        mRb1.setChecked(true);
    }

    private String getTimestart(int arrayPosition) {
        int position = arrayPosition;
        if (arrayPosition >= 48 * 2) {
            position -= 48 * 2;
        } else if (arrayPosition >= 48) {
            position -= 48;
        }

        int haveMinture = position % 2;
        int hourNumber = position / 2;

        String hour = hourNumber >= 10 ? hourNumber + "" : "0" + hourNumber;
        String minture = haveMinture == 1 ? "30" : "00";
        return hour + ":" + minture;
    }

    /**
     * 是否是导师接单时间
     */
    private boolean isTeacherOnline(int arrayPosition) {
        int position = arrayPosition;
        if (arrayPosition >= 48 * 2) {
            position -= 48 * 2;
        } else if (arrayPosition >= 48) {
            position -= 48;
        }
        //00:00  24:00
        // 接单时间不太清楚
        if (position < mStartPosition || position >= mEndPosition) {
            return false;
        }
        return true;
    }


    @Override
    public void onPositionListener(int position) {
        //被选中的市场 position=1 表示为1个小时
        mTvTimeLength.setText(position == -1 ? "" : "(" + position + "小时)");
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!buttonView.isChecked()) {
            //如果是取消选中,就不需要操作其他的checkbox
            //重置时长
            initRulerView(-1);
            mTvTimeLength.setText("");

            mCheckedEntity = null;
        } else if (isChecked) {
            try {
                //当有一个选中时，清除其他所有的选中状态
                for (CheckBox mCb : mCbs) {
                    if (mCb != buttonView) {
                        //注意先去掉监听,改变后再添加
                        mCb.setOnCheckedChangeListener(null);
                        mCb.setChecked(false);
                        mCb.setOnCheckedChangeListener(this);
                    } else {
                        //如果是选中，需要清除其他的checkbox的选中状态
                        int position = mCbs.indexOf(buttonView);
                        TimePikcerEntity timePikcerEntity = mNodeArray[position + mDayChecked * 48];
                        initRulerView(timePikcerEntity.hours);
                        mTvTimeLength.setText("(1小时)");
                        LogUtil.e(timePikcerEntity.toString());

                        mCheckedEntity = timePikcerEntity;
                    }
                }
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        mDayChecked = 0;
        //下单日期的选择
        if (checkedId == mRb1.getId()) {
            mDayChecked = 0;
        } else if (checkedId == mRb2.getId()) {
            mDayChecked = 1;
        } else {
            mDayChecked = 2;
        }
        //重置时长
        initRulerView(-1);
        mTvTimeLength.setText("");

        //重置开始时间
        for (int position = 0; position < 48; position++) {
            int realPosition = position + mDayChecked * 48;
            TimePikcerEntity timePikcerEntity = mNodeArray[realPosition];

            CheckBox checkBox = mCbs.get(position);
            if (timePikcerEntity != null) {
                if (timePikcerEntity.type == NODE_LIFELESS) {
                    checkBox.setEnabled(false);
                    checkBox.setTextColor(0xffdddddd);
                    checkBox.setChecked(false);
                    continue;
                } else if (timePikcerEntity.type == NODE_UNACTIVE) {
                    checkBox.setTextColor(colorStateList);
                    checkBox.setEnabled(false);
                    checkBox.setChecked(false);
                    continue;
                }
            }
            checkBox.setTextColor(colorStateList);
            checkBox.setEnabled(true);
            checkBox.setChecked(false);
        }
    }

    public TextView getmTvReset() {
        return mTvReset;
    }

    public TextView getmTvConfirm() {
        return mTvConfirm;
    }

    public void resetTime() {
        mRgDay.clearCheck();
        mRgDay.check(mRb1.getId());
//        //重置时长
//        initRulerView(-1);
//        mTvTimeLength.setText("");
    }

    public TimePikcerEntity getCheckedTime() {
        return mCheckedEntity;
    }

    public int getCheckedTimeLength() {
        String str = mTvTimeLength.getText().toString();
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        return StringUtils.getInt(str);
    }

    public void setupCheckedData(TimePikcerEntity mCheckedTime, final int checkedTimeLength) {
        if (mCheckedTime == null) {
            return;
        }

        int arrayPosition = mNodeList.indexOf(mCheckedTime);
        int position = arrayPosition;
        if (arrayPosition > mNodeArrayLength / 3 * 2 - 1) {
            mRgDay.check(mRb3.getId());
            position = arrayPosition - 48 * 2;
        } else if (arrayPosition > mNodeArrayLength / 3 - 1) {
            mRgDay.check(mRb2.getId());
            position = arrayPosition - 48;
        } else {
            mRgDay.check(mRb1.getId());
        }
        if (position>=0) {
            CheckBox checkBox = mCbs.get(position);
            checkBox.setChecked(true);
        }
        mRulerView.post(new Runnable() {
            @Override
            public void run() {
                mTvTimeLength.setText("(" + checkedTimeLength + "小时)");
                mRulerView.smoothScrollTo(checkedTimeLength);
            }
        });
    }

}
