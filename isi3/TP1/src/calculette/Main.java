package calculette;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Saisissez l'operateur");
		String c = sc.nextLine();
		System.out.println("Saisissez le 1er nombre");//saisir avec , et pas . 
		Float c1 = sc.nextFloat();
		System.out.println("Saisissez le 2nd nombre");
		Float c2 = sc.nextFloat();

		Calculette calculator = new Calculette();
		float result = calculator.calculate(c1, c2, c);

		System.out.println("Résultat"+result);

  }
}
