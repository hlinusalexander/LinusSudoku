package com.example.linussudoku.android.view.game.algorithms

import com.example.linussudoku.android.view.game.SudokuGame

class BoardSolver {

    fun solveBoardBruteForce(sudokuGame: SudokuGame) {

        val cells = sudokuGame.board.cells
        for (cellIndexToTest in 0..81) {
            val cellToTest = cells[cellIndexToTest]

            if (cellToTest.value == 0) {
                val row = cellIndexToTest / 9
                val col = cellIndexToTest % 9

                for (valueToTest in 1..9) {

                }
            } else {
                //The value was not 0, so we do not go down and test different values of this cell.
            }
        }
    }

    private fun getCellsOnSameRowAsIndex(cellIndex: Int) {
        val row = cellIndex / 9
    }

}