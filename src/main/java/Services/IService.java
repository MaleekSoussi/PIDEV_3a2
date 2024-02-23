package Services;
import Models.Users;

import java.util.List;
import java.sql.SQLException;

public interface IService<T> {
    void create(T t) throws SQLException;

    void update(T t) throws SQLException;

    void delete(int userId) throws SQLException;

    List<T> read() throws SQLException;



}