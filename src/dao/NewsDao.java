package dao;

import java.sql.SQLException;

import java.util.List;

import model.News;

public interface NewsDao {

	void insertNews(News news) throws SQLException;

	News selectNews(long newsId);

	List<News> selectAllNews();

	boolean deleteNews(int id) throws SQLException;

	boolean updateNews(News news) throws SQLException;



}