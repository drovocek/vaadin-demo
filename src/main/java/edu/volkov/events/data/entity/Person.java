package edu.volkov.events.data.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@ToString
@Entity
public class Person {

    @Id
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false; // null or other class
        }
        Person other = (Person) obj;

        if (id != null) {
            return id.equals(other.id);
        }
        return super.equals(other);
    }
}
