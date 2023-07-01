package xyz.n8ify.logmaid.utils;

import com.google.gson.Gson;
import xyz.n8ify.logmaid.model.Preset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private static Connection connection = null;

    private static Gson gson = new Gson();

    public static void initial() {

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:logmaid.db");

            try (Statement stm = connection.createStatement()) {
                final String createTable = "CREATE TABLE IF NOT EXISTS PRESET(" +
                        " ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        " PRESET_NAME CHAR(128) NOT NULL," +
                        " PRESET_VALUE TEXT NOT NULL" +
                        " )";
                stm.executeUpdate(createTable);

                final String selectDefaultPreset = "SELECT * FROM PRESET WHERE ID = 1";
                try (ResultSet resultSet = stm.executeQuery(selectDefaultPreset)) {
                    if (!resultSet.next()) {
                        final String createDefaultPreset = "INSERT INTO PRESET VALUES(1, 'Default', '')";
                        stm.executeUpdate(createDefaultPreset);
                    }
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static List<Preset> loadPresets() throws SQLException {
        List<Preset> presets = new ArrayList<>();
        try (Statement stm = connection.createStatement()) {
            final String sql = "SELECT * FROM PRESET";
            try (ResultSet rs = stm.executeQuery(sql)) {
                while (rs.next()) {
                    presets.add(getPresetFromResultSet(rs));
                }
            }
        }
        return presets;
    }
    public static Preset loadPresetByName(String presetName) throws SQLException {
        final String sql = "SELECT * FROM PRESET WHERE PRESET_NAME = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, presetName);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    return getPresetFromResultSet(rs);
                }
            }
        }
        return null;
    }

    public static List<String> loadPresetNames() throws SQLException {
        List<String> presetNames = new ArrayList<>();
        try (Statement stm = connection.createStatement()) {
            final String sql = "SELECT PRESET_NAME FROM PRESET";
            try (ResultSet rs = stm.executeQuery(sql)) {
                while (rs.next()) {
                    presetNames.add(rs.getString(1));
                }
            }
        }
        return presetNames;
    }

    public static void upsertPreset(Preset preset) throws SQLException {

        int updateId = -1;

        final String selectSql = "SELECT ID FROM PRESET WHERE PRESET_NAME = ?";
        try (PreparedStatement stm = connection.prepareStatement(selectSql)) {
            stm.setString(1, preset.getName());
            try(ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    updateId = rs.getInt(1);
                }
            }
        }

        if (updateId == -1) {
            final String insertSql = "INSERT INTO PRESET (PRESET_NAME, PRESET_VALUE) VALUES(?, ?)";
            try (PreparedStatement stm = connection.prepareStatement(insertSql)) {
                stm.setString(1, preset.getName());
                stm.setString(2, gson.toJson(preset.getConfig()));
                stm.executeUpdate();
            }
        } else {
            final String updateSql = "UPDATE PRESET SET PRESET_NAME = ?, PRESET_VALUE = ? WHERE ID = ?";
            try (PreparedStatement stm = connection.prepareStatement(updateSql)) {
                stm.setString(1, preset.getName());
                stm.setString(2, gson.toJson(preset.getConfig()));
                stm.setInt(3, updateId);
                stm.executeUpdate();
            }
        }

    }

    public static void deletePresetByName(String presetName) throws SQLException {
        final String sql = "DELETE FROM PRESET WHERE PRESET_NAME = ?";
        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setString(1, presetName);
            pstm.executeUpdate();
        }
    }

    /* --- Helper function --- */
    private static Preset getPresetFromResultSet(ResultSet rs) throws SQLException {
        Preset preset = new Preset();
        preset.setId(rs.getInt(1));
        preset.setName(rs.getString(2));
        String rawConfig = rs.getString(3);
        if (StringUtil.isNotNullOrEmpty(rawConfig)) {
            Preset.Config config = new Gson().fromJson(rs.getString(3), Preset.Config.class);
            preset.setConfig(config);
        }
        return preset;
    }

}
