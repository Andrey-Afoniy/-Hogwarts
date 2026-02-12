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
            Faculty gryffindor = new Faculty("Гриффиндор", "красный");
            Faculty slytherin = new Faculty("Слизерин", "зеленый");
            Faculty ravenclaw = new Faculty("Когтевран", "синий");
            Faculty hufflepuff = new Faculty("Пуффендуй", "желтый");

            facultyRepository.saveAll(Arrays.asList(gryffindor, slytherin, ravenclaw, hufflepuff));

            // Создаем студентов (используем конструктор с 4 параметрами)
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

            Student fred = new Student(null, "Фред Уизли", 18, "fred.weasley@hogwarts.com");
            fred.setFaculty(gryffindor);

            Student george = new Student(null, "Джордж Уизли", 18, "george.weasley@hogwarts.com");
            george.setFaculty(gryffindor);

            studentRepository.saveAll(Arrays.asList(
                    harry, hermione, ron, draco, luna, cedric, neville, ginny, fred, george
            ));

            Avatar harryAvatar = new Avatar("/avatars/harry.jpg", 102400L, "image/jpeg");
            harryAvatar.setStudent(harry);

            Avatar hermioneAvatar = new Avatar("/avatars/hermione.jpg", 98304L, "image/jpeg");
            hermioneAvatar.setStudent(hermione);

            Avatar ronAvatar = new Avatar("/avatars/ron.jpg", 105000L, "image/jpeg");
            ronAvatar.setStudent(ron);

            Avatar dracoAvatar = new Avatar("/avatars/draco.jpg", 95000L, "image/jpeg");
            dracoAvatar.setStudent(draco);

            Avatar lunaAvatar = new Avatar("/avatars/luna.jpg", 89000L, "image/jpeg");
            lunaAvatar.setStudent(luna);

            avatarRepository.saveAll(Arrays.asList(
                    harryAvatar, hermioneAvatar, ronAvatar, dracoAvatar, lunaAvatar
            ));

            for (int i = 1; i <= 10; i++) {
                Avatar avatar = new Avatar("/avatars/avatar" + i + ".jpg", 100000L + i * 1000, "image/jpeg");
                avatarRepository.save(avatar);
            }

            System.out.println("Тестовые данные инициализированы:");
            System.out.println("Студентов: " + studentRepository.count());
            System.out.println("Факультетов: " + facultyRepository.count());
            System.out.println("Аватаров: " + avatarRepository.count());
        };
    }
}