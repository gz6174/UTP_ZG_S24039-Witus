package zad1;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Database
{

    private TravelData travelData;
    private String dbName = "UTP4_GZ_S24039_DB";
    private String tableName ="tblOffers";
    private String url;

    Database(String url, TravelData travelData)      //Constructor
    {
        this.travelData = travelData;
        this.url = url;
        try {
            Connection connection=connConnectToDatabase();
            Statement statement = connection.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS " + this.dbName);
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS " + this.dbName + "." + this.tableName + " (" +
                    "Kod varchar(250), " +
                    "Kraj varchar(250), " +
                    "DataOd varchar(250), " +
                    "DataDo varchar(250), " +
                    "Dokad varchar(250), " +
                    "Cena varchar(250), " +
                    "Waluta varchar(250));");
            statement.close();
            connection.close();
        } catch(SQLException exc) {
            System.out.println("Nieudane połączenie z " + url);
            System.out.println(exc);
            System.exit(1);
        }
    }
    /*
    public void create()
    {
        //wpisanie do bazy wszystkich ofert, wczytanych z plików
        List<String> lstTravelOffers = new LinkedList<>(travelData.getRows());

        try {
            Connection connection=connConnectToDatabase();
            PreparedStatement preparedStatement=connection.prepareStatement("INSERT INTO " + this.dbName + "."+ this.tableName + " VALUES(?,?,?,?,?,?,?)");
            for (int i=0; i < lstTravelOffers.size(); i++)
            {
                String[] arrLine = lstTravelOffers.get(i).split("\t");     //split line into array
                preparedStatement.setString(1, arrLine[0]);         //IdKraju
                preparedStatement.setString(2, arrLine[1]);         //Kraj
                preparedStatement.setString(3, arrLine[2]);         //DataWyjazdu
                preparedStatement.setString(4, arrLine[3]);         //DataPowrotu
                preparedStatement.setString(5, arrLine[4]);         //Miejsce
                preparedStatement.setString(6, arrLine[5]);         //Cena
                preparedStatement.setString(7, arrLine[6]);         //SymbolWaluty
                preparedStatement.execute();
            }
            preparedStatement.close();
            connection.close();
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + url);
            System.out.println(exc);
            System.exit(1);
        }
    }
    public void createDB()
    {
        //W poleceniu zadania, metoda ma się nazywać createDB(), w metodzie main jest create()-> stąd obie wersje
        create();
    }
    public void showGui()
    {
        //otwarcie GUI z tabelą, pokazującą wczytane oferty

    }
    */

    private Connection connConnectToDatabase()
    {
        //make connection to database
        String strUsername = System.getProperty("user.name");           //Get Window's username
        Connection connResult=null;
        try {
            if(!strUsername.equals("Hronic"))
            {
                connResult =  DriverManager.getConnection(url);
            } else {
                connResult =  DriverManager.getConnection(url, "root", "MySqlPassword");
            }
        } catch(SQLException exc) {  // nieudane połączenie
            System.out.println("Nieudane połączenie z " + url);
            System.out.println(exc);
            System.exit(1);
        }
        return connResult;
    }
}