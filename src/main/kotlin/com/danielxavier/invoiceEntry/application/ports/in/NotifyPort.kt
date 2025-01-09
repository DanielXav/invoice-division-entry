package com.danielxavier.invoiceEntry.application.ports.`in`

import com.danielxavier.invoiceEntry.application.usecase.dto.ObjectResponse

interface NotifyPort {

    fun sendNotification(response: ObjectResponse)
}