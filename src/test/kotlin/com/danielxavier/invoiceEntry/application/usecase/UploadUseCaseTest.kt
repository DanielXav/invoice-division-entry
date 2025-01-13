package com.danielxavier.invoiceEntry.application.usecase

import com.danielxavier.invoiceEntry.application.ports.`in`.UploadPort
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectRequest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.InputStream

class UploadUseCaseTest {

    private lateinit var s3Client: S3Client
    private lateinit var notifyUseCase: NotifyUseCase
    private lateinit var uploadPort: UploadPort
    private val bucketName: String = "buckettest"
    private val fileSizeLimit: Long = 123123
    private val allowedMimeTypes: List<String> = listOf("png", "jpg", "jpeg")
    private lateinit var objectRequest: ObjectRequest

    @BeforeEach
    fun setUp() {
        s3Client = mockk()
        notifyUseCase = mockk(relaxed = true)
        uploadPort = UploadUseCase(s3Client, notifyUseCase, bucketName, fileSizeLimit, allowedMimeTypes)
        objectRequest = ObjectRequest(type = "jpg", content = mockk<InputStream>(relaxed = true), size = 1024L)
    }

    @Test
    fun `deve enviar salvar o arquivo no S3 com sucesso`() {

        every { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) } returns mockk()

        uploadPort.upload(objectRequest)

        verify(exactly = 1) { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) }
        verify(exactly = 1) { notifyUseCase.sendNotification(any()) }

    }

    @Test
    fun `deve retornar erro de tipo`() {

        every { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) } returns mockk()

        assertThrows<IllegalArgumentException> { uploadPort.upload(objectRequest.copy(type = "img")) }

        verify(exactly = 0) { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) }
        verify(exactly = 0) { notifyUseCase.sendNotification(any()) }

    }

    @Test
    fun `deve retornar erro de tamanho de arquivo`() {

        every { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) } returns mockk()

        assertThrows<IllegalArgumentException> { uploadPort.upload(objectRequest.copy(size = 200000)) }

        verify(exactly = 0) { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) }
        verify(exactly = 0) { notifyUseCase.sendNotification(any()) }

    }

    @Test
    fun `deve retornar uma exceção`() {
        every { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) } throws Exception()

        assertThrows<Exception> { uploadPort.upload(objectRequest) }

        verify(exactly = 1) { s3Client.putObject(ofType(PutObjectRequest::class), ofType(RequestBody::class)) }
        verify(exactly = 0) { notifyUseCase.sendNotification(any()) }
    }
}