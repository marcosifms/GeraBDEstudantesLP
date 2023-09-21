package view;


import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Marcos
 */
public class GerarBDEstudantes {

    List listaBD;

    public void lerArquivo() {
        listaBD = new ArrayList();
        BufferedReader br = null;
        Path currentRelativePath = Paths.get("");
        String diretorioAtual = currentRelativePath.toAbsolutePath().toString();
        System.out.println(diretorioAtual);
        try {
            String arquivo = diretorioAtual + "\\src\\listaEstudantes2023.txt";
            br = new BufferedReader(new FileReader(arquivo));
            String linha;
            while ((linha = br.readLine()) != null) {
                //System.out.println(linha);
                //while  (linha.contains(" "))
                String[] quebra = linha.split(" ");
                linha = quebra[0] + "_" + quebra[quebra.length - 1];
//                while (linha.split(" ").length != 2) {
//                    linha = linha.substring(linha.inde)
//                }
                //System.out.println(linha.split(" ").length);
                //System.out.println(linha);
                listaBD.add(linha.toLowerCase());
            }
            Collections.sort(listaBD);
            for (int i = 0; i < listaBD.size(); i++) {
                System.out.println(listaBD.get(i));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GerarBDEstudantes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GerarBDEstudantes.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(GerarBDEstudantes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void gerarBD() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://10.7.0.51:33062/db_marcos_vilhanueva";//"jdbc:mysql://localhost/aulas";
            String user = "root";
            String passwd = "outrasenha###$$$"; //""
            Connection cnt = (Connection) DriverManager.getConnection(url, user, passwd);
            Statement statement = (Statement) cnt.createStatement();
            //statement.executeQuery("show databases like \"db%\";");
            String username = "";
            for (int i = 0; i < listaBD.size(); i++) {
                //statement.executeUpdate("create database "+listaBD.get(i));
                username = (String) listaBD.get(i);
                System.out.println("username:"+username);
                System.out.println("CREATE USER IF NOT EXISTS '" + username + "' @'%' IDENTIFIED BY '" + username + "'");
                statement.executeUpdate("CREATE USER IF NOT EXISTS '" + username + "' @'%' IDENTIFIED BY '" + username + "'");
                //"db_" + 
                System.out.println("GRANT USAGE ON . TO '" + username + "' @'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0");
                statement.executeUpdate("GRANT USAGE ON *.* TO '" + username + "' @'%' REQUIRE NONE WITH MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0");

                System.out.println("CREATE DATABASE IF NOT EXISTS `db_" + username + "`");
                statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `db_" + username + "`");

                System.out.println("GRANT ALL PRIVILEGES ON `db_" + username + "`.* TO '" + username + "' @'%')");
                statement.executeUpdate("GRANT ALL PRIVILEGES ON `db_" + username + "`.* TO '" + username + "'@'%'");
            }

//            for (int i = 0; i < listaBD.size(); i++) {
//                statement.executeUpdate("create database "+listaBD.get(i));
//                System.out.println(listaBD.get(i));
//            }
//            for (int i = 0; i < listaBD.size(); i++) {
//                statement.executeUpdate("drop database db_"+listaBD.get(i));
//                System.out.println(listaBD.get(i));
//            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GerarBDEstudantes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(GerarBDEstudantes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

        GerarBDEstudantes gerarBDEstudantes = new GerarBDEstudantes();
        gerarBDEstudantes.lerArquivo();
        gerarBDEstudantes.gerarBD();
    }
}
