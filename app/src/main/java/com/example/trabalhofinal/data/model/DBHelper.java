package com.example.trabalhofinal.data.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    // Database Info
    private static final String DATABASE_NAME = "eFootballScores";
    private static final int DATABASE_VERSION = 2;

    // Table Names
    private static final String TABLE_MATCH = "matches";
    private static final String TABLE_TEAM = "team";
    private static final String TABLE_PLAYER = "player";
    private static final String TABLE_USER = "user";

    // Matches Table Columns
    private static final String COLUMN_MATCHID = "matchId";
    private static final String COLUMN_DATE = "matchDate";
    private static final String COLUMN_GAME = "game";
    private static final String COLUMN_GAME_MODE = "gameMode";
    private static final String COLUMN_RIVAL = "rival";
    private static final String COLUMN_HOME_TEAM = "homeTeam";
    private static final String COLUMN_GOALS_FOR = "goalsFor";
    private static final String COLUMN_GOALS_AGAINST = "goalsAgainst";
    private static final String COLUMN_AWAY_TEAM = "awayTeam";
    private static final String COLUMN_PENALTIES_GF = "penaltiesGF";
    private static final String COLUMN_PENALTIES_GC = "penaltiesGC";

    // Team Table Columns
    private static final String COLUMN_TEAM_NAME = "name";
    private static final String COLUMN_IMAGE_PATH = "image";
    private static final String COLUMN_USER_FK = "user_id";

    // Player Table Columns
    private static final String COLUMN_PLAYER_NAME = "player";

    // User Table Columns
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USER_NAME = "username";
    private static final String COlUMN_PASSWORD = "password";

    // Statistic Columns for Tables Team and Player
    private static final String COLUMN_GAMES_PLAYED = "gamesPlayed";
    private static final String COLUMN_WINS = "wins";
    private static final String COLUMN_DRAWS = "draws";
    private static final String COLUMN_DEFEATS = "defeats";

    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_MATCH = "CREATE TABLE " + TABLE_MATCH +
                "(" +
                COLUMN_MATCHID + " INTEGER PRIMARY KEY AUTOINCREMENT," + // Define a primary key
                COLUMN_DATE + " TEXT," +
                COLUMN_GAME + " TEXT," +
                COLUMN_GAME_MODE + " TEXT," +
                COLUMN_RIVAL + " TEXT," +
                COLUMN_HOME_TEAM + " TEXT," +
                COLUMN_GOALS_FOR + " INTEGER," +
                COLUMN_GOALS_AGAINST + " INTEGER," +
                COLUMN_AWAY_TEAM + " TEXT," +
                COLUMN_PENALTIES_GF + " INTEGER," +
                COLUMN_PENALTIES_GC + " INTEGER," +
                COLUMN_USER_FK + " TEXT REFERENCES " + TABLE_USER + " ON DELETE CASCADE ON UPDATE CASCADE" +
                ")";

        String CREATE_TABLE_TEAM = "CREATE TABLE " + TABLE_TEAM +
                "(" +
                COLUMN_TEAM_NAME + " TEXT," +
                COLUMN_IMAGE_PATH + " TEXT," +
                COLUMN_GAMES_PLAYED + " INTEGER," +
                COLUMN_WINS + " INTEGER," +
                COLUMN_DRAWS + " INTEGER," +
                COLUMN_DEFEATS + " INTEGER," +
                COLUMN_GOALS_FOR + " INTEGER," +
                COLUMN_GOALS_AGAINST + " INTEGER," +
                COLUMN_USER_FK + " TEXT REFERENCES " + TABLE_USER +
                " ON DELETE CASCADE ON UPDATE CASCADE," +
                "PRIMARY KEY(" + COLUMN_TEAM_NAME + ", " + COLUMN_USER_FK + ")" +
                ")";

        String CREATE_TABLE_PLAYER = "CREATE TABLE " + TABLE_PLAYER +
                "(" +
                COLUMN_PLAYER_NAME + " TEXT," +
                COLUMN_GAMES_PLAYED + " INTEGER," +
                COLUMN_WINS + " INTEGER," +
                COLUMN_DRAWS + " INTEGER," +
                COLUMN_DEFEATS + " INTEGER," +
                COLUMN_GOALS_FOR + " INTEGER," +
                COLUMN_GOALS_AGAINST + " INTEGER," +
                COLUMN_USER_FK +
                " TEXT REFERENCES " + TABLE_USER + " ON DELETE CASCADE ON UPDATE CASCADE," +
                "PRIMARY KEY(" + COLUMN_PLAYER_NAME + ", " + COLUMN_USER_FK + ")" +
                ")";

        String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER  +
                "(" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY," +
                COLUMN_USER_NAME +  " TEXT," +
                COlUMN_PASSWORD + " TEXT" +
                ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_TEAM);
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_MATCH);
    }

    // Called when the database needs to be upgraded.
    // This method will only be called if a database already exists on disk with the same DATABASE_NAME,
    // but the DATABASE_VERSION is different than the version of the database that exists on disk.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
            onCreate(db);
        }
    }

    public void addUser(User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_EMAIL, user.getEmail());
            values.put(COLUMN_USER_NAME, user.getUsername());
            values.put(COlUMN_PASSWORD, user.getPassword());

            System.out.println("Banco " + user.getEmail());
            System.out.println(user.getPassword());

            db.insertOrThrow(TABLE_USER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while creating a user.");
        } finally {
            db.endTransaction();
        }
    }

    public void addPlayer(Player player, User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_FK, user.getEmail());
            values.put(COLUMN_PLAYER_NAME, player.getName());
            values.put(COLUMN_GAMES_PLAYED, player.getStats().getGamesPlayed());
            values.put(COLUMN_WINS, player.getStats().getWins());
            values.put(COLUMN_DRAWS, player.getStats().getDraws());
            values.put(COLUMN_DEFEATS, player.getStats().getDefeats());
            values.put(COLUMN_GOALS_FOR, player.getStats().getGoalsFor());
            values.put(COLUMN_GOALS_AGAINST, player.getStats().getGoalsAgainst());

            db.insertOrThrow(TABLE_PLAYER, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add a player.");
        } finally {
            db.endTransaction();
        }
    }

    public void addTeam(Team team, User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TEAM_NAME, team.getName());
            values.put(COLUMN_IMAGE_PATH, team.getBadgePath());
            values.put(COLUMN_GAMES_PLAYED, team.getStats().getGamesPlayed());
            values.put(COLUMN_WINS, team.getStats().getWins());
            values.put(COLUMN_DRAWS, team.getStats().getDraws());
            values.put(COLUMN_DEFEATS, team.getStats().getDefeats());
            values.put(COLUMN_GOALS_FOR, team.getStats().getGoalsFor());
            values.put(COLUMN_GOALS_AGAINST, team.getStats().getGoalsAgainst());
            values.put(COLUMN_USER_FK, user.getEmail());

            db.insertOrThrow(TABLE_TEAM, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add a team.");
        } finally {
            db.endTransaction();
        }
    }

    public void addMatch(Match match, User user) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_DATE, match.getMatchDate().toString());
            values.put(COLUMN_GAME, match.getGame());
            values.put(COLUMN_GAME_MODE, match.getGameMode());
            values.put(COLUMN_RIVAL, match.getRival());
            values.put(COLUMN_HOME_TEAM, match.getMyTeam());
            values.put(COLUMN_GOALS_FOR, match.getGoalsFor());
            values.put(COLUMN_GOALS_AGAINST, match.getGoalsAgainst());
            values.put(COLUMN_AWAY_TEAM, match.getRivalTeam());
            values.put(COLUMN_USER_FK, user.getEmail());
            System.out.println("Penalti Add " + match.getPenaltiesGF());
            if (match.getPenaltiesGF() != null) {
                values.put(COLUMN_PENALTIES_GF, match.getPenaltiesGF());
                values.put(COLUMN_PENALTIES_GC, match.getPenaltiesGA());
            }

            db.insertOrThrow(TABLE_MATCH, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to add a match. " + e);
        } finally {
            db.endTransaction();
        }
    }

    public List<User> getUser(User user) {
        List<User> users = new ArrayList<>();

        String USER_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s = '%s'",
                        TABLE_USER, COLUMN_EMAIL, user.getEmail());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(USER_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    User newUser = new User();
                    newUser.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)));
                    newUser.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                    newUser.setPassword(cursor.getString(cursor.getColumnIndex(COlUMN_PASSWORD)));

                    users.add(newUser);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get a user from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return users;
    }

    public List<Player> getAllPlayers(User user) {
        List<Player> players = new ArrayList<>();

        String PLAYER_SELECT_QUERY =
                String.format("SELECT * FROM %s WHERE %s LIKE '%s' ORDER BY %s DESC",
                        TABLE_PLAYER, COLUMN_USER_FK, user.getEmail(), COLUMN_GAMES_PLAYED);

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PLAYER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Stats newStats = new Stats();
                    newStats.setGamesPlayed(cursor.getInt(cursor.getColumnIndex(COLUMN_GAMES_PLAYED)));
                    newStats.setWins(cursor.getInt(cursor.getColumnIndex(COLUMN_WINS)));
                    newStats.setDraws(cursor.getInt(cursor.getColumnIndex(COLUMN_DRAWS)));
                    newStats.setDefeats(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFEATS)));
                    newStats.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));

                    Player newPlayer = new Player();
                    newPlayer.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME)));
                    newPlayer.setStats(newStats);

                    players.add(newPlayer);

                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get players from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return players;
    }

    public List<Player> getPlayer(String playerName, User user) {
        List<Player> players = new ArrayList<>();

        String PLAYER_SELECT_QUERY = String.format("SELECT * FROM %s " +
                        "WHERE %s = '%s' AND %s = '%s'",
                TABLE_PLAYER,
                COLUMN_PLAYER_NAME,
                playerName,
                COLUMN_USER_FK, user.getEmail());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(PLAYER_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Stats newStats = new Stats();
                    newStats.setGamesPlayed(cursor.getInt(cursor.getColumnIndex(COLUMN_GAMES_PLAYED)));
                    newStats.setWins(cursor.getInt(cursor.getColumnIndex(COLUMN_WINS)));
                    newStats.setDraws(cursor.getInt(cursor.getColumnIndex(COLUMN_DRAWS)));
                    newStats.setDefeats(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFEATS)));
                    newStats.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));

                    Player newPlayer = new Player();
                    newPlayer.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PLAYER_NAME)));
                    newPlayer.setStats(newStats);

                    players.add(newPlayer);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get players from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return players;
    }

    public List<Team> getAllTeams(User user) {
        List<Team> teams = new ArrayList<>();

        String TEAM_SELECT_QUERY = String.format("SELECT * " +
                "FROM %s " +
                "WHERE %s = '%s' " +
                "ORDER BY %s DESC, %s DESC",
                TABLE_TEAM, COLUMN_USER_FK, user.getEmail(),
                COLUMN_GAMES_PLAYED, COLUMN_WINS);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TEAM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Stats newStats = new Stats();
                    newStats.setGamesPlayed(cursor.getInt(cursor.getColumnIndex(COLUMN_GAMES_PLAYED)));
                    newStats.setWins(cursor.getInt(cursor.getColumnIndex(COLUMN_WINS)));
                    newStats.setDraws(cursor.getInt(cursor.getColumnIndex(COLUMN_DRAWS)));
                    newStats.setDefeats(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFEATS)));
                    newStats.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));

                    Team newTeam = new Team();
                    newTeam.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TEAM_NAME)));
                    newTeam.setBadgePath();
                    newTeam.setStats(newStats);

                    teams.add(newTeam);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get teams from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return teams;
    }

    public List<Team> getTeam(String teamName, User user) {
        List<Team> teams = new ArrayList<>();

        String TEAM_SELECT_QUERY = String.format("SELECT * " +
                        "FROM %s " +
                        "WHERE %s = '%s' AND %s = '%s'",
                TABLE_TEAM,
                COLUMN_TEAM_NAME,
                teamName, COLUMN_USER_FK, user.getEmail());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TEAM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Stats newStats = new Stats();
                    newStats.setGamesPlayed(cursor.getInt(cursor.getColumnIndex(COLUMN_GAMES_PLAYED)));
                    newStats.setWins(cursor.getInt(cursor.getColumnIndex(COLUMN_WINS)));
                    newStats.setDraws(cursor.getInt(cursor.getColumnIndex(COLUMN_DRAWS)));
                    newStats.setDefeats(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFEATS)));
                    newStats.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));

                    Team newTeam = new Team();
                    newTeam.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TEAM_NAME)));
                    newTeam.setBadgePath();
                    newTeam.setStats(newStats);

                    teams.add(newTeam);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get teams from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return teams;
    }

    public List<Match> getAllMatches(User user) {
        List<Match> matches = new ArrayList<>();

        String MATCH_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = '%s'",
                TABLE_MATCH, COLUMN_USER_FK, user.getEmail());

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(MATCH_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Match newMatch = new Match();
                    newMatch.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCHID)));
                    newMatch.setMatchDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DATE))));
                    newMatch.setGame(cursor.getString(cursor.getColumnIndex(COLUMN_GAME)));
                    newMatch.setGameMode(cursor.getString(cursor.getColumnIndex(COLUMN_GAME_MODE)));
                    newMatch.setRival(cursor.getString(cursor.getColumnIndex(COLUMN_RIVAL)));
                    newMatch.setMyTeam(cursor.getString(cursor.getColumnIndex(COLUMN_HOME_TEAM)));
                    newMatch.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newMatch.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));
                    newMatch.setRivalTeam(cursor.getString(cursor.getColumnIndex(COLUMN_AWAY_TEAM)));
                    newMatch.setPenaltiesGF(cursor.getInt(cursor.getColumnIndex(COLUMN_PENALTIES_GF)));
                    newMatch.setPenaltiesGA(cursor.getInt(cursor.getColumnIndex(COLUMN_PENALTIES_GC)));

                    matches.add(newMatch);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get matches from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return matches;
    }

    public List<Match> getMatch(User user, int id) {
        List<Match> matches = new ArrayList<>();

        String MATCH_SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = '%s' AND %s = '%s'",
                TABLE_MATCH, COLUMN_USER_FK, user.getEmail(), COLUMN_MATCHID, id);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(MATCH_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Match newMatch = new Match();
                    newMatch.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_MATCHID)));
                    newMatch.setMatchDate(Date.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_DATE))));
                    newMatch.setGame(cursor.getString(cursor.getColumnIndex(COLUMN_GAME)));
                    newMatch.setGameMode(cursor.getString(cursor.getColumnIndex(COLUMN_GAME_MODE)));
                    newMatch.setRival(cursor.getString(cursor.getColumnIndex(COLUMN_RIVAL)));
                    newMatch.setMyTeam(cursor.getString(cursor.getColumnIndex(COLUMN_HOME_TEAM)));
                    newMatch.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newMatch.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));
                    newMatch.setRivalTeam(cursor.getString(cursor.getColumnIndex(COLUMN_AWAY_TEAM)));
                    newMatch.setPenaltiesGF(cursor.getInt(cursor.getColumnIndex(COLUMN_PENALTIES_GF)));
                    newMatch.setPenaltiesGA(cursor.getInt(cursor.getColumnIndex(COLUMN_PENALTIES_GC)));

                    matches.add(newMatch);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get matches from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return matches;
    }

    public List<Team> getMostPlayedTeams() {
        List<Team> teams = new ArrayList<>();

        String TEAM_SELECT_QUERY = String.format("SELECT * FROM %s ORDER BY %s DESC LIMIT 10",
                TABLE_TEAM, COLUMN_GAMES_PLAYED);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(TEAM_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Stats newStats = new Stats();
                    newStats.setGamesPlayed(cursor.getInt(cursor.getColumnIndex(COLUMN_GAMES_PLAYED)));
                    newStats.setWins(cursor.getInt(cursor.getColumnIndex(COLUMN_WINS)));
                    newStats.setDraws(cursor.getInt(cursor.getColumnIndex(COLUMN_DRAWS)));
                    newStats.setDefeats(cursor.getInt(cursor.getColumnIndex(COLUMN_DEFEATS)));
                    newStats.setGoalsFor(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_FOR)));
                    newStats.setGoalsAgainst(cursor.getInt(cursor.getColumnIndex(COLUMN_GOALS_AGAINST)));

                    Team newTeam = new Team();
                    newTeam.setName(cursor.getString(cursor.getColumnIndex(COLUMN_TEAM_NAME)));
                    newTeam.setStats(newStats);

                    teams.add(newTeam);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to get teams from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return teams;
    }

    public int updateMatch(Match match, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, match.getMatchDate().toString());
        values.put(COLUMN_GAME, match.getGame());
        values.put(COLUMN_GAME_MODE, match.getGameMode());
        values.put(COLUMN_RIVAL, match.getRival());
        values.put(COLUMN_HOME_TEAM, match.getMyTeam());
        values.put(COLUMN_GOALS_FOR, match.getGoalsFor());
        values.put(COLUMN_GOALS_AGAINST, match.getGoalsAgainst());
        values.put(COLUMN_AWAY_TEAM, match.getRivalTeam());
        values.put(COLUMN_USER_FK, user.getEmail());
        if (match.getPenaltiesGF() != null) {
            values.put(COLUMN_PENALTIES_GF, match.getPenaltiesGF());
            values.put(COLUMN_PENALTIES_GC, match.getPenaltiesGA());
        }

        return db.update(TABLE_MATCH, values,
                COLUMN_MATCHID + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(match.getId()), String.valueOf(user.getEmail())});

    }

    public void updateTeamStats(Team team, User user) {
        updateTeamGamesPlayed(team, user);
        updateTeamWins(team, user);
        updateTeamDraws(team, user);
        updateTeamDefeats(team, user);
        updateTeamGoalsFor(team, user);
        updateTeamGoalsAgainst(team, user);

    }

    public int updateTeamGamesPlayed(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GAMES_PLAYED, team.getStats().getGamesPlayed());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public int updateTeamWins(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WINS, team.getStats().getWins());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public int updateTeamDraws(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRAWS, team.getStats().getDraws());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public int updateTeamDefeats(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DEFEATS, team.getStats().getDefeats());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public int updateTeamGoalsFor(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GOALS_FOR, team.getStats().getGoalsFor());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public int updateTeamGoalsAgainst(Team team, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GOALS_AGAINST, team.getStats().getGoalsAgainst());

        return db.update(TABLE_TEAM, values,
                COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(team.getName()), String.valueOf(user.getEmail())});
    }

    public void updatePlayerStats(Player player, User user) {
        updatePlayerGamesPlayed(player, user);
        updatePlayerWins(player, user);
        updatePlayerDraws(player, user);
        updatePlayerDefeats(player, user);
        updatePlayerGoalsFor(player, user);
        updatePlayerGoalsAgainst(player, user);
    }

    public int updatePlayerGamesPlayed(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GAMES_PLAYED, player.getStats().getGamesPlayed());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }

    public int updatePlayerWins(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_WINS, player.getStats().getWins());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }

    public int updatePlayerDraws(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DRAWS, player.getStats().getDraws());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }

    public int updatePlayerDefeats(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DEFEATS, player.getStats().getDefeats());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }

    public int updatePlayerGoalsFor(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GOALS_FOR, player.getStats().getGoalsFor());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }

    public int updatePlayerGoalsAgainst(Player player, User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_GOALS_AGAINST, player.getStats().getGoalsAgainst());

        return db.update(TABLE_PLAYER, values,
                COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?",
                new String[] {String.valueOf(player.getName()), String.valueOf(user.getEmail())});
    }


    public void deleteTeam(Team team, User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_TEAM,
                    COLUMN_TEAM_NAME + " = ? AND " + COLUMN_USER_FK + " = ?"
                    , new String[]{team.getName(), user.getEmail()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to delete a team");
        } finally {
            db.endTransaction();
        }
    }

    public void deletePlayer(Player player, User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_PLAYER,
                    COLUMN_PLAYER_NAME + " = ? AND " + COLUMN_USER_FK + " = ?"
                    , new String[]{player.getName(), user.getEmail()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to delete a player");
        } finally {
            db.endTransaction();
        }
    }

    public void deleteMatch(Match match, User user) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(TABLE_MATCH,
                    COLUMN_MATCHID + " = ? AND " + COLUMN_USER_FK + " = ?"
                    , new String[]{String.valueOf(match.getId()), user.getEmail()});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d("TAG", "Error while trying to delete a player");
        } finally {
            db.endTransaction();
        }
    }
}
