package Servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Fabián
 */
@WebServlet(urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException { 
        
    }

    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String nombre_usuario = request.getParameter("usuario");
        String password = request.getParameter("pass");
        String err;

        if (("".equals(nombre_usuario)) || ("".equals(password))) {
            err = "Complete todos los datos.";
            String url = response.encodeRedirectURL("nuevo.html");
            response.sendRedirect(url);
        } else {
            String sql = "SELECT CAST(COUNT(1) AS BIT) AS existe FROM usuarios WHERE (\"usuario\" = '" + nombre_usuario + "' AND \"passwd\" = '" + password + "');";
            Connection connection = null;
            int esValido = -1;

            try {
                connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/template_postgis20", "postgres", "postgres");
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(sql);

                while (rs.next()) {
                    System.out.print("Column 1 returned ");
                    System.out.println(rs.getString(1));
                    esValido = Integer.parseInt(rs.getString(1));
                }
                rs.close();
                st.close();

                PrintWriter pww = response.getWriter();
                if (esValido == 1) {
                    response.setContentType("text/html");
                    pww.write("<h1>Bienvenido, " + nombre_usuario + "</h1>");
                    HttpSession session = request.getSession();
                    session.setAttribute("usuario", nombre_usuario);
                    String url = response.encodeRedirectURL("main.html");
                    response.sendRedirect(url);
                } else {
                    pww.write("Fallo en el login\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
