package personal.mrfired.imagetransfer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import personal.mrfired.imagetransfer.entity.Image;

@Repository
public interface ImageRepo extends JpaRepository<Image, String> {
    @Override
    <S extends Image> S save(S image);

    Page<Image> findAll(Pageable pageable);
}
