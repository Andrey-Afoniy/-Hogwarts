package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;

@Service
@Transactional
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final AvatarRepository avatarRepository;

    @Autowired
    public AvatarService(AvatarRepository avatarRepository) {
        this.avatarRepository = avatarRepository;
        logger.info("AvatarService initialized");
    }

    public Avatar saveAvatar(Avatar avatar) {
        logger.info("Was invoked method for save avatar");
        logger.debug("Avatar details: {}", avatar);

        try {
            Avatar savedAvatar = avatarRepository.save(avatar);
            logger.info("Avatar saved successfully with id: {}", savedAvatar.getId());
            return savedAvatar;
        } catch (Exception e) {
            logger.error("Error saving avatar: {}", e.getMessage());
            throw e;
        }
    }

    public Avatar findAvatar(Long id) {
        logger.info("Was invoked method for find avatar with id: {}", id);

        return avatarRepository.findById(id)
                .map(avatar -> {
                    logger.debug("Found avatar: {}", avatar);
                    return avatar;
                })
                .orElseGet(() -> {
                    logger.warn("Avatar with id {} not found", id);
                    return null;
                });
    }

    public void deleteAvatar(Long id) {
        logger.info("Was invoked method for delete avatar with id: {}", id);
        logger.warn("Delete operation for avatar with id: {}", id);

        if (!avatarRepository.existsById(id)) {
            logger.error("Cannot delete: avatar with id {} not found", id);
            return;
        }

        try {
            avatarRepository.deleteById(id);
            logger.info("Avatar with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error("Error deleting avatar with id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars with pagination - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarRepository.findAll(pageable);
        logger.debug("Found {} avatars on page {}", avatars.getNumberOfElements(), page);
        logger.debug("Total avatars: {}, total pages: {}", avatars.getTotalElements(), avatars.getTotalPages());
        return avatars;
    }

    public Long getTotalAvatarsCount() {
        logger.info("Was invoked method for get total avatars count");

        Long count = avatarRepository.count();
        logger.info("Total avatars count: {}", count);
        return count;
    }
}