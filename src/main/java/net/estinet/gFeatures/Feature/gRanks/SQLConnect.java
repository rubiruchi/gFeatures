package net.estinet.gFeatures.Feature.gRanks;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.estinet.gFeatures.API.Logger.Debug;
import org.bukkit.Bukkit;

/*
gFeatures
https://github.com/EstiNet/gFeatures

   Copyright 2018 EstiNet

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

public class SQLConnect {


    public static void Connect(String query) {
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
    }

    public static List<String> ConnectReturn(String query) {
        List<String> array = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        ResultSet result;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            result = st.executeQuery(query);
            result.beforeFirst();
            for (; !result.isLast(); ) {
                result.next();
                try {
                    array.add(result.getString(1));
                    array.add(result.getString(2));
                } catch (Exception e) {
                    array.add(result.getString(1));
                }
            }
            return array;
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
        return null;
    }

    public static List<String> ConnectReturnRanks(String query) {
        List<String> array = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        ResultSet result = null;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            result = st.executeQuery(query);
            result.beforeFirst();

            for (; !result.isLast(); ) {
                result.next();
                try {
                    array.add(result.getString("Name"));
                    array.add(result.getString("Prefix"));
                } catch (Exception e) {
                    array.add(result.getString(1));
                }
            }
            return array;
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
        return null;
    }

    public static List<String> ConnectReturnPeople(String query) {
        List<String> array = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        ResultSet result = null;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            result = st.executeQuery(query);
            result.beforeFirst();

            for (; !result.isLast(); ) {
                result.next();
                try {
                    array.add(result.getString("UUID"));
                    array.add(result.getString("Rank"));
                } catch (Exception e) {
                    array.add(result.getString(1));
                }
            }
            return array;
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
        return null;
    }

    public static List<String> ConnectReturnPerm(String query) {
        List<String> array = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        ResultSet result = null;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            result = st.executeQuery(query);
            result.beforeFirst();

            for (; !result.isLast(); ) {
                result.next();
                try {
                    array.add(result.getString("Perm"));
                    array.add(result.getString("Rank"));
                } catch (Exception e) {
                    array.add(result.getString(1));
                }
            }
            return array;
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
        return null;
    }

    public static List<String> ConnectReturnInherit(String query) {
        List<String> array = new ArrayList<>();
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        ResultSet result = null;
        try {
            con = DriverManager.getConnection(gRanks.url, gRanks.username, gRanks.password);
            st = con.createStatement();
            result = st.executeQuery(query);
            result.beforeFirst();

            for (; !result.isLast(); ) {
                result.next();
                try {
                    array.add(result.getString("Inherit"));
                    array.add(result.getString("Rank"));
                } catch (Exception e) {
                    array.add(result.getString(1));
                }
            }
            return array;
        } catch (SQLException ex) {
            Debug.print(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                Debug.print(ex.getMessage());
            }
        }
        return null;
    }

    public static boolean checkOnline(String url, String user, String password) {
        java.sql.Connection con = null;
        java.sql.Statement st = null;
        ResultSet rs = null;
        Bukkit.getLogger().info("Establishing Connection to " + url);
        try {
            con = DriverManager.getConnection(url, user, password);
            Bukkit.getLogger().info("Connected!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            Bukkit.getLogger().info("Aww... Unable to connect to MySQL Server " + url);
            Bukkit.getLogger().info("Make sure you check if the port is correct!");

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Bukkit.getLogger().info("Aww... Unable to connect to MySQL Server " + url);
                Bukkit.getLogger().info("Make sure you check if the port is correct!");
            }
        }
        return false;
    }

    public static String toURL(String port, String address, String table) {
        return "jdbc:mysql://" + address + ":" + port + "/" + table + "?autoReconnect=true&useSSL=false";
    }

}
