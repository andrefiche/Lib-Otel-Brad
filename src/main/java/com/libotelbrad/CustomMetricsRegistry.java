package com.libotelbrad;

import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterProvider;
import io.opentelemetry.api.metrics.ObservableLongGauge;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.DoubleHistogram;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import org.springframework.stereotype.Component;

@Component
public class CustomMetricsRegistry {

    private final Meter meter;

    public CustomMetricsRegistry(MeterProvider meterProvider) {
        this.meter = meterProvider.meterBuilder("custom-metrics-lib").build();
    }

    // Counter
    public LongCounter createCounter(String name, String description) {
        return meter.counterBuilder(name)
                .setDescription(description)
                .setUnit("1")
                .build();
    }

    // UpDownCounter
    public LongUpDownCounter createUpDownCounter(String name, String description) {
        return meter.upDownCounterBuilder(name)
                .setDescription(description)
                .setUnit("1")
                .build();
    }

    // Gauge
    public ObservableLongGauge createGauge(String name, String description, Runnable callback) {
        return meter.gaugeBuilder(name)
                .setDescription(description)
                .setUnit("1")
                .ofLongs()
                .buildWithCallback(measurement -> callback.run());
    }

    // Histogram
    public DoubleHistogram createHistogram(String name, String description) {
        return meter.histogramBuilder(name)
                .setDescription(description)
                .setUnit("ms") // Exemplo: unidade de tempo em milissegundos
                .build();
    }

    // Observable Gauge (Double)
    public ObservableDoubleGauge createObservableGauge(String name, String description, Runnable callback) {
        return meter.gaugeBuilder(name)
                .setDescription(description)
                .setUnit("1")
                .buildWithCallback(measurement -> {
                    callback.run();
                });
    }
}