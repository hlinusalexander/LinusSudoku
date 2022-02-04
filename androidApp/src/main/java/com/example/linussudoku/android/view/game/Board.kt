package com.example.linussudoku.android.view.game

class Board(val size: Int, val cells: List<List<Cell>>) {

    fun getCell(row: Int, column: Int) = cells[row][column]
}