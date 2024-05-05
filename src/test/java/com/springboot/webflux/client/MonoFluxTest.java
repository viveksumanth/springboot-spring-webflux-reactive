package com.springboot.webflux.client;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MonoFluxTest {

    @Test
    public void testMono() {
        Mono<?> monoString = Mono.just("Hello world!").log();

        monoString.subscribe (
                (response) -> System.out.println(response) ,
                (error) -> System.out.println(error)
        );
    }

    @Test
    public void testMonoError() {
        Mono<?> monoString = Mono.just(1)
                .then(Mono.error(new RuntimeException("testing the exception")))
                .log();

        monoString.subscribe (
                (response) -> System.out.println(response),
                (error) -> System.out.println(error)
        );
    }


    @Test
    public void testFlux() {
        Flux<Integer> fluxIntegers = Flux.just(1,2,3,4,5)
                .concatWithValues(6)
                .log();

        fluxIntegers.subscribe(
                response -> System.out.println(response),
                error -> System.out.println(error)
        );
    }

    @Test
    public void testFluxError() {
        Flux<Integer> fluxIntegers = Flux.just(1,2,3,4,5)
                .concatWith(Flux.error(new RuntimeException("testing flux error")))
                .concatWithValues(6)
                .log();

        fluxIntegers.subscribe(
                response -> System.out.println(response),
                error -> System.out.println(error)
        );
    }
}
