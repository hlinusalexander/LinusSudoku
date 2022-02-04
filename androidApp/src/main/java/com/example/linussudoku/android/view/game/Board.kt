package com.example.linussudoku.android.view.game

class Board(val size: Int, val cells: List<List<Cell>>) {

    fun getCell(row: Int, column: Int) = cells[row][column]
    fun areAllCellsFilled(): Boolean {
        cells.forEach { cellRow ->
            cellRow.forEach { cell ->
                if (cell.value == 0) {
                    return false
                }
            }
        }
        return true
    }

}