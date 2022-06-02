package web;

import java.io.IOException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.NewsDao;
import dao.NewsDaoImpl;
import model.News;



@WebServlet("/")
public class NewsController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private NewsDao newsDAO;

	public void init() {
		newsDAO = new NewsDaoImpl();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertNews(request, response);
				break;
			case "/delete":
				deleteNews(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateNews(request, response);
				break;
			case "/list":
				listNews(request, response);
				break;
			default:
				RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
				dispatcher.forward(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listNews(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<News> listNews = newsDAO.selectAllNews();
		request.setAttribute("listNews", listNews);
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		News existingNews = newsDAO.selectNews(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo/todo-form.jsp");
		request.setAttribute("news", existingNews);
		dispatcher.forward(request, response);

	}

	private void insertNews(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		
		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");
		
		/*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"),df);*/
		
		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		News newNews = new News(title, username, description, LocalDate.now(), isDone);
		newsDAO.insertNews(newNews);
		response.sendRedirect("list");
	}

	private void updateNews(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		
		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");
		//DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"));
		
		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		News updateNews = new News(id, title, username, description, targetDate, isDone);
		
		newsDAO.updateNews(updateNews);
		
		response.sendRedirect("list");
	}

	private void deleteNews(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		newsDAO.deleteNews(id);
		response.sendRedirect("list");
	}
}
