package org.iesvdm.jsp_servlet_jdbc.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAO;
import org.iesvdm.jsp_servlet_jdbc.dao.SocioDAOImpl;
import org.iesvdm.jsp_servlet_jdbc.model.Socio;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "BorrarSociosServlet", value = "/BorrarSociosServlet")
public class BorrarSociosServlet extends HttpServlet {

    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1. Recoger el id del socio a borrar
        //2. Llamar al DAO para borrar el socio
        //3. Redirigir a la lista de socios

        RequestDispatcher dispatcher = null;

        String codigoStr = request.getParameter("codigo");

        Integer codigo = null;
        try {
            codigo = Integer.parseInt(codigoStr);

        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        if (codigo != null) {

            this.socioDAO.delete(codigo);
            //REDIRECCION DEL LADO DEL NAVEGADOR
            //response.sendRedirect("ListarSociosServlet");

            //REDIRECCION DEL LADO DEL SERVIDOR
            List<Socio> listado = this.socioDAO.getAll();
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");

            request.setAttribute("listado", listado);
            dispatcher.forward(request, response);

        } else {
            response.sendRedirect("ListarSociosServlet");
        }


    }

}
