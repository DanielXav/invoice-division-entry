package com.danielxavier.invoiceEntry.adapter.out.queue

import com.danielxavier.invoiceEntry.application.ports.out.NotifyProducer
import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import software.amazon.awssdk.services.sqs.SqsClient
import software.amazon.awssdk.services.sqs.model.SendMessageRequest

@Component
class NotifyProducerImpl(
    private val sqsClient: SqsClient,
    private val objectMapper: ObjectMapper,
    @Value("\${events.queues.url}") private val urlQueue: String
): NotifyProducer {

    override fun sendMessage(response: ObjectResponse) {
        val message = objectMapper.writeValueAsString(response)
        try {
            sqsClient.sendMessage(
                SendMessageRequest.builder()
                    .queueUrl(urlQueue)
                    .messageBody(message)
                    .build()
            ).also {
                logger.info("Mensagem enviada com sucesso para a fila SQS")
            }
        } catch (ex: Exception) {
            logger.error("Erro ao enviar mensagem para a fila. [${ex.message}]")
            throw ex
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NotifyProducerImpl::class.java.name)
    }
}