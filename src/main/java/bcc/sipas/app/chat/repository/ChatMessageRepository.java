package bcc.sipas.app.chat.repository;

import bcc.sipas.entity.ChatMessage;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends R2dbcRepository<ChatMessage, Long> {}
