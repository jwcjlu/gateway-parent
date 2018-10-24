package com.reactor.onError;

import reactor.core.publisher.Flux;

public class DoOnError {
    public static void main(String[] args) {
        Flux<String> flux = Flux.just("0", "1", "2", "abc","3")
            .map(i -> Integer.parseInt(i) + "")
            .doOnError(e -> e.printStackTrace())
            .onErrorReturn("System exception");
        flux.log().subscribe(System.out::println);
    }
}
