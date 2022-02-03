package com.example.linussudoku.android.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.example.linussudoku.android.view.game.Cell

class SudokuBoardView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private var squareRootSize = 3
    private var size = 9

    //These are set in onDraw()
    private var cellSizePixels = 0F
    private var noteSizePixels = 0F

    private val sideMargin = 10F

    private var selectedRow = 0
    private var selectedColumn = 0

    var listener: OnTouchListener? = null

    private var cells: List<Cell>? = null

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

    private val textPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    private val startingCellTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
        typeface = Typeface.DEFAULT_BOLD
    }

    private val startingCellPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.parseColor("#acacac")
    }

    private val noteTextPaint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val sizePixels = kotlin.math.min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(sizePixels, sizePixels)
    }

    override fun onDraw(canvas: Canvas) {
        updateMeasurements(width)
        fillCells(canvas)
        drawLines(canvas)
        drawText(canvas)
    }

    private fun updateMeasurements(width: Int) {
        cellSizePixels = (width / size).toFloat()
        noteSizePixels = cellSizePixels / squareRootSize.toFloat()
        noteTextPaint.textSize = cellSizePixels / squareRootSize.toFloat()
        textPaint.textSize = cellSizePixels / 1.5F
        startingCellTextPaint.textSize = cellSizePixels / 1.5F
    }

    fun fillCells(canvas: Canvas) {


        cells?.forEach {
            val row = it.row
            val column = it.column

            if (it.isStartingCell) {
                fillCell(canvas, row, column, startingCellPaint)
            } else if (row == selectedRow && column == selectedColumn) {
                fillCell(canvas, row, column, selectedCellPaint)
            } else if (row == selectedRow || column == selectedColumn) {
                fillCell(canvas, row, column, conflictingCellPaint)
            } else if (row / squareRootSize == selectedRow / squareRootSize && column / squareRootSize == selectedColumn / squareRootSize) {
                fillCell(canvas, row, column, conflictingCellPaint)
            }
        }

    }

    private fun fillCell(canvas: Canvas, row: Int, column: Int, paint: Paint) {
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

    private fun drawText(canvas: Canvas) {
        cells?.forEach { cell ->
            val cellValue = cell.value
            val textBounds = Rect()

            if (cellValue == 0) {
                cell.notes.forEach { note ->
                    val rowAndCell = (note - 1) / squareRootSize
                    val columnAndCell = (note - 1) % squareRootSize
                    val valueString = note.toString()
                    noteTextPaint.getTextBounds(valueString, 0, valueString.length, textBounds)
                    val textWidth = noteTextPaint.measureText(valueString)
                    val textHeight = textBounds.height()

                    canvas.drawText(
                        valueString,
                        cell.column * cellSizePixels + columnAndCell * noteSizePixels + noteSizePixels / 2 - textWidth / 2f,
                        cell.row * cellSizePixels + rowAndCell * noteSizePixels + noteSizePixels / 2 + textHeight / 2f,
                        noteTextPaint
                    )
                }
                //drawNotes
            } else {
                val valueString = cell.value.toString()
                val row = cell.row
                val column = cell.column
                val paintToUse = if (cell.isStartingCell) startingCellTextPaint else textPaint

                paintToUse.getTextBounds(valueString, 0, valueString.length, textBounds)
                val textWidth = paintToUse.measureText(valueString)
                val textHeight = textBounds.height()

                canvas.drawText(
                    valueString,
                    (column * cellSizePixels) + cellSizePixels / 2 - textWidth / 2,
                    (row * cellSizePixels) + cellSizePixels / 2 + textHeight / 2,
                    paintToUse
                )
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
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
        val possibleSelectedRow = (y / cellSizePixels).toInt()
        val possibleSelectedColumn = (x / cellSizePixels).toInt()
        listener?.onCellTouched(possibleSelectedRow, possibleSelectedColumn)
    }

    fun updateSelectedCellUI(row: Int, column: Int) {
        selectedRow = row
        selectedColumn = column
        invalidate()
    }

    fun updateCells(cells: List<Cell>) {
        this.cells = cells
        invalidate()
    }

    fun registerListener(listener: OnTouchListener) {
        this.listener = listener
    }

    interface OnTouchListener {
        fun onCellTouched(row: Int, column: Int)
    }

}