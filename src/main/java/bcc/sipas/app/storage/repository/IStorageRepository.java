package bcc.sipas.app.storage.repository;

import bcc.sipas.entity.StorageResponse;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface IStorageRepository {
    Mono<StorageResponse> create(byte[] file);
    Mono<Void> destroy(String publicId);
}
