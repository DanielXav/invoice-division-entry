package com.danielxavier.invoiceEntry.adapter.out.queue

import com.danielxavier.invoiceEntry.application.ports.out.NotifyProducer
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest
import software.amazon.awssdk.services.sqs.model.SqsException
import kotlin.test.assertEquals

class NotifyProducerImplTest {

    private lateinit var sqsClient: SqsClient
    private lateinit var objectMapper: ObjectMapper
    private lateinit var notifyProducer: NotifyProducer
    private val urlQueue: String = "urlteste"

    @BeforeEach
    fun setUp() {
        sqsClient = mockk()
        objectMapper = mockk()
        notifyProducer = NotifyProducerImpl(sqsClient, objectMapper, urlQueue)
    }

    @Test
    fun `deve enviar uma mensagem com sucesso`() {
        every { objectMapper.writeValueAsString(any()) } returns "{}"
        every { sqsClient.sendMessage(ofType(SendMessageRequest::class)) } returns mockk()

        val response = ObjectResponse("123", "pdf", 123.55)

        notifyProducer.sendMessage(response)

        verify(exactly = 1) { sqsClient.sendMessage(ofType(SendMessageRequest::class)) }
        verify(exactly = 1) { objectMapper.writeValueAsString(any()) }
    }

    @Test
    fun `deve retornar uma exceção`() {
        every { objectMapper.writeValueAsString(any()) } returns "{}"
        every { sqsClient.sendMessage(ofType(SendMessageRequest::class)) } throws SqsException.builder()
            .message("Erro ao enviar mensagem").build()

        val response = ObjectResponse("123", "pdf", 123.55)

        val exception = assertThrows<SqsException> {
            notifyProducer.sendMessage(response)
        }

        assertEquals("Erro ao enviar mensagem", exception.message)

        verify(exactly = 1) { sqsClient.sendMessage(ofType(SendMessageRequest::class)) }
        verify(exactly = 1) { objectMapper.writeValueAsString(any()) }
    }
}