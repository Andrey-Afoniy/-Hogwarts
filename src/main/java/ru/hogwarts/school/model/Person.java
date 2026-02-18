package ru.hogwarts.school.model;

import javax.persistence.*;

@Entity
@Table(name = "people")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "age", nullable = false)
    private int age;

    @Column(name = "has_license", nullable = false)
    private boolean hasLicense;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    public Person() {
    }

    public Person(Long id, String name, int age, boolean hasLicense) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.hasLicense = hasLicense;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isHasLicense() {
        return hasLicense;
    }

    public void setHasLicense(boolean hasLicense) {
        this.hasLicense = hasLicense;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", hasLicense=" + hasLicense +
                ", car=" + (car != null ? car.getBrand() + " " + car.getModel() : "нет") +
                '}';
    }
}