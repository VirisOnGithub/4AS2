public class Main {
    static void main() {
        Singleton first = Singleton.getInstance("first");
        Singleton second = Singleton.getInstance("second");

        System.out.println("First is " + first.getValue());
        System.out.println("Second is " + second.getValue());
    }
}
