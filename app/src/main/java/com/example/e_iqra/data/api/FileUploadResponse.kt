package com.example.e_iqra.data.api

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(
    @field:SerializedName("confidence")
    val confidence : String,

    @field:SerializedName("result")
    val result : String
)
