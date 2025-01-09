package com.danielxavier.invoiceEntry.adapter.`in`.controller

import com.danielxavier.invoiceEntry.adapter.`in`.controller.mapper.FileMapper.toRequest
import com.danielxavier.invoiceEntry.application.ports.`in`.UploadPort
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/invoice")
class UploadController(
    private val uploadPort: UploadPort
) {

    @PostMapping("/upload")
    fun uploadInvoice(@RequestParam("file") file: MultipartFile): ResponseEntity<ObjectResponse> {

        file.takeIf { !file.isEmpty } ?: return ResponseEntity.badRequest().build()

        return runCatching {
            uploadPort.upload(file.toRequest())
        }.fold(
            onSuccess = { ResponseEntity.ok(it) },
            onFailure = { ResponseEntity.status(500).build() }
        )
    }
}