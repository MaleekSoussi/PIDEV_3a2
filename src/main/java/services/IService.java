package services;

import java.sql.SQLException;
import java.util.List;

public interface IService<T> {
    T create(T t) throws SQLException;
    void update(T t) throws SQLException;
    void delete(int id) throws SQLException;
    List<T> read(String parameter) throws SQLException;
    T readById(int id) throws SQLException;
}
