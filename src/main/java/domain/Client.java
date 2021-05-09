package domain;

import java.util.Objects;

public class Client implements Entity {
    private Long id;
    private String firstName;
    private String lastName;

    public Client(String firstName, String surname) {
        this.firstName = firstName;
        this.lastName = surname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String surname) {
        this.lastName = surname;
    }


    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID=" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().equals(this.getClass()))) return false;
        Client client = (Client) o;
        return id.equals(client.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
