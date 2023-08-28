package bcc.sipas.app.storage.repository;

import bcc.sipas.entity.StorageResponse;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class StorageRepository implements IStorageRepository{

    @Autowired
    private Cloudinary cloudinary;

    private StorageResponse convertIntoStorageResponse(Map<?, ?> map){
        return StorageResponse
                .builder()
                .publicId((String)map.get("public_id"))
                .secureUrl((String)map.get("secure_url"))
                .signature((String)map.get("signature"))
                .build();
    }

    @Override
    public Mono<StorageResponse> create(String file) {
        return Mono.fromFuture(
                CompletableFuture
                .supplyAsync(() -> {
                    try {
                        var res = this.cloudinary
                                .uploader()
                                .upload(file, ObjectUtils.emptyMap());
                        return this.convertIntoStorageResponse(res);
                    } catch (IOException e) {
                        throw new RuntimeException("terjadi kesalahan pada saat menyimpan ke storage");
                    }
                })
        );
    }

    @Override
    public Mono<Void> destroy(String publicId) {
        return null;
    }
}
