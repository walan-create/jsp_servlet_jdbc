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
import java.util.Optional;

@WebServlet (name = "EditarSociosServlet", value = "/EditarSociosServlet")
public class EditarSociosServlet extends HttpServlet {

    private SocioDAO socioDAO = new SocioDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    //1. Recoger el id del socio a editar
    //2. Llamar al DAO para recuperar el socio
    //3. Redirigir a la vista de edición de socios

        RequestDispatcher dispatcher = null;

        String codigoStr = request.getParameter("codigo");

        Integer codigo = null;

        try {
            codigo = Integer.parseInt(codigoStr);
        }catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        if(codigo != null) {

            Optional socioOptional = socioDAO.find(codigo);

            if(socioOptional.isPresent()) {

                Socio socio = (Socio) socioOptional.get();
                this.socioDAO.update(socio);

                List<Socio> listado = this.socioDAO.getAll();
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp");

                request.setAttribute("listado", listado);
                dispatcher.forward(request, response);
            }
        }else {
            response.sendRedirect("ListarSociosServlet");
        }

    }
}
