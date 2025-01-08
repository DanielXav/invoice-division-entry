package com.danielxavier.invoiceEntry.application.ports.`in`

import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectRequest
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse

interface UploadPort {

    fun upload(file: ObjectRequest): ObjectResponse

}