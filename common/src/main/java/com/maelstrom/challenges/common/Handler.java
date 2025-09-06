package com.maelstrom.challenges.common;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * A handler for processing a single JSON message.
 */
public interface Handler {

    /**
     * Processes the given JSON message and returns a JSON response message.
     */
    JsonNode handle(JsonNode message);
}
