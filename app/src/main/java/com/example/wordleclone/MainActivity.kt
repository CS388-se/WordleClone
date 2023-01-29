package com.example.wordleclone

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting all the needed text fields
        val guessOne = findViewById<TextView>(R.id.guessOne)
        val guessOneCheck = findViewById<TextView>(R.id.guessOneCheck)
        val guessTwo = findViewById<TextView>(R.id.guessTwo)
        val guessTwoCheck = findViewById<TextView>(R.id.guessTwoCheck)
        val guessThree = findViewById<TextView>(R.id.guessThree)
        val guessThreeCheck = findViewById<TextView>(R.id.guessThreeCheck)

        // getting the input field and button and the random word to guess (wordle)
        val guessInput = findViewById<EditText>(R.id.guessInput)
        val guessButton = findViewById<Button>(R.id.guessButton)
        val resetButton = findViewById<Button>(R.id.resetButton)
        val wordle = findViewById<TextView>(R.id.wordle)

        // keeping count of the number of the guess
        var guessNum = 1

        // first thing I will get the random word and render that
        val fourLetterWord = FourLetterWord()
        wordle.text = fourLetterWord.getRandomFourLetterWord()

        // setting the on click event for the guess button
        guessButton.setOnClickListener {
            this.hideKeyboard()
            val guess = guessInput.text.toString().uppercase()
            val result = this.checkGuess(guess, wordle.text.toString())
            val colorCodedResult = this.colorCodeCheck(guess, result)

            if (guessInput.text.length == 4) {
                when (guessNum) {
                    1 -> {
                        guessOne.text = guess
                        guessOneCheck.setText(colorCodedResult, TextView.BufferType.SPANNABLE)
                    }
                    2 -> {
                        guessTwo.text = guess
                        guessTwoCheck.setText(colorCodedResult, TextView.BufferType.SPANNABLE)
                    }
                    3 -> {
                        guessThree.text = guess
                        guessThreeCheck.setText(colorCodedResult, TextView.BufferType.SPANNABLE)
                    }
                }

                if (result == "OOOO" || guessNum >= 3) {
                    wordle.visibility = View.VISIBLE
                    guessInput.isEnabled = false
                    guessInput.isClickable = false
                    guessButton.visibility = View.INVISIBLE
                    resetButton.visibility = View.VISIBLE

                    if (result == "OOOO") {
                        Toast.makeText(this, "Well done! Press 'Reset' to play again!", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "You were not able to guess the word :( Press 'Reset' to play again!", Toast.LENGTH_LONG).show()
                    }
                }

                guessInput.text.clear()
                guessNum++
            }
        }

        // setting the on click event for the reset button
        // and applying the logic for resetting the game
        resetButton.setOnClickListener {
            resetButton.visibility = View.INVISIBLE
            guessButton.visibility = View.VISIBLE
            wordle.visibility = View.INVISIBLE
            guessInput.isEnabled = true
            guessInput.isClickable = true

            wordle.text = fourLetterWord.getRandomFourLetterWord()

            guessOne.text = ""
            guessTwo.text = ""
            guessThree.text = ""

            guessOneCheck.text = ""
            guessTwoCheck.text = ""
            guessThreeCheck.text = ""
            guessNum = 1
        }

    }

    private fun colorCodeCheck(guess: String, checkResult: String): Spannable {
        val spannable: Spannable = SpannableString(guess)
        for (i in 0..3) {
            val color = if (checkResult[i] == 'O') {
                Color.GREEN
            } else if (checkResult[i] == '+') {
                Color.GRAY
            } else {
                Color.RED
            }
            spannable.setSpan(ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannable
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            // on below line we are creating a variable
            // for input manager and initializing it.
            val inputMethodManager =
                getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

            // on below line hiding our keyboard.
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Parameters / Fields:
     *   wordToGuess : String - the target word the user is trying to guess
     *   guess : String - what the user entered as their guess
     *
     * Returns a String of 'O', '+', and 'X', where:
     *   'O' represents the right letter in the right place
     *   '+' represents the right letter in the wrong place
     *   'X' represents a letter not in the target word
     */
    private fun checkGuess(guess: String, wordToGuess: String) : String {
        var result = ""
        for (i in 0..3) {
            result += if (guess[i] == wordToGuess[i]) {
                "O"
            } else if (guess[i] in wordToGuess) {
                "+"
            } else {
                "X"
            }
        }
        return result
    }
}