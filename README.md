# Custom Metrics Library

A **Custom Metrics Library** é uma biblioteca Java baseada em OpenTelemetry que permite criar métricas personalizadas para aplicações Spring Boot. Ela fornece uma interface simples para criar contadores (`Counter`), gauges (`Gauge`), histogramas (`Histogram`), e outros tipos de métricas, que podem ser usados para monitorar o comportamento da aplicação.

## Funcionalidades

- **Contadores (`Counter`)**: Métricas cumulativas que aumentam ao longo do tempo, como o número de requisições ou eventos.
- **Gauges (`Gauge`)**: Métricas que representam um valor em um momento específico, como o uso de memória ou o número de threads ativas.
- **Contadores Incrementais e Decrementais (`UpDownCounter`)**: Métricas que podem ser incrementadas ou decrementadas, como o número de conexões ativas.
- **Gauges Observáveis (`ObservableGauge`)**: Métricas observáveis que representam valores em tempo real, como o número de usuários ativos.
- **Histogramas (`Histogram`)**: Métricas que coletam distribuições de valores, como latências ou tamanhos de payload.
- **Gauges Observáveis de Ponto Flutuante (`ObservableDoubleGauge`)**: Métricas observáveis que utilizam valores de ponto flutuante, úteis para medições mais precisas.

## Requisitos

- Java 11 ou superior.
- Spring Boot.
- OpenTelemetry configurado na aplicação.

## Como Usar

### 1. Adicione a Dependência

Adicione a biblioteca ao seu projeto. Caso esteja usando Maven, inclua o seguinte no seu `pom.xml`:

```xml
<dependency>
    <groupId>o11y</groupId>
    <artifactId>lib-otel-brad</artifactId>
    <version>1.0.0</version>
</dependency>
```

### 2. Configure o OpenTelemetry na Aplicação

Certifique-se de que o OpenTelemetry está configurado na sua aplicação. Por exemplo, configure o exportador OTLP no arquivo `application.properties`:

```properties
otel.exporter.otlp.endpoint=http://localhost:4317
otel.resource.attributes=service.name=my-service
```

### 3. Use o `CustomMetricsRegistry`

Injete o `CustomMetricsRegistry` em seus componentes Spring e crie métricas personalizadas:

```java
import com.example.metrics.CustomMetricsRegistry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.ObservableLongGauge;
import io.opentelemetry.api.metrics.LongUpDownCounter;
import io.opentelemetry.api.metrics.DoubleHistogram;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final LongCounter requestCounter;
    private final ObservableLongGauge activeUsersGauge;
    private final LongUpDownCounter connectionCounter;
    private final DoubleHistogram responseTimeHistogram;

    public MetricsService(CustomMetricsRegistry metricsRegistry) {
        this.requestCounter = metricsRegistry.createCounter("http_requests_total", "Total de requisições HTTP");
        this.activeUsersGauge = metricsRegistry.createGauge("active_users", "Número de usuários ativos", this::getActiveUsers);
        this.connectionCounter = metricsRegistry.createUpDownCounter("active_connections", "Número de conexões ativas");
        this.responseTimeHistogram = metricsRegistry.createHistogram("http_response_time", "Tempo de resposta HTTP");
    }

    public void incrementRequestCounter() {
        requestCounter.add(1);
    }

    public void incrementConnectionCounter() {
        connectionCounter.add(1);
    }

    public void decrementConnectionCounter() {
        connectionCounter.add(-1);
    }

    public void recordResponseTime(double timeInMillis) {
        responseTimeHistogram.record(timeInMillis);
    }

    private long getActiveUsers() {
        // Retorna o número de usuários ativos (exemplo fictício)
        return 42;
    }
}
```

### 4. Visualize as Métricas

Certifique-se de que sua aplicação está exportando as métricas para um backend de observabilidade, como Prometheus, Grafana ou Jaeger. As métricas criadas estarão disponíveis com os nomes definidos (`http_requests_total`, `active_users`, `active_connections`, `http_response_time`, etc.).

## Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir issues ou enviar pull requests.

## Licença

Este projeto está licenciado sob a licença MIT.
