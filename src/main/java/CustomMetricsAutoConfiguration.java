package src.main.java.com;

import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.MetricExporter;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomMetricsAutoConfiguration {

    @Value("${otel.exporter.otlp.endpoint:http://localhost:4317}")
    private String otlpEndpoint;

    @Bean
    public MeterProvider meterProvider() {
        MetricExporter metricExporter = OtlpGrpcMetricExporter.builder()
                .setEndpoint(otlpEndpoint)
                .build();
        return SdkMeterProvider.builder()
                .registerMetricReader(PeriodicMetricReader.builder(metricExporter).build())
                .build();
    }

    @Bean
    public CustomMetricsRegistry customMetricsRegistry(MeterProvider meterProvider) {
        return new CustomMetricsRegistry(meterProvider);
    }
}
