package com.example.linussudoku.android.view.game.algorithms

import com.example.linussudoku.android.view.game.SudokuGame

class BoardSolver {

    fun solveBoardBruteForce(sudokuGame: SudokuGame) {

        val cells = sudokuGame.board.cells
        for (cellIndexToTest in 0..81) {
            val cellToTest = cells[cellIndexToTest]


        }
    }

    private fun getCellsOnSameRowAsIndex(cellIndex: Int) {
        val row = cellIndex / 9
    }

}