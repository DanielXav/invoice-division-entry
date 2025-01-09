package com.danielxavier.invoiceEntry.application.ports.out

import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse

interface NotifyProducer {

    fun sendMessage(response: ObjectResponse)
}