package com.danielxavier.invoiceEntry.application.usecase.dto

import java.io.InputStream

data class ObjectRequest(
    val type: String?,
    val content: InputStream,
    val size: Long
)
