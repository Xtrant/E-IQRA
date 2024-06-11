package com.example.e_iqra.data

data class Question(
    val questionText: String,
    val options: Array<String>? = null,
    val correctAnswerIndex: Int? = null,
    val type: QuestionType
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    DRAWING
}
