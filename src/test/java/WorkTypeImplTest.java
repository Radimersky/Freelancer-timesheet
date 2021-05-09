
import data.WorkTypeDao;
import domain.WorkType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;


import repository.Derby;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class WorkTypeImplTest {
    private WorkTypeDao workTypeDao;

    @BeforeEach
    public void setUpBeforeEach() {
        var derby = new Derby();
        derby.clearAll();
        workTypeDao = derby.getWorkTypeDao();
    }

    @Test
    public void addWorkType() {
        assertThat(workTypeDao.findAll().size()).isEqualTo(0);

        WorkType wt = new WorkType("Development",50,"Some description");
        WorkType wt2 = new WorkType("Bug fixing",52,"Some other description");

        workTypeDao.create(wt);
        assertThat(workTypeDao.findAll().size()).isEqualTo(1);

        workTypeDao.create(wt2);
        List<WorkType> workTypes = workTypeDao.findAll();

        assertThat(workTypes.size()).isEqualTo(2);
        assertThat(workTypes).containsOnlyOnce(wt);
        assertThat(workTypes).containsOnlyOnce(wt2);
    }

    @Test
    public void deleteWorkType() {
        assertThat(workTypeDao.findAll().size()).isEqualTo(0);

        WorkType wt = new WorkType("Development",50,"Some description");

        workTypeDao.create(wt);
        assertThat(workTypeDao.findAll().size()).isEqualTo(1);

        workTypeDao.delete(wt);
        assertThat(workTypeDao.findAll().size()).isEqualTo(0);
    }

    @Test
    public void updateWorkType() {
        assertThat(workTypeDao.findAll().size()).isEqualTo(0);

        WorkType wt = new WorkType("Development",50,"Some description");

        workTypeDao.create(wt);
        List<WorkType> workTypes = workTypeDao.findAll();
        assertThat(workTypes.size()).isEqualTo(1);

        WorkType dbWorkType = workTypes.get(0);
        dbWorkType.setDescription("New description");
        dbWorkType.setRate(40);
        dbWorkType.setName("New name");
        workTypeDao.update(dbWorkType);

        workTypes = workTypeDao.findAll();
        assertThat(workTypes.size()).isEqualTo(1);
        dbWorkType = workTypes.get(0);
        assertThat(dbWorkType.getName()).isEqualTo("New name");
        assertThat(dbWorkType.getDescription()).isEqualTo("New description");
        assertThat(dbWorkType.getRate()).isEqualTo(40);
    }
}