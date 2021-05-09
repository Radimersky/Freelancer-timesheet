package data.testDataGenerators;

import domain.Client;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClientsGenerator implements TestDataGenerator<Client> {

    private static final List<String> FIRST_NAMES = List.of("Petr", "Pavel", "Tomáš", "Jan", "Lukáš", "Patrik",
            "Jiří", "Karel", "Albert", "František");

    private static final List<String> LAST_NAMES = List.of("Novák", "Novotný", "Adámek", "Černý", "Hrdina",
            "Šťastný", "Žužlík", "Kvasnička", "Vopička", "Řezníček");

    private final Random random = new Random(129867358486L);

    @Override
    public Client createTestObject() {
        String firstName = selectRandom(FIRST_NAMES);
        String lastName = selectRandom(LAST_NAMES);
        return new Client(firstName, lastName);
    }

    @Override
    public List<Client> createTestObjects(int count) {
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
