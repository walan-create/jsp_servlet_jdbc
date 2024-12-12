package org.iesvdm.jsp_servlet_jdbc.servlet;


import jakarta.servlet.DispatcherType;
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

    //Este metodo obtiene el socio a editar y lo envia al formulario de edicion
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1. Recoger el id del socio a editar
        //2. Llamar al DAO para recuperar el socio
        //3. Redirigir a la vista de edición de socios

        RequestDispatcher dispatcher = null;

        //Tomamos el codigo
        String codigoStr = request.getParameter("codigo");
        Integer codigo = null;

        //Verificamos que sea un entero
        try {
            codigo = Integer.parseInt(codigoStr);
        }catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        if(codigo != null) {

            Optional<Socio> socioOptional = this.socioDAO.find(codigo);

            if(socioOptional.isPresent()) {

                Socio editarSocio = (Socio) socioOptional.get();
                //Metemos como atributo el socio listo para mandarlo al formulario de edicion
                request.setAttribute("editarSocio",editarSocio);

            }else{
                request.setAttribute("error","No existe el socio con el codigo: "+codigo);
                request.getRequestDispatcher("WEB-INF/jsp/listadoSociosB.jsp").forward(request,response);
            }

            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request,response);


        }else {
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request,response);
        }

    }


    @Override
    protected void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        // Creamos un objeto RequestDispatcher para luego redirigir a la página de listado de socios
        RequestDispatcher dispatcher = null;

        //Recuperamos el id del socio a editar y lo validamos
        int numSocio=-1;

        try {
            numSocio = Integer.parseInt(request.getParameter("codigo"));
        }catch (NumberFormatException n){
            request.setAttribute("error","ID socio no valido");
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request,response);
            return;
        }

        //Verificacion el socio con ese ID existe?
        Optional<Socio> optionalSocio=this.socioDAO.find(numSocio);
        if(!optionalSocio.isPresent()){
            request.setAttribute("error","El socio con el id: "+numSocio+" no existe");
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp");
            dispatcher.forward(request,response);
            return;
        }

        //Validar datos socio
        Optional<Socio> socioValido= UtilServlet.validaGrabar(request);
        if(socioValido.isPresent()){
            Socio socioEditar = socioValido.get();
            socioEditar.setSocioId(numSocio);

            //Actualizamos los datos del socio ya validado
            this.socioDAO.update(socioEditar);

            //Redireccion a la pagina de listado
            List<Socio> listado = this.socioDAO.getAll();
            request.setAttribute("listado",listado);
            request.getRequestDispatcher("/WEB-INF/jsp/listadoSociosB.jsp").forward(request,response);
        }else{
            request.setAttribute("error","Validacion de datos erronea");
            request.setAttribute("socio",optionalSocio.get());
            request.getRequestDispatcher("/WEB-INF/jsp/formularioEditarSocio.jsp").forward(request,response);
        }

    }

}
