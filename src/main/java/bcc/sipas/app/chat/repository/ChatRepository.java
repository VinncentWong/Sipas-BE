package bcc.sipas.app.chat.repository;

import bcc.sipas.entity.ChatResponseUsage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends R2dbcRepository<ChatResponseUsage, Long> {}
