package com.loner.verifyview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.logging.Handler

//注册验证码
class VerifyProgressView constructor(context: Context,attributeSet: AttributeSet) : View(context,attributeSet){

    //主题色的笔
    private var paintTheme = Paint()
    //未滑动默认背景色的笔
    private var paintDefBg = Paint()
    //边框的笔1
    private var paintFrame = Paint()
    //白色的笔
    private var paintWhite = Paint()
    //字体的笔
    private var paintText = Paint()
    //箭头的笔
    private var paintArrow = Paint()

    //判断手指是否落下
    private var isClick = false

    //是否完成了滑动
    private var isComplete = false

    private var progress = 0

    private var max = 10000//总进度设置成10000

    init {
        //初始化画笔
        paintTheme = ViewUtils.getPaint(context,1f, R.color.themeColor)
        paintDefBg = ViewUtils.getPaint(context,1f, R.color.Color_F5F5F5)
        paintFrame = ViewUtils.getPaint(context,1f, R.color.colorDefLine)
        paintWhite = ViewUtils.getPaint(context,1f, R.color.white)
        paintText = ViewUtils.getTextPaint(context,12f, R.color.Color_8C8C8C)
        paintArrow =  ViewUtils.getPaint(context,2f,R.color.white)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (!isComplete) {
            drawBackground(canvas)
            drawProgressCircle(canvas)
        }else{
            drawComplete(canvas)
        }
    }

    private fun drawBackground(canvas: Canvas?){
        val backRect = RectF(0f, 0f, width.toFloat(), height.toFloat())// 设置个新的长方形
        canvas?.drawRoundRect(backRect, 180f, 180f, paintDefBg)//第二个参数是x半径，第三个参数是y半径
        paintFrame.style = Paint.Style.STROKE
        canvas?.drawRoundRect(backRect, 180f, 180f, paintFrame)//第二个参数是x半径，第三个参数是y半径
        //x = width /2 - (字体宽度 / 2）
        //y = height / 2 + (字体高度 / 2)
        paintText.color = context.resources.getColor(R.color.Color_8C8C8C)
        canvas?.drawText(
            "滑至最右 完成验证",
            (width / 2) - (ViewUtils.getFontWidth(paintText, "滑至最右 完成验证") / 2),
            (height / 2) + (ViewUtils.getFontHeight(paintText,"滑至最右 完成验证") / 2),
            paintText
        )
        paintFrame.style = Paint.Style.STROKE
        canvas?.drawCircle(height / 2f, height / 2f,height / 2f,paintFrame)
        drawArrow(canvas,height / 2f, height / 2f,R.color.colorDefLine)
    }

    private fun drawProgressCircle(canvas: Canvas?){
        if (isClick){
            val s = height / 2
            val x = (width -(s * 2)) * (progress.toFloat() / max.toFloat()) + s.toFloat()
            //滑动的背景矩形
            val backRect = RectF(0f, 0f, x.toFloat() + s, height.toFloat())// 设置个新的长方形
            canvas?.drawRoundRect(backRect,180f,180f, paintTheme)//第二个参数是x半径，第三个参数是y半径
            paintTheme.style = Paint.Style.FILL
            canvas?.drawCircle(x, height / 2f,height / 2f,paintTheme)
            drawArrow(canvas,x, height / 2f,R.color.white)
        }else{
            paintFrame.style = Paint.Style.STROKE
            canvas?.drawCircle(height / 2f, height / 2f,height / 2f,paintFrame)
            drawArrow(canvas,height / 2f, height / 2f,R.color.colorDefLine)
        }
    }

    private fun drawComplete(canvas: Canvas?){
        progress = max
        val backRect = RectF(0f, 0f, width.toFloat(), height.toFloat())// 设置个新的长方形
        canvas?.drawRoundRect(backRect,180f,180f, paintTheme)//第二个参数是x半径，第三个参数是y半径
        paintText.color = context.resources.getColor(R.color.white)
        canvas?.drawText(
            "验证成功",
            (width / 2) - (ViewUtils.getFontWidth(paintText, "验证成功") / 2),
            (height / 2) + (ViewUtils.getFontHeight(paintText,"验证成功") / 2),
            paintText
        )
    }

    /**
     * 传圆的中心位置
     */
    private fun drawArrow(canvas: Canvas?,cx:Float,cy:Float,color:Int){
        paintArrow.color = context.resources.getColor(color)
        paintArrow.style = Paint.Style.STROKE
        var path = Path()
        path.moveTo((cx - ViewUtils.dp2px(context,5f)),(cy - ViewUtils.dp2px(context,8f)))
        path.lineTo((cx + ViewUtils.dp2px(context,5f)),cy)
        path.lineTo((cx - ViewUtils.dp2px(context,5f)),(cy + ViewUtils.dp2px(context,8f)))
        canvas?.drawPath(path,paintArrow)
    }


    private var preX = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                //确认用户手指落在圆点位置
                if (event?.x!! <= height){
                    isClick = true
                    progress = 0
                    preX = event?.x
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (isClick){
                    var maxX = width -(height / 2 * 2)
                    var offsetX = event?.x!! - preX
                    progress += ((max * (offsetX / maxX)).toInt())
                    progress = if(progress < 0) 0 else progress
                    preX = event?.x
                    if (progress >= max){
                        progress = 0
                        isClick = false
                        isComplete = true
                        android.os.Handler().postDelayed({
                            completeListener?.invoke(this)
                        },1000)
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                progress = 0
                isClick = false
            }
        }
//        Log.i("Loner",event?.action.toString())
        invalidate()
        return true
    }


    /**
     * 滑动完成监听
     */
    open var completeListener : ((View) -> Unit)? = null


}