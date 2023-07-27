package com.gzhe.chatapp.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendMessageRequest {
    private Integer chatId;
    private Integer userId;
    private String content;
}
