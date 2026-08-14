public record Book(String title, String author) {
    @Override
    public String toString() {
        return String.format("%s by %s", title, author);
    }
}