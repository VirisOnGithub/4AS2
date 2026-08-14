package dao;

import model.Partie;

import java.util.List;
import java.time.LocalDate;
import java.sql.Date;

public class PartieDAO {
    public static Partie findById(int id) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp7", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT pno, jno1, jno2, date, statut, temps, gagnant FROM Partie WHERE pno = ?")) {
            stmt.setInt(1, id);
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int pno = rs.getInt("pno");
                    int jno1 = rs.getInt("jno1");
                    int jno2 = rs.getInt("jno2");
                    Date sqlDate = rs.getDate("date");
                    LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    int statut = rs.getInt("statut");
                    int temps = rs.getInt("temps");
                    int gagnant = rs.getInt("gagnant");
                    return new Partie(pno, jno1, jno2, date, statut, temps, gagnant);
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return null;
    }

    public static void create(Partie partie) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp7", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("INSERT INTO Partie(pno, jno1, jno2, date, statut, temps, gagnant) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
            stmt.setInt(1, partie.getPno());
            stmt.setInt(2, partie.getJno1());
            stmt.setInt(3, partie.getJno2());
            LocalDate ld = partie.getDate();
            if (ld != null) stmt.setDate(4, Date.valueOf(ld)); else stmt.setNull(4, java.sql.Types.DATE);
            stmt.setInt(5, partie.getStatut());
            stmt.setInt(6, partie.getTemps());
            stmt.setInt(7, partie.getGagnant());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Partie created successfully.");
            } else {
                System.out.println("Failed to create partie.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static List<Partie> findAll() {
        List<Partie> parties = new java.util.ArrayList<>();
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp7", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("SELECT pno, jno1, jno2, date, statut, temps, gagnant FROM Partie")) {
            try (java.sql.ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int pno = rs.getInt("pno");
                    int jno1 = rs.getInt("jno1");
                    int jno2 = rs.getInt("jno2");
                    Date sqlDate = rs.getDate("date");
                    LocalDate date = (sqlDate != null) ? sqlDate.toLocalDate() : null;
                    int statut = rs.getInt("statut");
                    int temps = rs.getInt("temps");
                    int gagnant = rs.getInt("gagnant");
                    parties.add(new Partie(pno, jno1, jno2, date, statut, temps, gagnant));
                }
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return parties;
    }

    public static void delete(int id) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp7", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("DELETE FROM Partie WHERE pno = ?")) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Partie deleted successfully.");
            } else {
                System.out.println("Failed to delete partie.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static void update(Partie partie) {
        try (java.sql.Connection conn = java.sql.DriverManager.getConnection("jdbc:postgresql://localhost:5432/tp7", "dev", "dev");
             java.sql.PreparedStatement stmt = conn.prepareStatement("UPDATE Partie SET jno1 = ?, jno2 = ?, date = ?, statut = ?, temps = ?, gagnant = ? WHERE pno = ?")) {
            stmt.setInt(1, partie.getJno1());
            stmt.setInt(2, partie.getJno2());
            LocalDate ld = partie.getDate();
            if (ld != null) stmt.setDate(3, Date.valueOf(ld)); else stmt.setNull(3, java.sql.Types.DATE);
            stmt.setInt(4, partie.getStatut());
            stmt.setInt(5, partie.getTemps());
            stmt.setInt(6, partie.getGagnant());
            stmt.setInt(7, partie.getPno());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Partie updated successfully.");
            } else {
                System.out.println("Failed to update partie.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }
}
