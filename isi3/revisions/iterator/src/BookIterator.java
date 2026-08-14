import java.util.List;

public class BookIterator implements Iterator<Book>{
    private List<Book> books;
    private int position = 0;

    public BookIterator(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean hasNext() {
        return position < books.size();
    }

    @Override
    public Book next() {
        if (!hasNext()) throw new IllegalStateException("No more books in the collection.");
        return books.get(position++);
    }
}
