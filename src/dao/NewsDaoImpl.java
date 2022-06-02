package dao;

import java.sql.Connection;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.News;
import utils.JDBCUtils;

/**
 * This DAO class provides CRUD database operations for the table todos in the
 * database.
 * 
 * @author Ramesh Fadatare
 *
 */

public class NewsDaoImpl implements NewsDao {

	private static final String INSERT_NEWS_SQL = "INSERT INTO news"
			+ "  (title, username, description, target_date,  is_done) VALUES " + " (?, ?, ?, ?, ?);";

	private static final String SELECT_NEWS_BY_ID = "select id,title,username,description,target_date,is_done from news where id =?";
	private static final String SELECT_ALL_NEWS = "select * from news";
	private static final String DELETE_NEWS_BY_ID = "delete from news where id = ?;";
	private static final String UPDATE_NEWS = "update news set title = ?, username= ?, description =?, target_date =?, is_done = ? where id = ?;";

	public NewsDaoImpl() {
	}

	@Override
	public void insertNews(News news) throws SQLException {
		System.out.println(INSERT_NEWS_SQL);
		// try-with-resource statement will auto close the connection.
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEWS_SQL)) {
			preparedStatement.setString(1, news.getTitle());
			preparedStatement.setString(2, news.getUsername());
			preparedStatement.setString(3, news.getDescription());
			preparedStatement.setDate(4, JDBCUtils.getSQLDate(news.getTargetDate()));
			preparedStatement.setBoolean(5, news.getStatus());
			System.out.println(preparedStatement);
			preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
	}

	@Override
	public News selectNews(long newsId) {
		News news = null;
		// b 1: ket noi co so du lieu
		try (Connection connection = JDBCUtils.getConnection();
				// b 2:Create a statement using connection object
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NEWS_BY_ID);) {
			preparedStatement.setLong(1, newsId);
			System.out.println(preparedStatement);
			// b2 3: Execute the query or update query
			ResultSet rs = preparedStatement.executeQuery();

			// b 4: truyen cac doi tuong vao resoure
			while (rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				String username = rs.getString("username");
				String description = rs.getString("description");
				LocalDate targetDate = rs.getDate("target_date").toLocalDate();
				boolean isDone = rs.getBoolean("is_done");
				news = new News(id, title, username, description, targetDate, isDone);
			}
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
		return news;
	}

	@Override
	public List<News> selectAllNews() {

		List<News> news = new ArrayList<>();

		//ket noi co so du lieu 
		try (Connection connection = JDBCUtils.getConnection();

				// 
				PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_NEWS);) {
			System.out.println(preparedStatement);
			// Step 3: kich hoat thu vien cap nhat que ry
			ResultSet rs = preparedStatement.executeQuery();

			// Step 4: Process the ResultSet object.
			while (rs.next()) {
				long id = rs.getLong("id");
				String title = rs.getString("title");
				String username = rs.getString("username");
				String description = rs.getString("description");
				LocalDate targetDate = rs.getDate("target_date").toLocalDate();
				boolean isDone = rs.getBoolean("is_done");
				news.add(new News(id, title, username, description, targetDate, isDone));
			}
		} catch (SQLException exception) {
			JDBCUtils.printSQLException(exception);
		}
		return news;
	}

	@Override
	public boolean deleteNews(int id) throws SQLException {
		boolean rowDeleted;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(DELETE_NEWS_BY_ID);) {
			statement.setInt(1, id);
			rowDeleted = statement.executeUpdate() > 0;
		}
		return rowDeleted;
	}

	@Override
	public boolean updateNews(News news) throws SQLException {
		boolean rowUpdated;
		try (Connection connection = JDBCUtils.getConnection();
				PreparedStatement statement = connection.prepareStatement(UPDATE_NEWS);) {
			statement.setString(1, news.getTitle());
			statement.setString(2, news.getUsername());
			statement.setString(3, news.getDescription());
			statement.setDate(4, JDBCUtils.getSQLDate(news.getTargetDate()));
			statement.setBoolean(5, news.getStatus());
			statement.setLong(6, news.getId());
			rowUpdated = statement.executeUpdate() > 0;
		}
		return rowUpdated;
	}
}
