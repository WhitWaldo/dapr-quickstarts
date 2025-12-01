package com.service;

import java.util.List;

import io.dapr.client.DaprClientBuilder;
import io.dapr.client.DaprPreviewClient;
import io.dapr.client.domain.ConversationInputAlpha2;
import io.dapr.client.domain.ConversationMessageContent;
import io.dapr.client.domain.ConversationRequestAlpha2;
import io.dapr.client.domain.ConversationResponseAlpha2;
import io.dapr.client.domain.UserMessage;
import reactor.core.publisher.Mono;

public class Conversation {

    public static void main(String[] args) {
        String prompt = "What is Dapr?";

        try (DaprPreviewClient client = new DaprClientBuilder().buildPreviewClient()) {
            System.out.println("Input: " + prompt);

            // Create a user message with the prompt
            UserMessage userMessage = new UserMessage(List.of(new ConversationMessageContent(prompt)));
            ConversationInputAlpha2 conversationInput = new ConversationInputAlpha2(List.of(userMessage));

            // Component name is the name provided in the metadata block of the conversation.yaml file.
            Mono<ConversationResponseAlpha2> responseMono = client.converseAlpha2(
                    new ConversationRequestAlpha2("echo", List.of(conversationInput))
                            .setContextId("contextId")
                            .setScrubPii(true)
                            .setTemperature(1.1d));
            ConversationResponseAlpha2 response = responseMono.block();
            System.out.printf("Output response: %s",
                    response.getOutputs().get(0).getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
