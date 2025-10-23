package com.maelstrom.challenges.uniqueid;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maelstrom.challenges.common.Handler;

/**
 * UniqueIdHandler solves Challenge #2: Unique Id Generation
 */
public class UniqueIdHandler implements Handler {

    private final ObjectMapper mapper;

    private SpacefakeIdGenerator idGenerator;
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

          int nodeNumber = Integer.parseInt(nodeId.substring(1));
          idGenerator = new SpacefakeIdGenerator(nodeNumber);

          ObjectNode initReplyBody = mapper.createObjectNode();
          initReplyBody.put("type", "init_ok");
          initReplyBody.set("in_reply_to", messageBody.get("msg_id"));
          yield initReplyBody;
        }
        case "generate" -> {
          long uniqueId = idGenerator.nextId();

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