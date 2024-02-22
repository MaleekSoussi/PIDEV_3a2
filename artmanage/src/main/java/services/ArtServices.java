package services;
import entities.art;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtServices implements IServices <art> {
    private Connection con;
    public ArtServices(){ con = MyDB.getInstance().getConnection();}

    @Override
    public void add(art a) throws SQLException {
        // SQL query to insert art data into the database
        String req = "INSERT INTO art ( title, materials, height, width, type, city, description,price) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";

        // Using try-with-resources to ensure proper resource management
        try (PreparedStatement pstmt = con.prepareStatement(req)) {
            // Setting the parameters for the prepared statement
            pstmt.setString(1, a.getTitle());
            pstmt.setString(2, a.getMaterials());
            pstmt.setDouble(3, a.getHeight()); // Corrected index from 4 to 3
            pstmt.setDouble(4, a.getWidth());
            pstmt.setString(5, a.getType());
            pstmt.setString(6, a.getCity());
            pstmt.setString(7, a.getDescription());
            pstmt.setFloat(8, a.getPrice());

            // Executing the update operation
            pstmt.executeUpdate();

        }}

    @Override
    public void modify(art newart, int id_art) throws SQLException {
        String req = "UPDATE art SET title=?, materials=?, height=?, width=?, type=?, city=?, description=? ,price=? WHERE id_art=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, newart.getTitle());
        pre.setString(2, newart.getMaterials());
        pre.setDouble(3, newart.getHeight());
        pre.setDouble(4, newart.getWidth());
        pre.setString(5, newart.getType());
        pre.setString(6, newart.getCity());
        pre.setString(7, newart.getDescription());
        pre.setFloat(8, newart.getPrice());
        pre.setInt(9, id_art);

        pre.executeUpdate();
    }


    public void delete(int id_art)throws SQLException {

            String req = "delete  from art where id_art=?";
            PreparedStatement pre = con.prepareStatement(req);

            pre.setInt(1,id_art);
            int rowsAffected = pre.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("art deleted with success.");
            } else {
                System.out.println("Attention !! no one have this  id .");
            }
        }



    @Override
    public List<art> display() throws SQLException {
        List<art> arts = new ArrayList<>();

        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery("SELECT * FROM art");

        while (res.next()){
            art a = new art();
            a.setId_art(res.getInt(1));
            a.setTitle(res.getString(2));
            a.setMaterials(res.getString(3));
            a.setHeight(res.getDouble(4));
            a.setWidth(res.getDouble(5));
            a.setType(res.getString(6));
            a.setCity(res.getString(7));
            a.setDescription(res.getString(8));
            a.setPrice(res.getFloat(9));
            arts.add(a);
        }

        return arts;
    }




}


