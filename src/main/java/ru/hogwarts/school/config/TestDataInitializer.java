package ru.hogwarts.school.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Arrays;

@Configuration
public class TestDataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initData(
            StudentRepository studentRepository,
            FacultyRepository facultyRepository,
            AvatarRepository avatarRepository) {

        return args -> {
            if (facultyRepository.count() == 0) {
                Faculty gryffindor = new Faculty("Гриффиндор", "красный");
                Faculty slytherin = new Faculty("Слизерин", "зеленый");
                Faculty ravenclaw = new Faculty("Когтевран", "синий");
                Faculty hufflepuff = new Faculty("Пуффендуй", "желтый");

                facultyRepository.saveAll(Arrays.asList(gryffindor, slytherin, ravenclaw, hufflepuff));

                System.out.println("Факультеты созданы: " + facultyRepository.count());
            }

            if (studentRepository.count() == 0) {
                Faculty gryffindor = facultyRepository.findByNameIgnoreCase("Гриффиндор").orElse(null);
                Faculty slytherin = facultyRepository.findByNameIgnoreCase("Слизерин").orElse(null);
                Faculty ravenclaw = facultyRepository.findByNameIgnoreCase("Когтевран").orElse(null);
                Faculty hufflepuff = facultyRepository.findByNameIgnoreCase("Пуффендуй").orElse(null);

                Student harry = new Student(null, "Гарри Поттер", 17, "harry.potter@hogwarts.com");
                harry.setFaculty(gryffindor);

                Student hermione = new Student(null, "Гермиона Грейнджер", 18, "hermione.granger@hogwarts.com");
                hermione.setFaculty(gryffindor);

                Student ron = new Student(null, "Рон Уизли", 17, "ron.weasley@hogwarts.com");
                ron.setFaculty(gryffindor);

                Student draco = new Student(null, "Драко Малфой", 17, "draco.malfoy@hogwarts.com");
                draco.setFaculty(slytherin);

                Student luna = new Student(null, "Луна Лавгуд", 16, "luna.lovegood@hogwarts.com");
                luna.setFaculty(ravenclaw);

                Student cedric = new Student(null, "Седрик Диггори", 18, "cedric.diggory@hogwarts.com");
                cedric.setFaculty(hufflepuff);

                studentRepository.saveAll(Arrays.asList(
                        harry, hermione, ron, draco, luna, cedric
                ));

                System.out.println("Студенты созданы: " + studentRepository.count());
            }

            if (avatarRepository.count() == 0) {
                for (int i = 1; i <= 15; i++) {
                    Avatar avatar = new Avatar("/avatars/avatar" + i + ".jpg", 100000L + i * 1000, "image/jpeg");
                    avatarRepository.save(avatar);
                }
                System.out.println("Аватары созданы: " + avatarRepository.count());
            }

            System.out.println("Инициализация данных завершена!");
            System.out.println("Всего студентов: " + studentRepository.count());
            System.out.println("Всего факультетов: " + facultyRepository.count());
            System.out.println("Всего аватаров: " + avatarRepository.count());
        };
    }
}