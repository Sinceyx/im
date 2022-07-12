package com.winter.autumn.message;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class GroupChatResponseMessage extends AbstractResponseMessage {
    private String from;
    private String groupName;
    private String content;

    public GroupChatResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    public GroupChatResponseMessage(String from, String content, String groupName) {
        this.from = from;
        this.content = content;
        this.groupName = groupName;
    }
    @Override
    public int getMessageType() {
        return GROUP_CHAT_RESPONSE_MESSAGE;
    }
}
