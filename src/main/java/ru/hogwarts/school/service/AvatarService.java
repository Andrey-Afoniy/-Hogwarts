package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
@Transactional
public class AvatarService {

    private final AvatarRepository avatarRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
    }

    public Avatar saveAvatar(Avatar avatar) {
        return avatarRepository.save(avatar);
    }

    public Avatar findAvatar(Long id) {
        return avatarRepository.findById(id).orElse(null);
    }

    public Avatar findAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId).orElse(null);
    }

    public void deleteAvatar(Long id) {
        avatarRepository.deleteById(id);
    }


    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }

    public Page<Avatar> getAllAvatarsSorted(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return avatarRepository.findAll(pageable);
    }

    public Page<Avatar> getAvatarsByMediaType(String mediaType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findByMediaType(mediaType, pageable);
    }

    public Page<Avatar> getAvatarsLargerThan(Long minSize, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findByFileSizeGreaterThan(minSize, pageable);
    }

    public Long getTotalAvatarsCount() {
        return avatarRepository.count();
    }
}