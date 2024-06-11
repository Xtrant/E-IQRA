package com.example.e_iqra.view.main.ui.quiz

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.e_iqra.R
import com.example.e_iqra.databinding.ActivityQuizResultBinding

class QuizResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizResultBinding
    private lateinit var textResultTitle: TextView
    private lateinit var textCorrectAnswers: TextView
    private lateinit var textWrongAnswers: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textResultTitle = findViewById(R.id.text_result_title)
        textCorrectAnswers = findViewById(R.id.text_correct_answers)
        textWrongAnswers = findViewById(R.id.text_wrong_answers)

        // Ambil data hasil quiz dari intent
        val totalCorrectAnswers = intent.getIntExtra("TOTAL_CORRECT_ANSWERS", 0)
        val totalQuestions = intent.getIntExtra("TOTAL_QUESTIONS", 0)
        val totalWrongAnswers = totalQuestions - totalCorrectAnswers

        // Tampilkan hasil quiz pada TextViews
        textCorrectAnswers.text = "Correct Answers: $totalCorrectAnswers"
        textWrongAnswers.text = "Wrong Answers: $totalWrongAnswers"
    }
}
