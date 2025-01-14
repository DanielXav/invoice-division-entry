package com.danielxavier.invoiceEntry.application.usecase

import com.danielxavier.invoiceEntry.application.ports.`in`.NotifyPort
import com.danielxavier.invoiceEntry.application.ports.out.NotifyProducer
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NotifyUseCase(
    private val notifyProducer: NotifyProducer
): NotifyPort {

    override fun sendNotification(response: ObjectResponse) {
        try {
            logger.info("Mensagem sendo enviada para a fila.")
            notifyProducer.sendMessage(response)
        } catch (ex: Exception) {
            logger.error("Erro ao enviar mensagem para a fila. [${ex.message}]")
            throw ex
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NotifyUseCase::class.java.name)
    }
}