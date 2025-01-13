package com.danielxavier.invoiceEntry.application.usecase

import com.danielxavier.invoiceEntry.application.ports.`in`.NotifyPort
import com.danielxavier.invoiceEntry.application.ports.out.NotifyProducer
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows

class NotifyUseCaseTest {

    private lateinit var notifyProducer: NotifyProducer
    private lateinit var notifyPort: NotifyPort

    @BeforeEach
    fun setUp() {
        notifyProducer = mockk()
        notifyPort = NotifyUseCase(notifyProducer)
    }

    @Test
    fun `deve enviar a notificação com sucesso`() {
        every { notifyProducer.sendMessage(any()) } returns Unit

        val response = ObjectResponse("123", "pdf", 2312)

        assertDoesNotThrow {
            notifyPort.sendNotification(response)
        }

        verify(exactly = 1) { notifyProducer.sendMessage(any()) }
    }

    @Test
    fun `deve retornar erro ao tentar enviar mensagem para a fila`() {
        every { notifyProducer.sendMessage(any()) } throws Exception("Falha ao enviar mensagem")

        val response = ObjectResponse("123", "pdf", 123)

        val exception = assertThrows<Exception> {
            notifyPort.sendNotification(response)
        }
        assertEquals("Falha ao enviar mensagem", exception.message)
    }
}