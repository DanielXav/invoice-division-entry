package com.danielxavier.invoiceEntry.adapter.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.sqs.SqsClient

@Configuration
class AwsConfiguration {

    @Bean
    fun s3Client(): S3Client {
        return S3Client.create()
    }

    @Bean
    fun sqsClient(): SqsClient {
        return SqsClient.create()
    }
}