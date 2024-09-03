package personal.mrfired.imagetransfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import personal.mrfired.imagetransfer.entity.Image;
import personal.mrfired.imagetransfer.repository.ImageRepo;
import personal.mrfired.imagetransfer.service.storage.StorageService;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class DefaultImageService implements ImageService {
    @Autowired
    private ImageRepo imageRepo;

    @Autowired
    private StorageService storageService;

    @Override
    public void uploadImage(String username, MultipartFile imageFile) {
        String key = storageService.store(imageFile);
        imageRepo.save(new Image(key, username, Timestamp.from(Instant.now())));
    }

    @Override
    public Resource getImageAsResource(String name) {
        return storageService.loadAsResource(name);
    }

    @Override
    public Page<Image> getImages(int pageNumber, int pageSize) {
        return getSortedImages(pageNumber, pageSize, Sort.by("uploadTimestamp").descending());
    }

    @Override
    public Page<Image> getSortedImages(int pageNumber, int pageSize, Sort sort) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        return imageRepo.findAll(pageable);
    }

    @Override
    public void deleteImage(String name) {
        storageService.delete(name);
        imageRepo.deleteById(name);
    }
}
