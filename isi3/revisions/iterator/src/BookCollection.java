public interface BookCollection {
    void addBook(Book book);
    Iterator<Book> createIterator();
}
