package data.testDataGenerators;

import domain.WorkType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestWorkTypesGenerator implements TestDataGenerator<WorkType> {

    private final Random random = new Random(129867358486L);
    private final List<String> workTypes = Arrays.asList("Cleaning up", "Planning", "Bug fixing",
            "More bug fixing", "Development", "Prototyping", "Overtime development");

    @Override
    public WorkType createTestObject() {
        String name = selectRandom(workTypes);
        String description = "Executed remotely";
        return new WorkType(name, random.nextInt(20) * 10, description);
    }

    @Override
    public List<WorkType> createTestObjects(int count) {
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
