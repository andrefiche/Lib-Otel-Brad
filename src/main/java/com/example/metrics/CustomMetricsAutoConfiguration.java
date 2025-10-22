package com.example.metrics;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMetricsAutoConfiguration {

    @Bean
    public MeterProvider meterProvider() {
        MetricExporter metricExporter = OtlpGrpcMetricExporter.getDefault();
        return SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
                .build();
    }

    @Bean
    public CustomMetricsRegistry customMetricsRegistry(MeterProvider meterProvider) {
        return new CustomMetricsRegistry(meterProvider);
    }
}
