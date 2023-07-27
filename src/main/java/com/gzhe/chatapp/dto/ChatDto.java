package com.gzhe.chatapp.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    private Integer id;
    private String chat_name;
    private String chat_image;

    @Column(columnDefinition = "boolean default false")
    private Boolean is_group;

    private Set<UserDto> admins = new HashSet<>();
    private UserDto created_by;
    private Set<UserDto> users = new HashSet<>();
    private List<MessageDto> messages = new ArrayList<>();
}
