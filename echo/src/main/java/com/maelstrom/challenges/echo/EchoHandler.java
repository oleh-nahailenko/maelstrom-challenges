package com.maelstrom.challenges.echo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maelstrom.challenges.common.Handler;

/**
 * EchoHandler solves Challenge #1: Echo
 */
public class EchoHandler implements Handler {

    private final ObjectMapper mapper;
    private String nodeId;

    public EchoHandler(ObjectMapper mapper) {
      this.mapper = mapper;
    }

    @Override
    public JsonNode handle(JsonNode message) {
      JsonNode messageBody = message.get("body");
      String type = messageBody.get("type").asText();

      ObjectNode replyBody = switch (type) {
        case "init" -> {
          nodeId = messageBody.get("node_id").asText();

          ObjectNode initReplyBody = mapper.createObjectNode();
          initReplyBody.put("type", "init_ok");
          initReplyBody.set("in_reply_to", messageBody.get("msg_id"));
          yield initReplyBody;
        }
        case "echo" -> {
          ObjectNode echoReplyBody = mapper.createObjectNode();
          echoReplyBody.put("type", "echo_ok");
          echoReplyBody.set("msg_id", messageBody.get("msg_id"));
          echoReplyBody.set("in_reply_to", messageBody.get("msg_id"));
          echoReplyBody.set("echo", messageBody.get("echo"));
          yield echoReplyBody;
        }
        default -> throw new IllegalArgumentException("Unsupported type: " + type);
      };
      
      ObjectNode replyMessage = mapper.createObjectNode();
      replyMessage.put("src", nodeId);
      replyMessage.set("dest", message.get("src"));
      replyMessage.set("body", replyBody);
      
      return replyMessage;
    }

}