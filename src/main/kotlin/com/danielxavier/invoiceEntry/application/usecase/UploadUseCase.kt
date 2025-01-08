package com.danielxavier.invoiceEntry.application.usecase

import com.danielxavier.invoiceEntry.application.ports.`in`.UploadPort
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectRequest
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*

@Component
class UploadUseCase(
    private val s3Client: S3Client,
    @Value("\${aws.s3.bucketName}")
    private val bucketName: String,
    @Value("\${aws.s3.fileSizeLimit}")
    private val fileSizeLimit: Long,
    @Value("\${aws.s3.allowedMimeTypes}")
    private val allowedMimeTypes: List<String>
) : UploadPort {

    override fun upload(file: ObjectRequest): ObjectResponse {

        try {
            logger.info("Iniciando o upload do arquivo.")

            require(file.size in 1..fileSizeLimit) { "Tamanho do arquivo inválido: ${file.size} bytes." }
            require(file.type in allowedMimeTypes) { "Tipo de arquivo inválido: ${file.type}" }

            val key = UUID.randomUUID().toString()

            logger.info("Key gerada: [$key] e arquivo inserido do tipo: [${file.type}].")

            s3Client.putObject(
                PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.type)
                    .build(),
                RequestBody.fromInputStream(file.content, file.size)
            )

            return ObjectResponse(key, file.type, file.size / 1024)
        } catch (ex: Exception) {
            logger.error("Erro durante o upload: ${ex.message}")
            throw ex
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(UploadUseCase::class.java.name)
    }
}


