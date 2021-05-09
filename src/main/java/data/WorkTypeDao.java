package data;

import domain.WorkType;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class WorkTypeDao extends AbstractDao<WorkType> {

    public WorkTypeDao(DataSource dataSource) {
        super(dataSource, "WORK_TYPE");
    }

    @Override
    public void create(WorkType workType) {
        if (workType.getId() != null) {
            throw new IllegalArgumentException("WorkType already has ID: " + workType);
        }
        String insertStatement = "INSERT INTO WORK_TYPE (WORK_NAME, RATE, DESCRIPTION) VALUES (?, ?, ?)";
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(insertStatement, RETURN_GENERATED_KEYS)) {
            st.setString(1, workType.getName());
            st.setInt(2, workType.getRate());
            st.setString(3, workType.getDescription());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    workType.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for workType: " + workType);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store workType " + workType, ex);
        }
    }


    public void update(WorkType workType) {
        if (workType.getId() == null) {
            throw new IllegalArgumentException("WorkType has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE WORK_TYPE SET WORK_NAME = ?, RATE = ?, DESCRIPTION = ? WHERE ID = ?")) {
            st.setString(1, workType.getName());
            st.setInt(2, workType.getRate());
            st.setString(3, workType.getDescription());
            st.setLong(4, workType.getId());
            if (st.executeUpdate() != 1) {
                throw new DataAccessException("Failed to update non-existing workType: " + workType);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update workType " + workType, ex);
        }
    }

    public List<WorkType> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, WORK_NAME, RATE, DESCRIPTION FROM WORK_TYPE")) {

            List<WorkType> workTypes = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    WorkType workType = new WorkType(
                            rs.getString("WORK_NAME"),
                            rs.getInt("RATE"),
                            rs.getString("DESCRIPTION"));
                    workType.setId(rs.getLong("ID"));
                    workTypes.add(workType);
                }
            }
            return workTypes;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all workTypes", ex);
        }
    }

    public WorkType findOne(Long id) {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, WORK_NAME, RATE, DESCRIPTION FROM WORK_TYPE WHERE ID = ?")) {
            st.setLong(1, id);

            WorkType workType = new WorkType();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    workType.setId(rs.getLong("ID"));
                    workType.setName(rs.getString("WORK_NAME"));
                    workType.setRate(rs.getInt("RATE"));
                    workType.setDescription(rs.getString("DESCRIPTION"));
                }
            }
            return workType;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to get work type", ex);
        }
    }

    public int count() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT COUNT(ID) FROM WORK_TYPE")) {

            int count = 0;
            try (var rs = st.executeQuery()) {
                if (rs.next()){
                    count = rs.getInt(1);
                }
            }
            return count;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to count employees", ex);
        }
    }
}
