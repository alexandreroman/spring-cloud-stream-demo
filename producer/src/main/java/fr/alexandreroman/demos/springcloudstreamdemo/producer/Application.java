/*
 * Copyright (c) 2019 Pivotal Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.alexandreroman.demos.springcloudstreamdemo.producer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

@Data
@RequiredArgsConstructor
class Person {
    private final String firstName;
    private final String lastName;
}

@EnableBinding(Source.class)
@RestController
@RequiredArgsConstructor
@Slf4j
class PersonController {
    private final Source source;

    @GetMapping("/")
    public String newPerson(@RequestParam("firstName") @NotEmpty String firstName,
                            @RequestParam("lastName") @NotEmpty String lastName) {
        final Person p = new Person(firstName, lastName);
        log.info("Publishing new person: {}", p);
        source.output().send(MessageBuilder.withPayload(p).build());

        return "Published new person: " + firstName + " " + lastName;
    }
}
