package com.yuong.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private List<List<View>> linesViews = new ArrayList<>();
    private List<Integer> linesHeights = new ArrayList<>();
    private int lineSpace;
    private int columnSpace;

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLineSpace(int lineSpace) {
        this.lineSpace = lineSpace;
    }

    public void setColumnSpace(int columnSpace) {
        this.columnSpace = columnSpace;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;
        int childCount = getChildCount();
        int lineWidth = 0;
        int lineHeight = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams mp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + mp.leftMargin + mp.rightMargin;
            int childHeight = child.getMeasuredHeight() + mp.topMargin + mp.bottomMargin;
            int parentWidth = widthSize - getPaddingLeft() - getPaddingRight();
            //需要换行
            if (lineWidth + childWidth > parentWidth || lineWidth + childWidth + columnSpace > parentWidth) {
                lineWidth = childWidth;
                //防止第一个view就已经占满一行，这样无端多计算了个lineSpace的高度
                if (lineHeight > 0) {
                    height = height + lineHeight + lineSpace;
                } else {
                    height = height + lineHeight;
                }
                lineHeight = childHeight;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth = lineWidth + childWidth + columnSpace;

            }
            if (i == childCount - 1) {
                height += lineHeight;
            }
        }
        setMeasuredDimension(widthSize, height + getPaddingTop() + getPaddingBottom());

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        linesHeights.clear();
        linesViews.clear();
        int lineWidth = 0;
        int lineHeight = 0;
        List<View> lineViews = new ArrayList<>();
        int parentWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            //如果为GONE 则也要记录这个坑位到list，但是不去计算它占的坑
            if (child.getVisibility() == GONE) {
                lineViews.add(child);
                continue;
            }
            MarginLayoutParams mp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + mp.leftMargin + mp.rightMargin;
            int childHeight = child.getMeasuredHeight() + mp.topMargin + mp.bottomMargin;
            if (lineWidth + childWidth > parentWidth) {
                //防止第一个view就已经大于一行，这样其实lineHeight、和lineViews是没数据的，不能添加
                if (lineViews.size() > 0) {
                    linesHeights.add(lineHeight);
                    linesViews.add(lineViews);
                }
                lineViews = new ArrayList<>();
                lineHeight = 0;
                lineWidth = 0;
            }

            lineWidth = lineWidth + childWidth + columnSpace;
            lineHeight = Math.max(lineHeight, childHeight);
            lineViews.add(child);

            if (i == childCount - 1) {
                linesHeights.add(lineHeight);
                linesViews.add(lineViews);
            }
        }
        //开始高度要从paddingTop开始
        int currentHeight = getPaddingTop();
        for (int i = 0; i < linesViews.size(); i++) {
            //开始宽度要从paddingTop开始
            lineWidth = getPaddingLeft();
            for (View child : linesViews.get(i)) {
                if (child.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams mp = (MarginLayoutParams) child.getLayoutParams();
                child.layout(lineWidth + mp.leftMargin, currentHeight + mp.topMargin, lineWidth + mp.leftMargin + child.getMeasuredWidth(), currentHeight + mp.topMargin + child.getMeasuredHeight());
                //得到摆放好的child的right坐标，再加上rightMargin就是下一个View开始计算的left位置
                lineWidth = child.getRight() + mp.rightMargin + columnSpace;
            }
            //每一行的View摆放好了，就取出这一行的高度，再加上行间距，这样就可以确定下一行要开始摆放的top了
            currentHeight += linesHeights.get(i) + lineSpace;
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

}

