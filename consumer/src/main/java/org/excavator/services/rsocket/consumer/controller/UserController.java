package org.excavator.services.rsocket.consumer.controller;

import org.excavator.services.rsocket.consumer.UserRequest;
import org.excavator.services.rsocket.consumer.UserResponse;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private RSocketRequester requester;

    public UserController(RSocketRequester requester) {
        this.requester = requester;
    }

    @GetMapping("/users/{id}")
    public Publisher<UserResponse> getUserInfoById(@PathVariable long id){
        return this.requester.route("/users/id")
                .data(new UserRequest(id))
                .retrieveMono(UserResponse.class);
    }

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE, value = "/users/stream/{id}")
    public Publisher<UserResponse> userStream(@PathVariable long id){
        return this.requester.route("/users/stream")
                .data(new UserRequest(id))
                .retrieveFlux(UserResponse.class);
    }

    @GetMapping("/users/error")
    public Publisher<UserResponse> error(){
        return this.requester.route("/users/error")
                .data(Mono.empty())
                .retrieveFlux(UserResponse.class);
    }
}
