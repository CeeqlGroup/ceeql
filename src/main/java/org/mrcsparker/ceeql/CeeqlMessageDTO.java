package org.mrcsparker.ceeql;

public class CeeqlMessageDTO {
    private String messageType;
    private String messageSubType;
    private long timestamp;
    private String message;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageSubType() {
        return messageSubType;
    }

    public void setMessageSubType(String messageSubType) {
        this.messageSubType = messageSubType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
