package com.maelstrom.challenges.uniqueid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maelstrom.challenges.common.Node;

/**
 * Assembles all dependencies and launches the node.
 */
public class UniqueIdRunner {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        UniqueIdHandler uniqueIdHandler = new UniqueIdHandler(mapper);
        Node node = new Node(uniqueIdHandler, mapper);
        node.run();
    }
}