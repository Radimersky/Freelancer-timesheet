package data;

import domain.Client;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public final class ClientDao extends AbstractDao<Client> {

    public ClientDao(DataSource dataSource) {
        super(dataSource, "CLIENT");
    }

    @Override
    public void create(Client client) {
        if (client.getId() != null) {
            throw new IllegalArgumentException("Client already has ID: " + client);
        }
        String insertStatement = "INSERT INTO CLIENT (FIRST_NAME, LAST_NAME) VALUES (?, ?)";
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(insertStatement, RETURN_GENERATED_KEYS)) {
            st.setString(1, client.getFirstName());
            st.setString(2, client.getLastName());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getLong(1));
                } else {
                    throw new DataAccessException("Failed to fetch generated key: no key returned for client: " + client);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store client " + client, ex);
        }
    }

    public void update(Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("Client has null ID");
        }
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement(
                     "UPDATE CLIENT SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?")) {
            st.setString(1, client.getFirstName());
            st.setString(2, client.getLastName());
            st.setLong(3, client.getId());
            if (st.executeUpdate() != 1) {
                throw new DataAccessException("Failed to update non-existing client: " + client);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update client " + client, ex);
        }
    }

    public List<Client> findAll() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT ID, FIRST_NAME, LAST_NAME FROM CLIENT")) {

            List<Client> clients = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Client client = new Client(
                            rs.getString("FIRST_NAME"),
                            rs.getString("LAST_NAME"));
                    client.setId(rs.getLong("ID"));
                    clients.add(client);
                }
            }
            return clients;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all employees", ex);
        }
    }

    public int count() {
        try (var connection = dataSource.getConnection();
             var st = connection.prepareStatement("SELECT COUNT(ID) FROM CLIENT")) {

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
