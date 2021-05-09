package data;

import java.util.Collection;

/**
 * Generic interface for CRUD operations on entities
 *
 * @param <T> type of the entity this DAO operates on
 */
public interface DataAccessObject<T> {

    /**
     * Creates a new entity using the underlying data source
     *
     * @param entity entity to be persisted
     * @throws IllegalArgumentException when the entity has already been persisted
     * @throws DataAccessException      when anything goes wrong with the underlying data source
     */
    void create(T entity);

    /**
     * Reads all entities from the underlying data source
     *
     * @return collection of all entities known to the underlying data source
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    Collection<T> findAll();

    /**
     * Updates an entity using the underlying data source
     *
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException      when anything goes wrong with the underlying data source
     */
    void update(T entity);

    /**
     * Deletes an entity using the underlying data source
     *
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException      when anything goes wrong with the underlying data source
     */
    void delete(T entity);
}
