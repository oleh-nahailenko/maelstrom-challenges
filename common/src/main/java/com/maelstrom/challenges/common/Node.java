package com.maelstrom.challenges.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Node instance reads incoming messages from STDIN and writes messages to STDOUT in an infinite loop.
 * Processing of an incoming message is delegated to a handler.
 */
public class Node {
    
    private final ObjectMapper jsonMapper;
    private final Handler handler;

    public Node(Handler handler, ObjectMapper objectMapper) {
        this.handler = handler;
        this.jsonMapper = objectMapper;
    }

    /**
     * Launches this node, reads STDIN, and writes to STDOUT.
     */
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out))) {
            while (true) {
                JsonNode request = readJson(br);
                JsonNode response = handler.handle(request);
                writeJson(bw, response);
            }
        } catch (Exception e) {
            System.err.println("[node]: An error occured: ".concat(e.getMessage()));
            throw new RuntimeException(e);
        }
    }

    private JsonNode readJson(BufferedReader br) throws IOException {
        String inputLine;
        while ((inputLine = br.readLine()) != null) {
            if (!inputLine.isBlank()) return jsonMapper.readTree(inputLine);
        }

        throw new IOException("Reached EOF for stdin\n");
    }

    private void writeJson(BufferedWriter bw, JsonNode message) throws IOException {
        String outputLine = jsonMapper.writeValueAsString(message);
        bw.write(outputLine);
        bw.newLine();
        bw.flush();
    }

}