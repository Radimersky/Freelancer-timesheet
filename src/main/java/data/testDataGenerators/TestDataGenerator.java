package data.testDataGenerators;

import java.util.List;

public interface TestDataGenerator<T> {

    public T createTestObject();

    public List<T> createTestObjects(int count);
}
