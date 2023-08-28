package bcc.sipas.app.storage.repository;

import bcc.sipas.entity.StorageResponse;
import reactor.core.publisher.Mono;

public interface IStorageRepository {
    Mono<StorageResponse> create(String file);
    Mono<Void> destroy(String publicId);
}
