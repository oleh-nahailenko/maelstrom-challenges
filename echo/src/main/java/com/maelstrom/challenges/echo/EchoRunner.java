package com.maelstrom.challenges.echo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maelstrom.challenges.common.Node;

/**
 * Assembles all dependencies and launches the node.
 */
public class EchoRunner {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
        EchoHandler echoHandler = new EchoHandler(mapper);
        Node node = new Node(echoHandler, mapper);
        node.run();
    }
}