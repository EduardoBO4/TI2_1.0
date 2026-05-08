package dao;

import java.sql.*;

public class DAO {
        protected Connection conexao;
        
        public DAO() {
                conexao = null;
        }
        
        public boolean conectar() {
                String driverName = "org.postgresql.Driver";
                String serverName = System.getenv("PGHOST") != null ? System.getenv("PGHOST") : "localhost";
                String mydatabase = System.getenv("PGDATABASE") != null ? System.getenv("PGDATABASE") : "ResolveAquiDB";
                int porta = System.getenv("PGPORT") != null ? Integer.parseInt(System.getenv("PGPORT")) : 5432;
                String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
                String username = System.getenv("PGUSER") != null ? System.getenv("PGUSER") : "postgres";
                String password = System.getenv("PGPASSWORD") != null ? System.getenv("PGPASSWORD") : "postgres";
                boolean status = false;

                try {
                        Class.forName(driverName);
                        conexao = DriverManager.getConnection(url, username, password);
                        status = (conexao == null);
                        System.out.println("Conexão efetuada com o postgres!");
                } catch (ClassNotFoundException e) { 
                        System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
                } catch (SQLException e) {
                        System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
                }

                return status;
        }
        
        public boolean close() {
                boolean status = false;
                
                try {
                        conexao.close();
                        status = true;
                } catch (SQLException e) {
                        System.err.println(e.getMessage());
                }
                return status;
        }
}