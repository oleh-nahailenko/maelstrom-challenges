package com.maelstrom.challenges.uniqueid;

import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maelstrom.challenges.common.Handler;

/**
 * UniqueIdHandler solves Challenge #2: Unique Id Generation
 */
public class UniqueIdHandler implements Handler {

    private final ObjectMapper mapper;
    private String nodeId;

    public UniqueIdHandler(ObjectMapper mapper) {
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
        case "generate" -> {
          String uniqueId = UUID.randomUUID().toString();

          ObjectNode generateReplyBody = mapper.createObjectNode();
          generateReplyBody.put("type", "generate_ok");
          generateReplyBody.put("id", uniqueId);
          generateReplyBody.set("msg_id", messageBody.get("msg_id"));
          generateReplyBody.set("in_reply_to", messageBody.get("msg_id"));

          yield generateReplyBody;
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