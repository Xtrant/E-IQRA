package com.example.e_iqra.view.main.ui.quiz

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.data.Question
import com.example.e_iqra.data.QuestionType
import com.example.e_iqra.databinding.ActivityQuizBinding
import java.io.ByteArrayOutputStream

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private var currentQuestionIndex = 0

    private lateinit var multipleChoiceLayout: View
    private lateinit var drawingLayout: View

    private lateinit var questionText: TextView
    private lateinit var answerGroup: RadioGroup
    private lateinit var resultText: TextView

    private lateinit var drawingQuestionText: TextView
    private lateinit var drawingCanvas: ImageView
    private lateinit var drawingResultText: TextView

    private var totalCorrectAnswers = 0

    private val questionList = listOf(
        Question(
            questionText = "What is the capital of France?",
            options = arrayOf("Paris", "London", "Berlin", "Madrid"),
            correctAnswerIndex = 0,
            type = QuestionType.MULTIPLE_CHOICE
        ),
        Question(
            questionText = "Which one is the largest planet in our Solar System?",
            options = arrayOf("Earth", "Mars", "Jupiter", "Saturn"),
            correctAnswerIndex = 2,
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
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        multipleChoiceLayout = findViewById(R.id.multiple_choice_layout)
        drawingLayout = findViewById(R.id.drawing_layout)

        questionText = findViewById(R.id.question_text)
        answerGroup = findViewById(R.id.answer_group)
        resultText = findViewById(R.id.result_text)

        drawingQuestionText = findViewById(R.id.drawing_question_text)
        drawingCanvas = findViewById(R.id.drawing_canvas)
        drawingResultText = findViewById(R.id.drawing_result_text)

        showQuestion(currentQuestionIndex)

        findViewById<View>(R.id.submit_button).setOnClickListener {
            checkAnswer()
        }

        findViewById<View>(R.id.submit_drawing_button).setOnClickListener {
            checkDrawingAnswer()
        }

        val finishButton = findViewById<Button>(R.id.finish_button)
        finishButton.setOnClickListener {
            navigateToResultQuiz()
        }
    }

    private fun showQuestion(questionIndex: Int) {
        if (questionIndex >= questionList.size) {
            resultText.text = "Quiz finished!"
            drawingResultText.text = "Quiz finished!"
            return
        }

        val question = questionList[questionIndex]
        when (question.type) {
            QuestionType.MULTIPLE_CHOICE -> {
                multipleChoiceLayout.visibility = View.VISIBLE
                drawingLayout.visibility = View.GONE
                questionText.text = question.questionText
                answerGroup.clearCheck()
                question.options?.let {
                    (answerGroup.getChildAt(0) as RadioButton).text = it[0]
                    (answerGroup.getChildAt(1) as RadioButton).text = it[1]
                    (answerGroup.getChildAt(2) as RadioButton).text = it[2]
                    (answerGroup.getChildAt(3) as RadioButton).text = it[3]
                }
                findViewById<View>(R.id.submit_button).visibility = View.VISIBLE
                findViewById<View>(R.id.submit_drawing_button).visibility = View.GONE
            }
            QuestionType.DRAWING -> {
                multipleChoiceLayout.visibility = View.GONE
                drawingLayout.visibility = View.VISIBLE
                drawingQuestionText.text = question.questionText
                findViewById<View>(R.id.submit_button).visibility = View.GONE
                findViewById<View>(R.id.submit_drawing_button).visibility = View.VISIBLE
            }
        }

        // Cek jika ini adalah pertanyaan terakhir
        if (questionIndex == questionList.size - 1 && question.type == QuestionType.DRAWING) {
            findViewById<View>(R.id.submit_button).visibility = View.GONE
            findViewById<View>(R.id.submit_drawing_button).visibility = View.VISIBLE
        }
    }



    private fun checkAnswer() {
        val selectedId = answerGroup.checkedRadioButtonId
        if (selectedId == -1) {
            resultText.text = "Please select an answer"
            return
        }

        val selectedRadioButton = findViewById<RadioButton>(selectedId)
        val selectedIndex = answerGroup.indexOfChild(selectedRadioButton)
        val currentQuestion = questionList[currentQuestionIndex]

        if (selectedIndex == currentQuestion.correctAnswerIndex) {
            totalCorrectAnswers++
            resultText.text = "Correct!"
        } else {
            resultText.text = "Incorrect. The correct answer is ${currentQuestion.options?.get(currentQuestion.correctAnswerIndex!!)}"
        }

        currentQuestionIndex++
        if (currentQuestionIndex < questionList.size) {
            showQuestion(currentQuestionIndex)
        } else {
            resultText.text = "Quiz finished!"
        }
    }


    private fun checkDrawingAnswer() {
        // Ambil gambar dari ImageView (drawingCanvas)
        val bitmap = getBitmapFromView(drawingCanvas)

        // Konversi bitmap ke byte array
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        // Kirim byte array ke model ML (pseudo-code, ganti dengan implementasi nyata)
        val isDrawingCorrect = checkDrawingWithMLModel(byteArray)

        drawingResultText.text = if (isDrawingCorrect) {
            "Drawing is correct!"
        } else {
            "Drawing is incorrect."
        }

        // Periksa apakah ini adalah pertanyaan terakhir
        if (currentQuestionIndex == questionList.size - 1) {
            navigateToResultQuiz()
        } else {
            currentQuestionIndex++
            showQuestion(currentQuestionIndex)
        }
    }



    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun navigateToResultQuiz() {
        val intent = Intent(this, QuizResultActivity::class.java)
        intent.putExtra("TOTAL_CORRECT_ANSWERS", totalCorrectAnswers)
        intent.putExtra("TOTAL_QUESTIONS", questionList.size)
        startActivity(intent)
        finish()
    }

    private fun checkDrawingWithMLModel(byteArray: ByteArray): Boolean {
        // Pseudo-code: kirim byteArray ke model ML dan terima hasilnya
        // Ganti dengan implementasi nyata yang memanggil model ML
        return true // Asumsikan jawaban selalu benar untuk sekarang
    }

}
