package org.excavator.services.rsocket.producer.controller;

import org.excavator.services.rsocket.producer.domain.UserRequest;
import org.excavator.services.rsocket.producer.domain.UserResponse;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * @author cmonkey
 */
@Controller
public class UserRSocketController {

    @MessageExceptionHandler
    Flux<UserResponse> errorHandler(IllegalArgumentException iia){
        return Flux.just(new UserResponse(0, "error"));
    }

    @MessageMapping("/users/error")
    public Flux<UserResponse> error(){
        return Flux.error(new IllegalArgumentException());
    }

    @MessageMapping("/users/id")
    public UserResponse getUserInfoById(UserRequest userRequest){
        return new UserResponse(userRequest.getId(), "name-" + Instant.now());
    }

    @MessageMapping("/users/stream")
    public Flux<UserResponse> userStream(UserRequest userRequest){
        return Flux.fromStream(Stream.generate(() -> new UserResponse(userRequest.getId(), "name-" + Instant.now()))).delayElements(Duration.ofSeconds(1));
    }
}
