package ru.hogwarts.school.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);

    // Пагинация для аватаров - обязательный метод для задания
    Page<Avatar> findAll(Pageable pageable);

    Page<Avatar> findByMediaType(String mediaType, Pageable pageable);

    Page<Avatar> findByFileSizeGreaterThan(Long minSize, Pageable pageable);
}