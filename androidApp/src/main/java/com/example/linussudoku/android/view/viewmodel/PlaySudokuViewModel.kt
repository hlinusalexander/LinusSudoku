package com.example.linussudoku.android.view.viewmodel

import androidx.lifecycle.ViewModel
import com.example.linussudoku.android.view.game.SudokuGame

class PlaySudokuViewModel : ViewModel() {

    val sudokuGame = SudokuGame()
}