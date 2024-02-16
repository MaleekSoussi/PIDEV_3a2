package services;


import models.Person;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonService implements IService<Person>{

    private Connection connection;

    public PersonService(){
        connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void create(Person person) throws SQLException {
        String sql = "insert into person (firstName,lastName,age)"+
                "values('"+person.getFirstName()+"','"+person.getLastName()+"'" +
                ","+person.getAge()+")";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }




    public void update(Person person) throws SQLException {
        String sql = "update person set firstName = ?, lastName = ?, age = ? where id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, person.getFirstName());
        ps.setString(2, person.getLastName());
        ps.setInt(3,person.getAge());
        ps.setInt(4,person.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public List<Person> read() throws SQLException {
        String sql = "select * from person";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Person> people = new ArrayList<>();
        while (rs.next()){
            Person p = new Person();
            p.setId(rs.getInt("id"));
            p.setAge(rs.getInt("age"));
            p.setFirstName(rs.getString("firstName"));
            p.setLastName(rs.getString("lastName"));

            people.add(p);
        }
        return people;
    }
}
