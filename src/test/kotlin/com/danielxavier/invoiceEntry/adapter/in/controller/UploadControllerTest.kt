package com.danielxavier.invoiceEntry.adapter.`in`.controller

import com.danielxavier.invoiceEntry.application.ports.`in`.UploadPort
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import kotlin.test.assertEquals

class UploadControllerTest {

    private lateinit var uploadPort: UploadPort
    private lateinit var uploadController: UploadController

    @BeforeEach
    fun setup() {
        uploadPort = mockk()
        uploadController = UploadController(uploadPort)
    }

    @Test
    fun `deve fazer o upload com sucesso`() {
        every { uploadPort.upload(any()) } returns ObjectResponse("123", "pdf", 233)

        val response: ResponseEntity<ObjectResponse> = uploadController.uploadInvoice(mockMultipartFile())

        assertEquals(response.statusCode, HttpStatus.OK)
        verify(exactly = 1) { uploadPort.upload(any()) }
    }

    @Test
    fun `deve fazer o upload com erro`() {
        every { uploadPort.upload(any()) } throws Exception()

        val response: ResponseEntity<ObjectResponse> = uploadController.uploadInvoice(mockMultipartFile())

        assertEquals(response.statusCode, HttpStatus.INTERNAL_SERVER_ERROR)
        verify(exactly = 1) { uploadPort.upload(any()) }
    }

    @Test
    fun `deve retornar erro 400 quando vier arquivo vazio`() {

        val response: ResponseEntity<ObjectResponse> = uploadController.uploadInvoice(mockMultipartFileEmpty())

        assertEquals(response.statusCode, HttpStatus.BAD_REQUEST)
        verify(exactly = 0) { uploadPort.upload(any()) }
    }

    private fun mockMultipartFile(): MultipartFile {
        val file = mockk<MultipartFile>()
        every { file.isEmpty } returns false
        every { file.originalFilename } returns "test.pdf"
        every { file.contentType } returns "application/pdf"
        every { file.inputStream } returns mockk()
        every { file.size } returns 1234L
        return file
    }

    private fun mockMultipartFileEmpty(): MultipartFile {
        val file = mockk<MultipartFile>()
        every { file.isEmpty } returns true
        return file
    }
}