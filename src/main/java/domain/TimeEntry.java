package domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class TimeEntry implements Entity {
    private Long id;
    private Client client;
    private WorkType workType;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TimeEntry(WorkType workType, Client client, LocalDateTime startTime, LocalDateTime endTime) {
        this.workType = workType;
        this.startTime = startTime;
        this.endTime = endTime;
        this.client = client;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public WorkType getWorkType() {
        return workType;
    }

    public void setWorkType(WorkType workType) {
        this.workType = workType;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Duration getDuration() {
        return Duration.between(getStartTime(), getEndTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeEntry timeEntry = (TimeEntry) o;
        return Objects.equals(id, timeEntry.id) &&
                Objects.equals(workType, timeEntry.workType) &&
                Objects.equals(startTime, timeEntry.startTime) &&
                Objects.equals(endTime, timeEntry.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, workType, startTime, endTime);
    }
}
