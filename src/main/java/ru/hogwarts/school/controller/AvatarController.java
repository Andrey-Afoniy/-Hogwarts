package ru.hogwarts.school.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping
    public ResponseEntity<Avatar> createAvatar(@RequestBody Avatar avatar) {
        Avatar createdAvatar = avatarService.saveAvatar(avatar);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAvatar);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Avatar> getAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        if (avatar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(avatar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.findAvatar(id);
        if (avatar == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        avatarService.deleteAvatar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @RequestParam int page,
            @RequestParam int size) {
        Page<Avatar> avatars = avatarService.getAllAvatars(page, size);
        return ResponseEntity.ok(avatars);
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getTotalAvatarsCount() {
        Long count = avatarService.getTotalAvatarsCount();
        return ResponseEntity.ok(count);
    }
}