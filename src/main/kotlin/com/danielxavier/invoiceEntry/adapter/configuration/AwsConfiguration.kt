package com.danielxavier.invoiceEntry.adapter.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class AwsConfiguration {

    @Bean
    fun s3Client(): S3Client {
        return S3Client.create()
    }
}