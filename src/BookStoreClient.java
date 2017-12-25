import com.walking.techie.model.Book;
import com.walking.techie.model.Chapter;
import com.walking.techie.model.Publisher;
import com.walking.techie.service.BookStoreService;

import java.util.LinkedList;
import java.util.List;

public class BookStoreClient {

    public static void main(String[] args) {
        BookStoreService service = new BookStoreService();


        // All Book information
        Publisher publisher = new Publisher("TECH", "Java Publications");
        Chapter chapter1 = new Chapter("Introduction of Hibernate", 1);
        Chapter chapter2 = new Chapter("Introduction of JPA and Hibernate", 2);
        List<Chapter> chapters = new LinkedList<>();
        chapters.add(chapter1);
        chapters.add(chapter2);
        Book book = new Book("123456789", "Java Persistence with Hibernate", publisher, chapters);
        // save book object graph in DB
        //service.persistObjectGraph(book);

        // retrieve Book object graph from DB based on ISBN
        book = service.retrieveObjectGraph("123456789");
        // print book information on console
        System.out.println(book);
    }
}
