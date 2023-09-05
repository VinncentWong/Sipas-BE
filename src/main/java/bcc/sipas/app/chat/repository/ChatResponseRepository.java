package bcc.sipas.app.chat.repository;

import bcc.sipas.entity.ChatResponse;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatResponseRepository extends R2dbcRepository<ChatResponse, Long>{}
