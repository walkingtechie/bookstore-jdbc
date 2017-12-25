package com.walking.techie.service;


import com.walking.techie.connection.DataBaseConnection;
import com.walking.techie.model.Book;
import com.walking.techie.model.Chapter;
import com.walking.techie.model.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookStoreService implements BookStore {
    @Override
    public void persistObjectGraph(Book book) {
        try {
            Connection connection = DataBaseConnection.getDBConnection();

            // store publisher details in PUBLISHER table
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO PUBLISHER (CODE, PUBLISHER_NAME) VALUES (?,?)");
            preparedStatement.setString(1, book.getPublisher().getCode());
            preparedStatement.setString(2, book.getPublisher().getName());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // store book details in BOOK table
            preparedStatement = connection.prepareStatement("INSERT INTO BOOK (ISBN, BOOK_NAME, PUBLISHER_CODE) VALUES (?,?,?)");
            preparedStatement.setString(1, book.getIsbn());
            preparedStatement.setString(2, book.getName());
            preparedStatement.setString(3, book.getPublisher().getCode());
            preparedStatement.executeUpdate();
            preparedStatement.close();

            // store chapter details in CHAPTER table
            preparedStatement = connection.prepareStatement("INSERT INTO CHAPTER (BOOK_ISBN, CHAPTER_NUM, TITLE) VALUES (?,?,?)");
            for (Chapter chapter : book.getChapters()) {
                preparedStatement.setString(1, book.getIsbn());
                preparedStatement.setInt(2, chapter.getChapterNumber());
                preparedStatement.setString(3, chapter.getTitle());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Book retrieveObjectGraph(String isbn) {
        Book book = null;
        // fetch book detail from DB
        try {
            Connection connection = DataBaseConnection.getDBConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM BOOK, PUBLISHER WHERE BOOK.PUBLISHER_CODE=PUBLISHER.CODE AND BOOK.ISBN=?");
            preparedStatement.setString(1, isbn);
            ResultSet resultSet = preparedStatement.executeQuery();
            book = new Book();
            if (resultSet.next()) {
                book.setIsbn(resultSet.getString("ISBN"));
                book.setName(resultSet.getString("BOOK_NAME"));

                // setting publisher of Book
                Publisher publisher = new Publisher();
                publisher.setName(resultSet.getString("PUBLISHER_NAME"));
                publisher.setCode(resultSet.getString("CODE"));
                book.setPublisher(publisher);
            }
            // close resultset
            resultSet.close();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("SELECT * FROM CHAPTER WHERE CHAPTER.BOOK_ISBN = ?");
            preparedStatement.setString(1, isbn);
            resultSet = preparedStatement.executeQuery();
            List<Chapter> chapters = new LinkedList<>();
            while (resultSet.next()) {
                Chapter chapter = new Chapter();
                chapter.setChapterNumber(resultSet.getInt("CHAPTER_NUM"));
                chapter.setTitle(resultSet.getString("TITLE"));
                chapters.add(chapter);
            }
            //close result set
            resultSet.close();
            //close preparedStatement
            preparedStatement.close();
            book.setChapters(chapters);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return book;
    }
}
