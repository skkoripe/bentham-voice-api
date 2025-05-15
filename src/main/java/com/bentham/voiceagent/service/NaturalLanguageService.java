package com.bentham.voiceagent.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for natural language understanding and generation.
 * This interface defines methods for processing text using AI models.
 */
public interface NaturalLanguageService {

    /**
     * Processes a user message and generates a response.
     *
     * @param message the user message
     * @return a CompletableFuture that will be completed with the generated response
     */
    CompletableFuture<String> processMessage(String message);

    /**
     * Processes a user message with conversation context and generates a response.
     *
     * @param message the user message
     * @param conversationId the ID of the conversation
     * @return a CompletableFuture that will be completed with the generated response
     */
    CompletableFuture<String> processMessage(String message, String conversationId);

    /**
     * Processes a user message with additional parameters and generates a response.
     *
     * @param message the user message
     * @param parameters additional parameters for the model
     * @return a CompletableFuture that will be completed with the generated response
     */
    CompletableFuture<String> processMessageWithParams(String message, Map<String, Object> parameters);

    /**
     * Creates a new conversation.
     *
     * @return the ID of the new conversation
     */
    String createConversation();

    /**
     * Ends a conversation and cleans up resources.
     *
     * @param conversationId the ID of the conversation to end
     */
    void endConversation(String conversationId);
}
