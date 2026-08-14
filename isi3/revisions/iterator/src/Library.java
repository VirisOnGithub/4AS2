import java.util.ArrayList;
import java.util.List;

public class Library implements BookCollection {
    private List<Book> books = new ArrayList<>();

    @Override
    public void addBook(Book book) {
        books.add(book);
    }

    @Override
    public Iterator<Book> createIterator() {
        return new BookIterator(books);
    }
}

void main() {
    Library library = new Library();
    library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald"));
    library.addBook(new Book("To Kill a Mockingbird", "Harper Lee"));
    library.addBook(new Book("1984", "George Orwell"));

    Iterator<Book> iterator = library.createIterator();
    while (iterator.hasNext()) {
        System.out.println(iterator.next());
    }
}