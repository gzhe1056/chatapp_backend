package com.gzhe.chatapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private String content;
    private Integer id;
    private LocalDateTime timeStamp;
    private Boolean is_read;
    private UserDto user;
    private ChatDto chat;
}
