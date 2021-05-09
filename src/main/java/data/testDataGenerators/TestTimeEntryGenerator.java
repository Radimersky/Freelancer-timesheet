package data.testDataGenerators;

import domain.Client;
import domain.TimeEntry;
import domain.WorkType;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestTimeEntryGenerator implements TestDataGenerator<TimeEntry> {

    private final Random random = new Random(129867358486L);
    private final List<WorkType> workTypes;
    private final List<Client> clients;

    public TestTimeEntryGenerator(List<Client> clients, List<WorkType> workTypes) {
        this.clients = clients;
        this.workTypes = workTypes;
    }

    public LocalDate randomDate() {
        long minDay = LocalDate.of(2015, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2020, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    @Override
    public TimeEntry createTestObject() {
        WorkType workType = selectRandom(Objects.requireNonNull(workTypes));
        Client client = selectRandom(Objects.requireNonNull(clients));

        int startHalfHour = random.nextInt(47);
        int duration = random.nextInt(47 - startHalfHour) + 1;

        LocalDateTime startDateTime = randomDate().atTime(startHalfHour / 2, (startHalfHour % 2) * 30);
        LocalDateTime endDateTime = startDateTime.plusMinutes(duration * 30);

        assert Duration.between(endDateTime, startDateTime).isNegative();
        return new TimeEntry(workType, client, startDateTime, endDateTime);
    }

    @Override
    public List<TimeEntry> createTestObjects(int count) {
        return Stream
                .generate(this::createTestObject)
                .limit(count)
                .collect(Collectors.toList());
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }
}
