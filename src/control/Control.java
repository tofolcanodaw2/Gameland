package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import modelo.JDBCSingleton;
import modelo.UsuarioCRUD;
import modelo.entidad.Usuario;
import vista.html.HtmlConstructor;
import vista.html.LoginPage;
import vista.html.MainPage;
import vista.html.RegistroPage;

public class Control {

	public static String getFileName(Part part) {
		for (String content : part.getHeader("content-disposition").split(";")) {
			if (content.trim().startsWith("filename"))
				return content.substring(content.indexOf("=") + 2, content.length() - 1);
		}
		return "default.jpeg";
	}
	
	/**
	 * Método que devuelve el nombre de usuario si está logeado
	 * 
	 * @param request request del servlet
	 * @return El nombre de usuario si se ha logeado o null si no.
	 */
	public static String getLoggedUser(HttpServletRequest request) {
		String user = null;

		// Comprobamos si tenemos una sesión y obtenemos su nombre de usuario.
		HttpSession session = request.getSession(false);

		if (session != null) {
			user = (String) session.getAttribute("usuario");
		}

		return user;
	}

	public static RegistroPage crearPagRegistro(String excepcion) {
		RegistroPage reg = new RegistroPage();
		if(excepcion != null) {
			reg.setExcepcion(excepcion);
		}
		return reg;
	}

	public static void printResponse(HtmlConstructor pagina, HttpServletResponse response) throws IOException, NullPointerException{
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		writer.print(pagina.toString());
	}

	public static void getConexion(String string, String string2)
			throws ClassNotFoundException, SQLException, NamingException {
		JDBCSingleton.setConnection(string, string2);
	}

	public static ResultSet getUsuariosDeBD() throws SQLException {
		ResultSet nombres = null;
		nombres = UsuarioCRUD.selectTodos();
		return nombres;
	}

	public static boolean guardarUsuarioEnBD(String nombre, String usuario, String password, String fPerfil)
			throws SQLException {
		Usuario u = new Usuario(nombre, usuario, password, fPerfil);
		try {
			UsuarioCRUD.insert(u);
			return false;
		} catch (SQLException e) {

			e.printStackTrace();
			return true;
		}
	}

	public static MainPage crearPagMain(String registrado, boolean esAdmin, String foto) {
		MainPage pag = new MainPage(registrado,esAdmin,foto);
		return pag;
	}

	public static LoginPage crearPagLogin(String error) {
		LoginPage logpag = new LoginPage();
		if(error!=null) {
			logpag.setExcepcion(error);
		}
		return logpag;
	}
	
	public static String fechaActual() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		return formatter.format(date);
	}
	
	public static <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
	    if (c == null) {
	    	return Collections.emptyList();
	    }
	    List<T> list = new ArrayList<T>(c);
	    if (pageSize == null || pageSize <= 0 || pageSize > list.size()) {
	    	pageSize = list.size();
	    }
	    int numPages = (int) Math.ceil((double)list.size() / (double)pageSize);
	    List<List<T>> pages = new ArrayList<List<T>>(numPages);
	    for (int pageNum = 0; pageNum < numPages;) {
	    	pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
	    }
	        
	    return pages;
	}

}
