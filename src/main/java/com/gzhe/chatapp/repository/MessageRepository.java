package com.gzhe.chatapp.repository;

import com.gzhe.chatapp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Query("select m from Message m join m.chat c where c.id= :chatId")
    List<Message> findMessageByChatId(@Param("chatId") Integer chatId);
}
