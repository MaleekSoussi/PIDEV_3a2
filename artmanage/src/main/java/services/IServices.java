package services;

import entities.art;

import java.sql.SQLException;
import java.util.List;

public interface IServices <T>{
    public  void add(T t ) throws SQLException;
    public  void modify(T t ,int id_art ) throws SQLException;

    public  void delete(int id_art )throws SQLException;
    public List<T> display() throws SQLException;

}
