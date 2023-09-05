package bcc.sipas.app.chat.repository;

import bcc.sipas.entity.ChatResponseUsage;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends R2dbcRepository<ChatResponseUsage, Long> {}
