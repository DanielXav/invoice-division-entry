package com.danielxavier.invoiceEntry.adapter.mapper

import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectRequest
import org.springframework.web.multipart.MultipartFile

object FileMapper {

    fun MultipartFile.toRequest(): ObjectRequest =
        ObjectRequest(
            type = this.contentType,
            content = this.inputStream,
            size = this.size
        )
}