package services;
import entities.art;
import utils.MyDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArtServices implements IServices <art> {
    private Connection con;
    public ArtServices(){ con = MyDB.getInstance().getConnection();}

    @Override
    public void add(art a) throws SQLException {
        // SQL query to insert art data into the database
        String req = "INSERT INTO art ( title, materials, height, width, type, city, description,price,id_category,path_image) VALUES ( ?, ?, ?, ?, ?, ?, ?, ? , ?, ?)";
        //analyse->comuling-->optimizing-->executing
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
            pstmt.setInt(9, a.getId_category());
            pstmt.setString(10, a.getPath_image());

            // Executing the update operation
            pstmt.executeUpdate();

        }}

    @Override
    public void modify(art newart, int id_art) throws SQLException {
        String req = "UPDATE art SET title=?, materials=?, height=?, width=?, type=?, city=?, description=? ,price=? ,id_category=?,path_image=? WHERE id_art=?";
        PreparedStatement pre = con.prepareStatement(req);
        pre.setString(1, newart.getTitle());
        pre.setString(2, newart.getMaterials());
        pre.setDouble(3, newart.getHeight());
        pre.setDouble(4, newart.getWidth());
        pre.setString(5, newart.getType());
        pre.setString(6, newart.getCity());
        pre.setString(7, newart.getDescription());
        pre.setFloat(8, newart.getPrice());
        pre.setInt(9, newart.getId_category());
        pre.setString(10, newart.getPath_image());
        pre.setInt(11, id_art);

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
            a.setId_category(res.getInt(10));
            a.setPath_image(res.getString(11));
            arts.add(a);
        }

        return arts;
    }

    @Override
    public List<art> getOneArt() throws SQLException {
        List<art> getOneArt = new ArrayList<>() ;
        String req = "SELECT A.*, C.name AS name FROM art A INNER JOIN category C ON A.id_category = C.id_category";
        Statement stmt = con.createStatement();
        ResultSet res = stmt.executeQuery(req);
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
            a.setId_category(res.getInt(10));
            a.setPath_image(res.getString(11));

            getOneArt.add(a);
        }
        return getOneArt;
    }
    public List<art> searchArt(String search) {
        List<art> artList = new ArrayList<>();
        try {
            String query = "SELECT * FROM art WHERE title LIKE ? ";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, "%" + search + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            // Parcours du résultat de la requête
            while (resultSet.next()) {
                art art = new art();
                art.setId_art(resultSet.getInt(1));
                art.setTitle(resultSet.getString(2));
                art.setMaterials(resultSet.getString(3));
                art.setHeight(resultSet.getDouble(4));
                art.setWidth(resultSet.getDouble(5));
                art.setType(resultSet.getString(6));
                art.setCity(resultSet.getString(7));
                art.setDescription(resultSet.getString(8));
                art.setPrice(resultSet.getFloat(9));
                art.setId_category(resultSet.getInt(10));
                art.setPath_image(resultSet.getString(11));
                artList.add(art);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return artList;
    }
    public art getArtById(int id) throws SQLException {
        art art = null;
        String query = "SELECT * FROM art WHERE id_art = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            // If a record is found, create an art object and populate its fields
            if (resultSet.next()) {
                art = new art();
                art.setId_art(resultSet.getInt(1));
                art.setTitle(resultSet.getString(2));
                art.setMaterials(resultSet.getString(3));
                art.setHeight(resultSet.getDouble(4));
                art.setWidth(resultSet.getDouble(5));
                art.setType(resultSet.getString(6));
                art.setCity(resultSet.getString(7));
                art.setDescription(resultSet.getString(8));
                art.setPrice(resultSet.getFloat(9));
                art.setId_category(resultSet.getInt(10));
                art.setPath_image(resultSet.getString(11));
            }
        }
        return art;
    }
    public List<art> getAllArt() {
        List<art> artList = new ArrayList<>();
        String query = "SELECT * FROM artworks"; // Adjust query based on your database schema

        try (
             PreparedStatement statement = con.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                // Create art objects from the result set and add them to the list
                art art = new art();
                art.setId_art(resultSet.getInt("id"));
                art.setTitle(resultSet.getString("title"));
                // Set other attributes accordingly

                artList.add(art);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as necessary
        }

        return artList;
    }
    @Override
    public int ConseilNumbers() throws SQLException {
        int count = 0;
        String req = "SELECT COUNT(*) AS total_arts FROM art";

        try (Statement stmt = con.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(req);

            if (resultSet.next()) {
                count = resultSet.getInt("total_arts");
            }
        }
        return count;
    }
@Override
    public art getLastAddedArt() throws SQLException {
        art art = new art();
        String req = "SELECT * FROM art ORDER BY dateCreation DESC LIMIT 1";

        try (Statement stmt = con.createStatement(); ResultSet res = stmt.executeQuery(req)) {
            while (res.next()) {
                art.setDateCreation(res.getTimestamp("dateCreation"));
            }
        }

        return art;
    }
    @Override
    public String getCategoryNames(int idCategory) throws SQLException {
        String categoryName = null;
        String req = "SELECT name FROM category WHERE id_category = ?";
        PreparedStatement preparedStatement = con.prepareStatement(req);
        //Sets the value of the first parameter in the SQL query to the idCategory parameter.
        preparedStatement.setInt(1, idCategory);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            categoryName = resultSet.getString("name");
        }
        return categoryName;
    }

    public Map<Integer, Long> getArtCountByCategory() throws SQLException {
        Map<Integer, Long> ArtCounts = new HashMap<>();
        String req = "SELECT id_category, COUNT(id_art) as art_count FROM art GROUP BY id_category";

        try (Statement stmt = con.createStatement(); ResultSet res = stmt.executeQuery(req)) {
            while (res.next()) {
                int idCategorie = res.getInt("id_category");
                long ArtCount = res.getLong("art_count");
                ArtCounts.put(idCategorie, ArtCount);
            }
        }

        return ArtCounts;
    }
    }

