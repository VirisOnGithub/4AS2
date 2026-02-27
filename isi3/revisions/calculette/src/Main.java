public class Main {
    static void main() {
        Calculette cal = new Calculette();
        System.out.println(cal.calculate(1, 2, new Add()));
    }
}
