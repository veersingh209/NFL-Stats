import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class java {
public static void main(String[] args){
								Scanner rdr = new Scanner(System.in);
								Connection conn = null;
								try{
																conn = DriverManager.getConnection("jdbc:/Users/veersingh/Desktop/V/Fall2019/CSE111/ProjectFinal/Project/nfl.db");
																int userInput = 0;
																do {
																								System.out.println("MAIN MENU");
																								System.out.println("(1)Admin");
																								System.out.println("(2)Coach");
																								System.out.println("(3)User");
																								System.out.println("(0)Quit");
																								userInput = rdr.nextInt();
																								System.out.println();
																								if(userInput == 1) admin(conn);
																								else if(userInput == 2) coachMenu(conn);
																								else if(userInput == 3) userMenu(conn);
																								else;
																} while(userInput != 0);
																System.out.println();
																if(conn != null) conn.close();
								} catch(SQLException e) { System.err.println(e);}
}
public static void admin(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								do {
																System.out.println("Admin Menu");
																System.out.println("(1)Coaches");
																System.out.println("(2)Games");
																System.out.println("(3)Players");
																System.out.println("(4)Players-Stats");
																System.out.println("(5)Teams");
																System.out.println("(6)Teams-Stats");
																System.out.println("(0)Back");
																userInput = rdr.nextInt();
																System.out.println();
																if(userInput == 1) adminCoach(conn);
																else if(userInput == 2) adminGames(conn);
																else if(userInput == 3) adminPlayers(conn);
																else if(userInput == (4)) adminPlayerStats(conn);
																else if(userInput == 5) adminTeams(conn);
																else if(userInput == 6) adminTeamStats(conn);
																else;
								} while(userInput != 0); System.out.println();
}
public static void adminCoach(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																int editCoach = 0;
																int typeEdit = 0;
																do {
																								rs = s.executeQuery("SELECT c_name, c_teamName, c_coachID, c_superbowls FROM Coaches;");
																								System.out.println(String.format("%-23s %-5s %-5s %s", "Name", "TeamID", "CoachID", "Superbowls"));
																								while(rs.next()) {
																																System.out.println(String.format("%-23s %-5s %-5s %s", rs.getString("c_name"), rs.getInt("c_teamName"), rs.getInt("c_coachID"), rs.getInt("c_superbowls")));
																								} System.out.print("Enter type of edit(1 = insert, 2 = delete, 3 = update, 0 = quit): ");
																								typeEdit = rdr.nextInt();
																								if(typeEdit == 1) coachEdit(conn, 0, typeEdit);
																								else if(typeEdit == 2 || typeEdit == 3) {
																																System.out.print("Enter coach ID to edit: ");
																																editCoach = rdr.nextInt();
																																coachEdit(conn, editCoach, typeEdit);
																								} else;
																} while(typeEdit != 0); System.out.println();
								} catch(SQLException e) { System.err.println(e.getMessage()); }
}
public static void coachEdit(Connection conn, int coachID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																PreparedStatement prepStat = null;
																ResultSet rs;
																if(typeEdit == 1) {
																								String coachName = "";
																								int team = 0;
																								int newCoachID = 0;
																								prepStat = conn.prepareStatement("INSERT INTO Coaches values(?,?,?,0,0,0);");
																								System.out.print("Enter new coach name: ");
																								coachName = rdr.nextLine();
																								System.out.print("Enter coach's team ID: ");
																								team = rdr.nextInt();
																								rs = s.executeQuery("SELECT MAX(c_coachID) AS maxCoach FROM Coaches;");
																								while(rs.next()) { newCoachID = rs.getInt("maxCoach") + 1; }
																								prepStat.setString(1, coachName);
																								prepStat.setInt(2, team);
																								prepStat.setInt(3, newCoachID);
																								prepStat.executeUpdate();
																} else if(typeEdit == 2) {
																								prepStat = conn.prepareStatement("DELETE FROM Coaches WHERE c_coachID = ?;");
																								prepStat.setInt(1, coachID);
																								prepStat.executeUpdate();
																} else {
																								int attributeEditing = 0;
																								System.out.print("Enter type editing (1 = name, 2 = team, 3 = superbowls): ");
																								attributeEditing = rdr.nextInt();
																								if(attributeEditing == 1) {
																																String newName = "";
																																prepStat = conn.prepareStatement("UPDATE Coaches SET c_name = ? WHERE c_coachID = ?;");
																																rdr.nextLine();
																																System.out.print("Enter new coach name: ");
																																newName = rdr.nextLine();
																																prepStat.setString(1, newName);
																																prepStat.setInt(2, coachID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 2) {
																																int newTeam = 0;
																																prepStat = conn.prepareStatement("UPDATE Coaches SET c_teamName = ? WHERE c_coachID = ?;");
																																System.out.print("Enter new team ID: ");
																																newTeam = rdr.nextInt();
																																prepStat.setInt(1, newTeam);
																																prepStat.setInt(2, coachID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 3) {
																																int coachTeam = 0;
																																prepStat = conn.prepareStatement("UPDATE Coaches SET c_superbowls = c_superbowls + 1 WHERE c_coachID = ?;");
																																prepStat.setInt(1, coachID);
																																prepStat.executeUpdate();
																																rs = s.executeQuery("SELECT c_teamName FROM Coaches WHERE c_coachID = " + coachID + ";");
																																while(rs.next()) { coachTeam = rs.getInt("c_teamName"); }
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_superbowls = ts_superbowls + 1 WHERE ts_teamID = ?;");
																																prepStat.setInt(1, coachTeam);
																																prepStat.executeUpdate();
																																System.out.println("Increased coaches AND team's superbowls by 1");
																								} else;
																} System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminGames(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																System.out.println();

																int editGames = 0;
																int typeEdit = 0;
																do {
																								rs = s.executeQuery("SELECT g_date, g_date, g_awayTeamName, g_homeTeamName, g_awayScore, g_homeScore, g_result FROM Games;");
																								System.out.println(String.format("%-5s %-12s %-12s %-12s %-12s %-12s %s", "ID", "Date", "AwayTeamID", "HomeTeamID", "Away Points", "Home Points", "Result"));
																								while(rs.next()) { System.out.println(String.format("%-5s %-12s %-12s %-12s %-12s %-12s %s", rs.getInt("g_date"), rs.getString("g_date"), rs.getInt("g_awayTeamName"), rs.getInt("g_homeTeamName"), rs.getInt("g_awayScore"), rs.getInt("g_homeScore"), rs.getInt("g_result"))); }
																								System.out.print("( (1)Insert, (2)Delete, (3)Update, (0)Quit) )");
																								typeEdit = rdr.nextInt();
																								if(typeEdit == 1) adminGamesEdit(conn, 0, typeEdit);
																								else if(typeEdit == 2 || typeEdit == 3) {
																																System.out.print("Enter game ID to edit: ");
																																editGames = rdr.nextInt();
																																adminGamesEdit(conn, editGames, typeEdit);
																								} else;
																} while(typeEdit != 0); System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminGamesEdit(Connection conn, int gameID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																PreparedStatement prepStat = null;

																if(typeEdit == 1) {
																								String gameDate = "";
																								int awayTeamID = 0;
																								int homeTeamID = 0;
																								int awayTeamPoints = 0;
																								int homeTeamPoints = 0;
																								int result = 0;
																								int newGameID = 0;

																								prepStat = conn.prepareStatement("INSERT INTO Games VALUES(?,?,?,?,?,?,?,?);");
																								System.out.print("Enter Date(MM-DD-YYYY): ");
																								gameDate = rdr.nextLine();
																								System.out.print("Away Team-ID: ");
																								awayTeamID = rdr.nextInt();
																								System.out.print("Home Team-ID: ");
																								homeTeamID = rdr.nextInt();
																								System.out.print("Away Team Points: ");
																								awayTeamPoints = rdr.nextInt();
																								System.out.print("Home Team Points: ");
																								homeTeamPoints = rdr.nextInt();
																								System.out.print("Game Result: ");
																								result = rdr.nextInt();
																								rs = s.executeQuery("SELECT MAX(g_date) AS maxGames FROM Games;");
																								while(rs.next()) { newGameID = rs.getInt("maxGames") + 1; }
																								prepStat.setString(1, gameDate);
																								prepStat.setInt(2, newGameID);
																								prepStat.setInt(3, awayTeamID);
																								prepStat.setInt((4), homeTeamID);
																								prepStat.setInt(5, awayTeamPoints);
																								prepStat.setInt(6, homeTeamPoints);
																								prepStat.setInt(7, result);
																								prepStat.executeUpdate();
																} else if(typeEdit == 2) {
																								prepStat = conn.prepareStatement("DELETE FROM Games WHERE g_date = ?;");
																								prepStat.setInt(1, gameID);
																								prepStat.executeUpdate();
																} else{
																								int attributeEditing = 0;
																								System.out.print("Enter type editing ( (1)Date, (2)Away Team, (3)Home Team, (4)Away Points, (5)Home Points, (6)result )");
																								attributeEditing = rdr.nextInt();
																								if(attributeEditing == 1) {
																																String newDate;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_Date = ? WHERE g_date = ?;");
																																rdr.nextLine();
																																System.out.print("New Date: ");
																																newDate = rdr.nextLine();
																																prepStat.setString(1, newDate);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();

																								} else if(attributeEditing == 2) {
																																int newTeam;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_awayTeam = ? WHERE g_date = ?;");
																																System.out.print("New Team-ID: ");
																																newTeam = rdr.nextInt();
																																prepStat.setInt(1, newTeam);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();
																								}
																								else if(attributeEditing == 3) {
																																int newTeam;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_homeTeam = ? WHERE g_date = ?;");
																																System.out.print("New Team-ID: ");
																																newTeam = rdr.nextInt();
																																prepStat.setInt(1, newTeam);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == (4)) {
																																int newPoints;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_awayScore = ? WHERE g_date = ?;");
																																System.out.print("New Points: ");
																																newPoints = rdr.nextInt();
																																prepStat.setInt(1, newPoints);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 5) {
																																int newPoints;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_homeScore = ? WHERE g_date = ?;");
																																System.out.print("New Points: ");
																																newPoints = rdr.nextInt();
																																prepStat.setInt(1, newPoints);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 6) {
																																int Result;
																																prepStat = conn.prepareStatement("UPDATE Games SET g_result = ? WHERE g_date = ?;");
																																System.out.print("New Team-ID: ");
																																Result = rdr.nextInt();
																																prepStat.setInt(1, Result);
																																prepStat.setInt(2, gameID);
																																prepStat.executeUpdate();
																								} else;
																} System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminPlayers(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																int editPlayer = 0;
																int typeEdit = 0;
																do {
																								rs = s.executeQuery("SELECT p_playerName, p_playerID, p_teamID, p_starter FROM Players;");
																								System.out.println(String.format("%-23s %-10s %-10s %s", "Name", "Player ID", "Team ID", "Starter Position"));
																								while(rs.next()) {
																																System.out.println(String.format("%-23s %-10s %-10s %s", rs.getString("p_playerName"), rs.getInt("p_playerID"), rs.getInt("p_teamID"), rs.getInt("p_starter")));
																								} System.out.print("( (1)Insert, (2)Delete, (3)Update, (0)Quit) )");
																								typeEdit = rdr.nextInt();
																								if(typeEdit == 1) adminPlayersEdit(conn, 0, typeEdit);
																								else if(typeEdit == 2 || typeEdit == 3) {
																																System.out.print("Enter Player-ID to Edit: ");
																																editPlayer = rdr.nextInt();
																																adminPlayersEdit(conn, editPlayer, typeEdit);
																								} else;
																} while(typeEdit != 0); System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminPlayersEdit(Connection conn, int playerID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																PreparedStatement prepStat = null;
																if(typeEdit == 1) {
																								String playerName = "";
																								int teamID = 0;
																								int newPlayerID = 0;
																								int starter = 0;
																								prepStat = conn.prepareStatement("INSERT INTO Players VALUES(?,?,?,?);");
																								System.out.print("New Player Name: ");
																								playerName = rdr.nextLine();
																								System.out.print("Player's Team-ID: ");
																								teamID = rdr.nextInt();
																								System.out.print("Starter or Not( (1)Yes, (0)No ) ): ");
																								starter = rdr.nextInt();
																								rs = s.executeQuery("SELECT MAX(p_playerID) AS maxPlayer FROM Players;");
																								while(rs.next()) { newPlayerID = rs.getInt("maxPlayer") + 1; }
																								prepStat.setString(1, playerName);
																								prepStat.setInt(2, newPlayerID);
																								prepStat.setInt(3, teamID);
																								prepStat.setInt((4), starter);
																								prepStat.executeUpdate();
																} else if(typeEdit == 2) {
																								prepStat = conn.prepareStatement("DELETE FROM Players WHERE p_playerID = ?;");
																								prepStat.setInt(1, playerID);
																								prepStat.executeUpdate();

																								prepStat = conn.prepareStatement("DELETE FROM PlayerStats WHERE ps_teamID = ?;");
																								prepStat.setInt(1, playerID);
																								prepStat.executeUpdate();
																} else {
																								int attributeEditing = 0;
																								System.out.print("Enter type editing ( (1)name, (2)Team, (3)Starter) ) ");
																								attributeEditing = rdr.nextInt();

																								if(attributeEditing == 1) {
																																String newName = "";
																																prepStat = conn.prepareStatement("UPDATE Players SET p_playerName = ? WHERE p_playerID = ?;");
																																rdr.nextLine();
																																System.out.print("New Name: ");
																																newName = rdr.nextLine();
																																prepStat.setString(1, newName);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 2) {
																																int newTeam = 0;
																																prepStat = conn.prepareStatement("UPDATE Players SET p_teamID = ? WHERE p_playerID = ?;");
																																System.out.print("New Team-ID: ");
																																newTeam = rdr.nextInt();
																																prepStat.setInt(1, newTeam);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 3) {
																																int currentStarter = 0;
																																rs = s.executeQuery("SELECT p_starter FROM players WHERE p_playerID = " + playerID + ";");
																																while(rs.next()) { currentStarter = rs.getInt("p_starter"); }
																																if(currentStarter == 0) {
																																								currentStarter = 1;
																																								System.out.println("Changed to Starter.");
																																} else {
																																								currentStarter = 0;
																																								System.out.println("Changed to Bench.");
																																} prepStat = conn.prepareStatement("UPDATE Players SET p_starter = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, currentStarter);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else;
																} System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminPlayerStats(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																int editStats = 0;
																int typeEdit = 0;
																do {
																								rs = s.executeQuery("SELECT ps_teamID, ps_touchDowns, ps_fumbles, ps_interception, ps_rushYards, ps_sacks FROM PlayerStats;");
																								System.out.println(String.format("%-10s %-8s %-9s %-8s %-10s %-8s", "ID", "Points", "Assists", "Blocks", "Rebounds", "Steals"));
																								while(rs.next()) { System.out.println(String.format("%-10s %-8s %-9s %-8s %-10s %-8s", rs.getInt("ps_teamID"), rs.getInt("ps_touchDowns"), rs.getInt("ps_fumbles"), rs.getInt("ps_interception"), rs.getInt("ps_rushYards"), rs.getInt("ps_sacks"))); }
																								System.out.print("Enter type of edit( (1)Delete, (2)Update, (0)Quit ) ");
																								typeEdit = rdr.nextInt();
																								if(editStats == 1 || editStats == 2) {
																																System.out.print("Player-ID to Edit: ");
																																editStats = rdr.nextInt();
																																adminPlayerStatsEdit(conn, editStats, typeEdit);
																								} else;
																} while(typeEdit != 0);
																System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminPlayerStatsEdit(Connection conn, int playerID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																PreparedStatement prepStat = null;
																if(typeEdit == 1) {
																								prepStat = conn.prepareStatement("DELETE FROM Players WHERE p_playerID = ?;");
																								prepStat.setInt(1, playerID);
																								prepStat.executeUpdate();
																								prepStat = conn.prepareStatement("DELETE FROM PlayerStats WHERE p_playerID = ?;");
																								prepStat.setInt(1, playerID);
																								prepStat.executeUpdate();
																} else{
																								int attributeEditing = 0;
																								int newValue = 0;
																								System.out.print("Enter type editing (1 = TouchDowns, 2 = Fumbles, 3 = Interceptions, (4) = Rush Yards, 5 = Sacks):");
																								attributeEditing = rdr.nextInt();
																								System.out.print("Enter new value: ");
																								newValue = rdr.nextInt();
																								if(attributeEditing == 1) {
																																prepStat = conn.prepareStatement("UPDATE PlayerStats SET ps_touchDowns = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 2) {
																																prepStat = conn.prepareStatement("UPDATE PlayerStats SET ps_fumbles = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 3) {
																																prepStat = conn.prepareStatement("UPDATE PlayerStats SET ps_interception = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == (4)) {
																																prepStat = conn.prepareStatement("UPDATE PlayerStats SET ps_rushYards = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 5) {
																																prepStat = conn.prepareStatement("UPDATE PlayerStats SET ps_sacks = ? WHERE p_playerID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, playerID);
																																prepStat.executeUpdate();
																								} else;
																} System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminTeams(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																int editTeam = 0;
																int typeEdit = 0;
																do {
																								rs = s.executeQuery("SELECT t_teamID, t_name, t_coachID FROM Teams;");
																								System.out.println(String.format("%-7s %-23s %s", "TeamID", "Team Name", "CoachID"));
																								while(rs.next()) {
																																System.out.println(String.format("%-7s %-23s %s", rs.getInt("t_teamID"), rs.getString("t_name"), rs.getInt("t_coachID")));
																								}
																								System.out.print("( (1)Insert, (2)Delete, (3)Update, (0)Quit) )");
																								typeEdit = rdr.nextInt();
																								if(typeEdit == 1) adminTeamsEdit(conn, 0, typeEdit);
																								else if(typeEdit == 2 || typeEdit == 3) {
																																System.out.print("Enter team ID to edit: ");
																																editTeam = rdr.nextInt();
																																adminTeamsEdit(conn, editTeam, typeEdit);
																								} else;
																} while(typeEdit != 0); System.out.println();
								}	catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminTeamsEdit(Connection conn, int teamID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																PreparedStatement prepStat = null;
																if(typeEdit == 1) {
																								String teamName = "";
																								int newTeamID = 0;
																								int coachID = 0;
																								prepStat = conn.prepareStatement("INSERT INTO Teams values(?,?,?,?,?,?);");
																								System.out.print("New Team Name: ");
																								teamName = rdr.nextLine();
																								System.out.print("Team's Coach-ID: ");
																								coachID = rdr.nextInt();
																								rs = s.executeQuery("SELECT MAX(t_teamID) AS maxTeam FROM Teams;");
																								while(rs.next()) { newTeamID = rs.getInt("maxTeam") + 1; }
																								prepStat.setInt(1, newTeamID);
																								prepStat.setString(2, teamName);
																								prepStat.setInt(3, coachID);
																								prepStat.executeUpdate();
																} else if(typeEdit == 2) {
																								prepStat = conn.prepareStatement("DELETE FROM Teams WHERE t_teamID = ?;");
																								prepStat.setInt(1, teamID);
																								prepStat.executeUpdate();
																								prepStat = conn.prepareStatement("DELETE FROM TeamStats WHERE ts_teamID = ?;");
																								prepStat.setInt(1, teamID);
																								prepStat.executeUpdate();
																} else{
																								int attributeEditing = 0;
																								System.out.print("(1)Name, (2)Coach):");
																								attributeEditing = rdr.nextInt();
																								if(attributeEditing == 1) {
																																String newName;
																																rdr.nextLine();
																																System.out.print("New Name: ");
																																newName = rdr.nextLine();
																																prepStat = conn.prepareStatement("UPDATE Teams SET t_name = ? WHERE t_teamID = ?;");
																																prepStat.setString(1, newName);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 2) {
																																int newCoach;
																																System.out.print("New Coach-ID: ");
																																newCoach = rdr.nextInt();
																																prepStat = conn.prepareStatement("UPDATE Teams SET t_coachID = ? WHERE t_teamID = ?;");
																																prepStat.setInt(1, newCoach);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else;
																} System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminTeamStats(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs = s.executeQuery("SELECT ts_teamID, ts_touchdowns, ts_interception, ts_fumbles, ts_rushYards, ts_sacks, ts_superbowls, ts_resYards, ts_resYards FROM TeamStats;");
																System.out.println(String.format("%-10s %-5s %-5s %-8s %-8s %-8s %-10s %-8s %-15s", "Team ID", "Wins", "Loss", "Points", "Assists", "Blocks", "Rebounds", "Steals", "Superbowls"));
																while(rs.next()) { System.out.println(String.format("%-10s %-5s %-5s %-8s %-8s %-8s %-10s %-8s %-15s", rs.getInt("ts_teamID"), rs.getInt("ts_resYards"), rs.getInt("ts_resYards"), rs.getInt("ts_touchdowns"), rs.getInt("ts_interception"), rs.getInt("ts_fumbles"), rs.getInt("ts_rushYards"), rs.getInt("ts_sacks"), rs.getInt("ts_superbowls"))); }
																int editStats = 0;
																int typeEdit = 0;
																do {
																								System.out.print("Enter type of edit(1 = delete, 2 = update, 0 = quit): ");
																								typeEdit = rdr.nextInt();
																								if(typeEdit == 1 || typeEdit == 2) {
																																System.out.print("Team-ID to edit: ");
																																editStats = rdr.nextInt();
																																adminTeamStatsEdit(conn, editStats, typeEdit);
																								} else;
																} while(typeEdit != 0);
								}
								catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void adminTeamStatsEdit(Connection conn, int teamID, int typeEdit){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																PreparedStatement prepStat = null;
																if(typeEdit == 1) {
																								prepStat = conn.prepareStatement("DELETE FROM Teams WHERE t_teamID = ?;");
																								prepStat.setInt(1, teamID);
																								prepStat.executeUpdate();
																								prepStat = conn.prepareStatement("DELETE FROM TeamStats WHERE ts_teamID = ?;");
																								prepStat.setInt(1, teamID);
																								prepStat.executeUpdate();
																}
																else {
																								int attributeEditing = 0;
																								int newValue = 0;
																								System.out.print("Enter type editing (1 = Touchdowns, 2 = Interceptions, 3 = Fumbles, (4) = Rushyards, 5 = Sacks, 6 = Superbowls, 7 = Wins, 8 = Loss): ");
																								attributeEditing = rdr.nextInt();
																								if(attributeEditing != 6) {
																																System.out.print("New Value: ");
																																newValue = rdr.nextInt();
																								} if(attributeEditing == 1) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_touchdowns = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 2) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_interception = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 3) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_fumbles = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == (4)) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_rushYards = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 5) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_sacks = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 6) {
																																int coachTeam = 0;
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_superbowls = ts_superbowls + 1 WHERE ts_teamID = ?;");
																																prepStat.setInt(1, teamID);
																																prepStat.executeUpdate();
																																prepStat = conn.prepareStatement("UPDATE Coaches SET c_superbowls = c_superbowls + 1 WHERE c_coachID = ?;");
																																rs = s.executeQuery("SELECT c_coachID FROM Coaches WHERE c_teamName = " + teamID + ";");
																																while(rs.next()) {
																																								coachTeam = rs.getInt("c_coachID");
																																}
																																prepStat.setInt(1, coachTeam);
																																prepStat.executeUpdate();
																																System.out.println("Increased Team's & Coaches SuperBowls");
																								} else if(attributeEditing == 7) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_resYards = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else if(attributeEditing == 8) {
																																prepStat = conn.prepareStatement("UPDATE TeamStats SET ts_resYards = ? WHERE ts_teamID = ?;");
																																prepStat.setInt(1, newValue);
																																prepStat.setInt(2, teamID);
																																prepStat.executeUpdate();
																								} else;
																							}
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void coachMenu(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								boolean foundCoach = false;
								System.out.println("Enter coach ID: ");
								int currentCoach = rdr.nextInt();
								try{
																Statement s = conn.createStatement();
																ResultSet rs = s.executeQuery("SELECT c_coachID FROM Coaches;");
																while(rs.next()) {
																								if(rs.getInt("c_coachID") == currentCoach)
																																foundCoach = true;
																}

																if(!foundCoach) { System.out.println("!ERROR! Cannot find enter Coach-ID."); }
																else{
																								int currentTeam = 0;
																								String starter = "";
																								//Gets the team ID
																								rs = s.executeQuery("SELECT c_teamName FROM Coaches WHERE c_coachID = " + currentCoach + ";");
																								while(rs.next()) { currentTeam = rs.getInt("c_teamName"); }
																								System.out.println();
																								do {
																																System.out.println("Coach Menu");
																																//Prints out list of players for the coaches team and current starting position
																																rs = s.executeQuery("SELECT p_playerName, p_playerID, p_starter FROM Players WHERE p_teamID = " + currentTeam + ";");
																																System.out.println(String.format("%-10s %-23s %s", "PlayerID", "Name", "Starter"));
																																while(rs.next()) {
																																								if(rs.getInt("p_starter") == 1)
																																																starter = "S";
																																								else
																																																starter = "B";
																																								System.out.println(String.format("%-10d %-23s %s", rs.getInt("p_playerID"), rs.getString("p_playerName"), starter));
																																}
																																System.out.println();
																																System.out.println("(1)Update Starters on Team");
																																System.out.println("(2)Trade Player to another Team");
																																System.out.println("(0)Back");
																																userInput = rdr.nextInt();
																																if(userInput == 1) coachStarterUpdate(conn, currentTeam);
																																else if(userInput == 2) coachPlayerTrade(conn, currentTeam);
																																else;
																								} while(userInput != 0); System.out.println(); }
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void coachStarterUpdate(Connection conn, int currentTeam){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																PreparedStatement prepStat = null;
																ResultSet rs;
																int playerID = 0;
																int newPosition = 0;
																prepStat = conn.prepareStatement("UPDATE Players SET p_starter = ? WHERE p_playerID = ? AND p_teamID = ?;");
																System.out.print("Player-ID to Update: ");
																playerID = rdr.nextInt();
																rs = s.executeQuery("SELECT p_starter FROM Players WHERE p_teamID = " + currentTeam + " AND p_playerID = " + playerID + ";");
																while(rs.next()) {
																								if(rs.getInt("p_starter") == 1) newPosition = 0;
																								else newPosition = 1;
																} prepStat.setInt(1, newPosition);
																prepStat.setInt(2, playerID);
																prepStat.setInt(3, currentTeam);
																prepStat.executeUpdate();
																System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void coachPlayerTrade(Connection conn, int currentTeam){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																ResultSet rs;
																PreparedStatement playerTrade = null;
																int playerID = 0;
																int newTeamID = 0;
																String newTeam = "";
																playerTrade = conn.prepareStatement("UPDATE Players SET p_teamID = ?, p_starter = ? WHERE p_teamID = ? AND p_playerID = ?;");
																System.out.print("Player-ID to be traded: ");
																playerID = rdr.nextInt();
																rdr.nextLine();
																System.out.print("Team Name being traded to: ");
																newTeam = rdr.nextLine();
																rs = s.executeQuery("SELECT t_teamID FROM Teams WHERE t_name LIKE '%" + newTeam + "%';");
																while(rs.next()) { newTeamID = rs.getInt("t_teamID"); }
																playerTrade.setInt(1, newTeamID);
																playerTrade.setInt(2, 0);
																playerTrade.setInt(3, currentTeam);
																playerTrade.setInt((4), playerID);
																playerTrade.executeUpdate();
																System.out.println();
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userMenu(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								do {
																System.out.println("User Menu");
																System.out.println("(1)Games");
																System.out.println("(2)Players");
																System.out.println("(3)Teams & Coaches");
																System.out.println("(0)Back");
																userInput = rdr.nextInt();
																System.out.println();
																if(userInput == 1) userGamesMenu(conn);
																else if(userInput == 2) userPlayersMenu(conn);
																else if(userInput == 3) userTeamsMenu(conn);
																else;
								} while(userInput != 0);
								System.out.println();
}
public static void userGamesMenu(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								do {
																System.out.println("USER GAMES MENU");
																System.out.println("(1)All Games");
																System.out.println("(2)List of games on day");
																System.out.println("(0)Back");
																userInput = rdr.nextInt();
																System.out.println();
																if(userInput == 1) userPrintAllGames(conn);
																else if(userInput == 2) userPrintDayGames(conn);
																else;
								} while(userInput != 0);
								System.out.println();
}
public static void userPrintAllGames(Connection conn){
								try{
																Statement s = conn.createStatement();
																ResultSet rs = s.executeQuery("SELECT g_date, T(1)t_name AS homeTeam, g_homeScore, T(2)t_name AS awayTeam, g_awayScore FROM Teams T1, Teams T2, Games WHERE g_homeTeamName = T(1)t_teamID AND g_awayTeamName = T(2)t_teamID ORDER BY g_date DESC;");
																while(rs.next()) {
																								String homePoints = "(" + rs.getInt("g_homeScore") + ")";
																								String awayPoints = "(" + rs.getInt("g_awayScore") + ")";
																								System.out.println(String.format("%-12s %-25s %-5s %s %-5s %-25s", rs.getString("g_date"), rs.getString("awayTeam"), awayPoints, "VS.", homePoints, rs.getString("homeTeam")));
																}
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userPrintDayGames(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																String gameDate = "";
																int totalNumGames = 0;
																System.out.print("Enter game date(mm-dd-yyyy): ");
																gameDate = rdr.nextLine();
																ResultSet rs = s.executeQuery("SELECT g_date, T(1)t_name AS homeTeam, g_homeScore, T(2)t_name AS awayTeam, g_awayScore FROM Teams T1, Teams T2, Games WHERE g_homeTeamName = T(1)t_teamID AND g_awayTeamName = T(2)t_teamID AND g_date = '" + gameDate + "' ORDER BY g_date DESC;");
																while(rs.next()) {
																								String homePoints = "(" + rs.getInt("g_homeScore") + ")";
																								String awayPoints = "(" + rs.getInt("g_awayScore") + ")";
																								System.out.println(String.format("%-12s %-25s %-5s %s %-5s %-25s", rs.getString("g_date"), rs.getString("awayTeam"), awayPoints, "VS.", homePoints, rs.getString("homeTeam")));
																								totalNumGames++;
																} System.out.println("Total number of games: " + totalNumGames);
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userPlayersMenu(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								do {
																System.out.println("User Player - Menu");
																System.out.println("(1)All players");
																System.out.println("(2)Players from a team");
																System.out.println("(3)Specific player");
																System.out.println("(0)Back");
																userInput = rdr.nextInt();
																System.out.println();
																if(userInput == 1) userPrintAllPlayers(conn);
																else if(userInput == 2) userPrintTeamPlayers(conn);
																else if(userInput == 3) userPrintSpecPlayers(conn);
																else;
								} while(userInput != 0);
								System.out.println();
}
public static void userPrintAllPlayers(Connection conn){
								try{
																Statement s = conn.createStatement();
																ResultSet rs = s.executeQuery("SELECT p_playerName, t_name FROM Players, Teams WHERE p_teamID = t_teamID;");
																while(rs.next()) { System.out.println(String.format("%-23s %s", rs.getString("p_playerName"), rs.getString("t_name"))); }
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userPrintTeamPlayers(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																String teamName = "";
																int teamID = 0;
																System.out.print("Team Name: ");
																teamName = rdr.nextLine();
																ResultSet rs = s.executeQuery("SELECT t_teamID FROM Teams WHERE t_name LIKE '%" + teamName + "%';");
																while(rs.next()) { teamID = rs.getInt("t_teamID"); }
																rs = s.executeQuery("SELECT p_playerName, p_starter FROM Players, Teams WHERE p_teamID = t_teamID AND t_teamID = " + teamID + ";");
																while(rs.next()) {
																								String starter = " ";
																								if(rs.getInt("p_starter") == 1) starter = "S";
																								System.out.println(String.format("%-23s %s", rs.getString("p_playerName"), starter));
																}
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userPrintSpecPlayers(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																String playerName = "";
																System.out.print("Player Name: ");
																playerName = rdr.nextLine();
																ResultSet rs = s.executeQuery("SELECT p_playerName, t_name, p_starter FROM Players, Teams WHERE p_teamID = t_teamID AND p_playerName LIKE '%" + playerName + "%';");
																while(rs.next()) {
																								String starter = " ";
																								if(rs.getInt("p_starter") == 1) starter = "S";
																								System.out.println(String.format("%-23s %s %10s", rs.getString("p_playerName"), rs.getString("t_name"), starter));
																}
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userTeamsMenu(Connection conn){
								Scanner rdr = new Scanner(System.in);
								int userInput = 0;
								do {
																System.out.println("User - Teams/Coaches Menu");
																System.out.println("(1)All teams & coaches");
																System.out.println("(2)Specific Team");
																System.out.println("(0)Back");
																userInput = rdr.nextInt();
																System.out.println();
																if(userInput == 1) userTeamPrint(conn);
																else if(userInput == 2) userTeamsSpec(conn);
																else;
								} while(userInput != 0); System.out.println();
}
public static void userTeamPrint(Connection conn){
								try{
																Statement s = conn.createStatement();
																ResultSet rs = s.executeQuery("SELECT t_name, c_name FROM Teams, Coaches WHERE t_coachID = c_coachID;");
																while(rs.next()) { System.out.println(String.format("%-23s %s", rs.getString("t_name"), rs.getString("c_name"))); }
								} catch(SQLException e) {System.err.println(e.getMessage());}
}
public static void userTeamsSpec(Connection conn){
								try{
																Scanner rdr = new Scanner(System.in);
																Statement s = conn.createStatement();
																String teamName = "";
																System.out.print("Team's Name: ");
																teamName = rdr.nextLine();
																ResultSet rs = s.executeQuery("SELECT t_name, c_name FROM Teams, Coaches WHERE t_coachID = c_coachID AND t_name LIKE '%" + teamName + "%';");
																while(rs.next()) { System.out.println(String.format("%-23s %s", rs.getString("t_name"), rs.getString("c_name"))); }
								} catch(SQLException e) {System.err.println(e.getMessage()); }
 }
}
