package com.example.e_iqra.view.main.ui.quiz

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityQuizBinding
import com.example.e_iqra.data.Question
import com.example.e_iqra.data.QuestionType
import java.io.ByteArrayOutputStream

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var currentQuestionIndex = 0
    private var totalCorrectAnswers = 0
    private var selectedOption: Button? = null
    private var isAnswered = false // Track if the current question is answered

    private val questionList = listOf(
        Question(
            questionText = "Huruf apakah ini",
            image = R.drawable.alif,
            options = arrayOf("Alif", "Dzal", "Syin", "Tha'"),
            correctAnswerIndex = 0,
            type = QuestionType.MULTIPLE_CHOICE
        ),
        Question(
            questionText = "Huruf apakah ini",
            image = R.drawable.jim,
            options = arrayOf("Lam Alif", "Ya", "Kha'", "Jim"),
            correctAnswerIndex = 3,
            type = QuestionType.MULTIPLE_CHOICE
        ),
        Question(
            questionText = "Who wrote 'To Kill a Mockingbird'?",
            options = arrayOf("Harper Lee", "Mark Twain", "Ernest Hemingway", "F. Scott Fitzgerald"),
            correctAnswerIndex = 0,
            type = QuestionType.MULTIPLE_CHOICE
        ),
        Question(
            questionText = "Draw a cat.",
            type = QuestionType.DRAWING
        ),
        Question(
            questionText = "Draw a house.",
            type = QuestionType.DRAWING
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Disable 'Next Question' button initially
        binding.nextButton.isEnabled = false

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        showQuestion(currentQuestionIndex)

        binding.finishButton.setOnClickListener {
            checkAnswer()
        }

        binding.submitDrawingButton.setOnClickListener {
            checkDrawingAnswer()
        }

        binding.nextButton.setOnClickListener {
            nextQuestion()
        }

        binding.optionOne.setOnClickListener { selectOption(binding.optionOne) }
        binding.optionTwo.setOnClickListener { selectOption(binding.optionTwo) }
        binding.optionThree.setOnClickListener { selectOption(binding.optionThree) }
        binding.optionFour.setOnClickListener { selectOption(binding.optionFour) }
    }

    private fun showQuestion(questionIndex: Int) {
        if (questionIndex >= questionList.size) {
            return
        }

        val question = questionList[questionIndex]
        when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                binding.multipleChoiceLayout.visibility = View.VISIBLE
                binding.drawingLayout.visibility = View.GONE
                binding.questionPrompt.text = question.questionText

                question.image?.let { imageResId ->
                    binding.questionImage.setImageResource(imageResId)
                    binding.questionImage.visibility = View.VISIBLE
                } ?: run {
                    binding.questionImage.visibility = View.GONE
                }

                binding.optionOne.text = question.options?.get(0) ?: ""
                binding.optionTwo.text = question.options?.get(1) ?: ""
                binding.optionThree.text = question.options?.get(2) ?: ""
                binding.optionFour.text = question.options?.get(3) ?: ""

                resetOptionColors()

                binding.finishButton.visibility = View.VISIBLE
                binding.submitDrawingButton.visibility = View.GONE
            }
            QuestionType.DRAWING -> {
                binding.multipleChoiceLayout.visibility = View.GONE
                binding.drawingLayout.visibility = View.VISIBLE
                binding.drawingQuestionText.text = question.questionText
                resetCanvas()
                binding.finishButton.visibility = View.GONE
                binding.submitDrawingButton.visibility = View.VISIBLE
            }
        }

        // Check if this is the last question
        binding.nextButton.visibility = if (questionIndex == questionList.size - 1) View.GONE else View.VISIBLE
        binding.finishButton.visibility = if (questionIndex == questionList.size - 1) View.VISIBLE else View.GONE

        // Reset isAnswered flag
        isAnswered = false
    }

    private fun selectOption(option: Button) {
        resetOptionColors() // Reset all options background to dark gray
        selectedOption = option
        selectedOption?.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_light))

        // Enable 'Next Question' button after selecting an option
        binding.nextButton.isEnabled = true
    }

    private fun resetOptionColors() {
        selectedOption = null
        binding.optionOne.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.optionTwo.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.optionThree.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
        binding.optionFour.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))
    }

    private fun checkAnswer() {
        if (selectedOption == null) {
            Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIndex = when (selectedOption) {
            binding.optionOne -> 0
            binding.optionTwo -> 1
            binding.optionThree -> 2
            binding.optionFour -> 3
            else -> -1
        }

        val currentQuestion = questionList[currentQuestionIndex]

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            totalCorrectAnswers++
        }

        // Reset selected option and disable 'Next Question' button again
        selectedOption = null
        binding.nextButton.isEnabled = false

        // Move to the next question
        nextQuestion()
    }

    private fun nextQuestion() {
        currentQuestionIndex++
        if (currentQuestionIndex < questionList.size) {
            showQuestion(currentQuestionIndex)
        } else {
            navigateToResultQuiz()
        }
    }

    private fun checkDrawingAnswer() {
        val bitmap = getBitmapFromView(binding.drawingCanvas)

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        val isDrawingCorrect = checkDrawingWithMLModel(byteArray)

        if (isDrawingCorrect) {
            totalCorrectAnswers++
        }

        binding.nextButton.performClick()
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun resetCanvas() {
        val drawingCanvas = binding.drawingCanvas
        val canvasWidth = drawingCanvas.width
        val canvasHeight = drawingCanvas.height

        if (canvasWidth <= 0 || canvasHeight <= 0) {
            drawingCanvas.post { resetCanvas() }
            return
        }

        val blankBitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(blankBitmap)
        canvas.drawColor(Color.WHITE)
        drawingCanvas.setBitmap(blankBitmap)
    }

    private fun navigateToResultQuiz() {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("TOTAL_CORRECT_ANSWERS", totalCorrectAnswers)
        intent.putExtra("TOTAL_QUESTIONS", questionList.size)
        startActivity(intent)
        finish()
    }

    private fun checkDrawingWithMLModel(byteArray: ByteArray): Boolean {
        // Pseudo-code: send byteArray to ML model and get the result
        // Replace with real implementation that calls the ML model
        return true // Assume the answer is always correct for now
    }
}
