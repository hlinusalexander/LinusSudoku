package com.example.linussudoku.android

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var squareRootSize = 3
    private var size = 9

    private var cellSizePixels = 0F

    private val sideMargin = 10F

    private var selectedRow = 0
    private var selectedColumn = 0

    private val thickLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 8F
    }

    private val thickLineInsidePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 8F
    }

    private val thinLinePaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.BLACK
        strokeWidth = 4F
    }

    private val selectedCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#6ead3a")
    }

    private val conflictingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#efedef")
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = widthMeasureSpec.coerceAtMost(heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        cellSizePixels = (width / size).toFloat()
        fillCells(canvas)
        drawLines(canvas)
    }

    private fun fillCells(canvas: Canvas) {

        if (selectedRow == -1 || selectedColumn == -1) {
            return
        }

        for (row in 0..size) {
            for (column in 0..size) {
                if (row == selectedRow && column == selectedColumn) {
                    fillCell(canvas, row, column, selectedCellPaint)
                } else if (row == selectedRow || column == selectedColumn) {
                    fillCell(canvas, row, column, conflictingCellPaint)
                } else if (row / squareRootSize == selectedRow / squareRootSize && column / squareRootSize == selectedColumn / squareRootSize) {
                    fillCell(canvas, row, column, conflictingCellPaint)
                }
            }
        }
    }

    private fun fillCell(canvas: Canvas, row: Int, column: Int, paint: Paint) {
        println("Filling now")
        canvas.drawRect(
            column * cellSizePixels,
            row * cellSizePixels,
            (column + 1) * cellSizePixels,
            (row + 1) * cellSizePixels,
            paint
        )
    }

    private fun drawLines(canvas: Canvas) {
        canvas.drawRect(
            sideMargin,
            1F,
            width.toFloat() - sideMargin,
            height.toFloat() - 1,
            thickLinePaint
        )

        for (i in 1 until size) {
            val paintToUse = when (i % squareRootSize) {
                0 -> thickLineInsidePaint
                else -> thinLinePaint
            }
            canvas.drawLine(
                i * cellSizePixels,
                0F,
                i * cellSizePixels,
                height.toFloat(),
                paintToUse
            )

            canvas.drawLine(
                0F + sideMargin,
                i * cellSizePixels,
                width.toFloat(),
                i * cellSizePixels,
                paintToUse
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                handleTouchEvent(event.x, event.y)
                true
            }
            else -> false
        }
    }

    private fun handleTouchEvent(x: Float, y: Float) {
        selectedRow = (y / cellSizePixels).toInt()
        selectedColumn = (x / cellSizePixels).toInt()
        invalidate()
    }

}