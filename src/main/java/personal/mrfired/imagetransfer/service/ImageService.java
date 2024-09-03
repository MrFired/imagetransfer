package personal.mrfired.imagetransfer.service;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import personal.mrfired.imagetransfer.entity.Image;

@Service
public interface ImageService {
    void uploadImage(String username, MultipartFile imageFile);

    Resource getImageAsResource(String name);

    Page<Image> getImages(int pageNumber, int pageSize);

    Page<Image> getSortedImages(int pageNumber, int pageSize, Sort sort);

    void deleteImage(String name);
}
