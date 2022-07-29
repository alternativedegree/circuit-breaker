package com.helloworld.circuitbreaker;

import com.helloworld.ServiceRuntimeException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * This class creates an instance of a circuit breaker. This class uses `resilience4j` CircuitBreaker
 * For more details refer to https://resilience4j.readme.io/docs/circuitbreaker
 */
@Component
public class CircuitBreakerFactory {
    private final CircuitBreaker circuitBreaker;

    // If 50%(failureRateThreshold) of the 10(slidingWindowSize) requests fail(match the recordedExceptions) then the
    // circuit remains open for 5 seconds(waitDurationInOpenState).
    // In open state all the requests through the circuit breaker would fail with CallNotPermitted exception.
    // After 5 seconds, circuit goes into half-open state.
    // During half-open state 2 requests(permittedNumberOfCallsInHalfOpenState) will be permitted to flow through.
    // If the requests succeed(failure rate is below threshold), the circuit will be closed again.
    // If the failure rate is above threshold in half open state, the circuit will be open again.
    public CircuitBreakerFactory() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(5000))
                .permittedNumberOfCallsInHalfOpenState(2)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)
                .recordExceptions(ServiceRuntimeException.class) // All recorded exceptions are considered as a failure
                .build();

        CircuitBreakerRegistry circuitBreakerRegistry =
                CircuitBreakerRegistry.of(circuitBreakerConfig);

        circuitBreaker = circuitBreakerRegistry
                .circuitBreaker("test");
    }

    public CircuitBreaker getCircuitBreaker() {
        return circuitBreaker;
    }
}
