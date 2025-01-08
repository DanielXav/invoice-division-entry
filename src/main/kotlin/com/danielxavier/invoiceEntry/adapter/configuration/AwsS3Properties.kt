package com.danielxavier.invoiceEntry.adapter.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "aws.s3")
@Component
data class AwsS3Properties(
    val allowedMineTypes: List<String> = emptyList()
)
