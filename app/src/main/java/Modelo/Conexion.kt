package Modelo

import java.sql.DriverManager

class Conexion {

    fun cadenaConexion(): java.sql.Connection? {
        try {
            val ip = "jdbc:oracle:thin:@192.168.1.207:1521:xe"
            val usuario = "SYSTEM"
            val contrasena = "ITR2024"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion
        }
        catch (e: Exception){
            println("ERROR: $e")
            return null
        }
    }
}