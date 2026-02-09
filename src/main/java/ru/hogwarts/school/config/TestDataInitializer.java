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
            System.out.println("Инициализация тестовых данных...");


            Faculty gryffindor = new Faculty(null, "Гриффиндор", "красный");
            Faculty slytherin = new Faculty(null, "Слизерин", "зеленый");
            Faculty ravenclaw = new Faculty(null, "Когтевран", "синий");
            Faculty hufflepuff = new Faculty(null, "Пуффендуй", "желтый");

            facultyRepository.saveAll(Arrays.asList(gryffindor, slytherin, ravenclaw, hufflepuff));


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

            Student neville = new Student(null, "Невилл Долгопупс", 17, "neville.longbottom@hogwarts.com");
            neville.setFaculty(gryffindor);

            Student ginny = new Student(null, "Джинни Уизли", 16, "ginny.weasley@hogwarts.com");
            ginny.setFaculty(gryffindor);

            studentRepository.saveAll(Arrays.asList(harry, hermione, ron, draco, luna, cedric, neville, ginny));


            Avatar harryAvatar = new Avatar(null, "/avatars/harry.jpg", 102400L, "image/jpeg");
            Avatar hermioneAvatar = new Avatar(null, "/avatars/hermione.jpg", 98304L, "image/jpeg");
            Avatar ronAvatar = new Avatar(null, "/avatars/ron.jpg", 105000L, "image/jpeg");
            Avatar dracoAvatar = new Avatar(null, "/avatars/draco.jpg", 95000L, "image/jpeg");
            Avatar lunaAvatar = new Avatar(null, "/avatars/luna.jpg", 89000L, "image/jpeg");
            Avatar cedricAvatar = new Avatar(null, "/avatars/cedric.jpg", 92000L, "image/jpeg");
            Avatar nevilleAvatar = new Avatar(null, "/avatars/neville.jpg", 87000L, "image/jpeg");
            Avatar ginnyAvatar = new Avatar(null, "/avatars/ginny.jpg", 91000L, "image/jpeg");

            avatarRepository.saveAll(Arrays.asList(
                    harryAvatar, hermioneAvatar, ronAvatar, dracoAvatar,
                    lunaAvatar, cedricAvatar, nevilleAvatar, ginnyAvatar
            ));

            System.out.println("Тестовые данные успешно инициализированы!");
            System.out.println("Создано студентов: " + studentRepository.count());
            System.out.println("Создано факультетов: " + facultyRepository.count());
            System.out.println("Создано аватаров: " + avatarRepository.count());
        };
    }
}