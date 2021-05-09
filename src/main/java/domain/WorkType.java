package domain;

import java.util.Objects;

public class WorkType implements Entity {
    private Long id;
    private String name;
    private int rate;
    private String description;

    public WorkType() {
    }

    public WorkType(String name, int rate, String description) {
        this.name = name;
        this.rate = rate;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (ID=" + id + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass().equals(this.getClass()))) return false;
        WorkType workType = (WorkType) o;
        return id.equals(workType.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
