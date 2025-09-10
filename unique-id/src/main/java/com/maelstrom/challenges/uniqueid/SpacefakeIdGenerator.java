package com.maelstrom.challenges.uniqueid;

import java.time.Instant;

/**
 * SpacefakeIdGenerator generates unique IDs using a simplified ID 
 * structure inspired by Snowflake algorithm.
 */
public class SpacefakeIdGenerator {

    private static final long EPOCH_SECONDS = 1704067200L;

    private static final int MAX_SEQUENCE = 1024;
    private static final short NODE_ID_BITS = 2;
    private static final short SEQUENCE_BITS = 10;

    private final int nodeId;

    private long previousSec = -1L;
    private int sequence = 0;

    public SpacefakeIdGenerator(int nodeId) {
        this.nodeId = nodeId;
    }

    public long nextId() {
        long currentSec = secondsSinceEpoch();
        if (currentSec < previousSec) {
            currentSec = waitUntil(previousSec);
        }

        if (currentSec == previousSec) {
            if (sequence == MAX_SEQUENCE) {
                currentSec = waitUntil(previousSec + 1);
                sequence = 0;
                previousSec = currentSec;
            } else {
                sequence++;
            }
        } else {
            sequence = 0;
            previousSec = currentSec;
        }
        
        return (currentSec << (NODE_ID_BITS + SEQUENCE_BITS)) |
               (nodeId << SEQUENCE_BITS) |
               sequence;
    }

    private long waitUntil(long targetSec) {
        long now = secondsSinceEpoch();
        while (now < targetSec) now = secondsSinceEpoch();
        return now;
    }

    private long secondsSinceEpoch() {
        return Instant.now().getEpochSecond() - EPOCH_SECONDS;
    }
}