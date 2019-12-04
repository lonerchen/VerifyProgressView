package com.loner.verifyview

import android.app.Activity
import android.content.Context
import android.graphics.Paint
import android.graphics.Rect
import android.view.View

class ViewUtils{

    companion object{

        /**
         * 获取画图的笔
         * @param width 传UI上的PT 内部自动转换
         * @param colorRes 传颜色资源
         */
        open fun getPaint(context: Context,width: Float,colorRes :Int) : Paint{
            val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)//抗锯齿
            mPaint.isAntiAlias = true//防抖动
            mPaint.strokeCap = Paint.Cap.ROUND
            mPaint.color = context.resources.getColor(colorRes)
            mPaint.strokeWidth = dp2px(context,width).toFloat()
            return  mPaint
        }

        /**
         * 获取画文字的笔
         * @param width 传UI上的PT 内部自动转换
         * @param colorRes 传颜色资源
         */
        open fun getTextPaint(context: Context,width: Float,colorRes :Int) : Paint{
            val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)//抗锯齿
            mPaint.isAntiAlias = true//防抖动
            mPaint.strokeCap = Paint.Cap.ROUND
            mPaint.color = context.resources.getColor(colorRes)
            mPaint.textSize = dp2px(context,width).toFloat()
            return  mPaint
        }

        /**
         * 获取文字宽度，用于测量居中位置
         */
        open fun getFontWidth(paint:Paint,text:String) : Float{
            return paint.measureText(text)
        }

        /**
         * 获取文字高度，用于测量居中位置
         */
        open fun getFontHeight(paint:Paint,str:String) : Float{
//            var fm = paint.fontMetrics
//            //文字基准线的下部距离-文字基准线的上部距离 = 文字高度
//            return fm.descent - fm.ascent
            val rect = Rect()
            paint.getTextBounds(str, 0, str.length, rect)
            val h = rect.height()
            return h.toFloat()
        }

        fun dp2px(context: Context,dp: Float): Int {
            val scale = context.getResources().getDisplayMetrics().density
            return (dp * scale + 0.5f).toInt()
        }

        fun layout2View(activity: Activity,layoutId:Int):View{
            val inflater = activity.layoutInflater
            val view = inflater.inflate(layoutId, null)
            return view
        }

    }
}