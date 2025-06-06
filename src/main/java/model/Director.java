package model;

import java.time.LocalDate;

public class Director {
    private int id;
    private String name;
    private LocalDate birthDate;
    private Actor actor; // Если директор еще и актер

    public Director(int id, String name, LocalDate birthDate, Actor actor) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.actor = actor;
    }

    public Director() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "Director{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}
