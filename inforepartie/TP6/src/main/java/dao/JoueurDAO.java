package dao;

import metier.Joueur;

import java.util.List;

public class JoueurDAO {
    public static Joueur findById(int id) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp6", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT jno, pseudo, email, pwd, elo FROM Joueur where jno = ?")) {
            stmt.setInt(1, id);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int jno = rs.getInt("jno");
                    String pseudo = rs.getString("pseudo");
                    String email = rs.getString("email");
                    String pwd = rs.getString("pwd");
                    int elo = rs.getInt("elo");
                    Joueur joueur = new Joueur(jno, pseudo, email, pwd, elo);
                    return joueur;
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return null;
    }

    public static void create(Joueur joueur) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp6", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("INSERT INTO Joueur(jno, pseudo, email, pwd, elo) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setInt(1, joueur.getJno());
            stmt.setString(2, joueur.getPseudo());
            stmt.setString(3, joueur.getEmail());
            stmt.setString(4, joueur.getPwd());
            stmt.setInt(5, joueur.getElo());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Joueur created successfully.");
            } else {
                System.out.println("Failed to create joueur.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static List<Joueur> findAll() {
        List<Joueur> joueurs = new java.util.ArrayList<>();
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp6", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT jno, pseudo, email, pwd, elo FROM Joueur")) {
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int jno = rs.getInt("jno");
                    String pseudo = rs.getString("pseudo");
                    String email = rs.getString("email");
                    String pwd = rs.getString("pwd");
                    int elo = rs.getInt("elo");
                    Joueur joueur = new Joueur(jno, pseudo, email, pwd, elo);
                    joueurs.add(joueur);
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return joueurs;
    }

    public static void delete(int id) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp6", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("DELETE FROM Joueur WHERE jno = ?")) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Joueur deleted successfully.");
            } else {
                System.out.println("Failed to delete joueur.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static void update(Joueur joueur) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp6", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("UPDATE Joueur SET pseudo = ?, email = ?, pwd = ?, elo = ? WHERE jno = ?")) {
            stmt.setString(1, joueur.getPseudo());
            stmt.setString(2, joueur.getEmail());
            stmt.setString(3, joueur.getPwd());
            stmt.setInt(4, joueur.getElo());
            stmt.setInt(5, joueur.getJno());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Joueur updated successfully.");
            } else {
                System.out.println("Failed to update joueur.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }


}
