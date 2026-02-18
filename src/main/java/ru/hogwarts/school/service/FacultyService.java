package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
@Transactional
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
        logger.info("FacultyService initialized");
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty with name: {}", faculty.getName());
        logger.debug("Faculty details: {}", faculty);

        try {
            Faculty savedFaculty = facultyRepository.save(faculty);
            logger.info("Faculty created successfully with id: {}", savedFaculty.getId());
            return savedFaculty;
        } catch (Exception e) {
            logger.error("Error creating faculty: {}", e.getMessage());
            throw e;
        }
    }

    public Faculty findFaculty(Long id) {
        logger.info("Was invoked method for find faculty with id: {}", id);

        return facultyRepository.findById(id)
                .map(faculty -> {
                    logger.debug("Found faculty: {}", faculty);
                    return faculty;
                })
                .orElseGet(() -> {
                    logger.warn("Faculty with id {} not found", id);
                    return null;
                });
    }

    public Faculty updateFaculty(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty with id: {}", id);

        if (!facultyRepository.existsById(id)) {
            logger.error("Cannot update: faculty with id {} not found", id);
            return null;
        }

        try {
            faculty.setId(id);
            Faculty updatedFaculty = facultyRepository.save(faculty);
            logger.info("Faculty with id {} updated successfully", id);
            logger.debug("Updated faculty: {}", updatedFaculty);
            return updatedFaculty;
        } catch (Exception e) {
            logger.error("Error updating faculty with id {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public Faculty deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty with id: {}", id);
        logger.warn("Delete operation for faculty with id: {}", id);

        return facultyRepository.findById(id)
                .map(faculty -> {
                    facultyRepository.deleteById(id);
                    logger.info("Faculty with id {} deleted successfully", id);
                    return faculty;
                })
                .orElseGet(() -> {
                    logger.error("Cannot delete: faculty with id {} not found", id);
                    return null;
                });
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");

        Collection<Faculty> faculties = facultyRepository.findAll();
        logger.debug("Found {} faculties", faculties.size());
        return faculties;
    }

    public Collection<Faculty> findFacultiesByColor(String color) {
        logger.info("Was invoked method for find faculties by color: {}", color);

        Collection<Faculty> faculties = facultyRepository.findByColor(color);
        logger.debug("Found {} faculties with color {}", faculties.size(), color);
        return faculties;
    }

    public Collection<Faculty> findFacultiesByNameOrColor(String name, String color) {
        logger.info("Was invoked method for find faculties by name: {} or color: {}", name, color);

        Collection<Faculty> faculties = facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
        logger.debug("Found {} faculties", faculties.size());
        return faculties;
    }
}