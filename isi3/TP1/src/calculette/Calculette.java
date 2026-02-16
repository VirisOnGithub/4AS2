package calculette;


public class Calculette
{
    public float calculate(float a, float b, String ope){
        float result;
        switch(ope) {
            case "+":
                result = a + b;
                break;
            case "-":
                result = a - b;
                break;

            default:
                System.out.println("L'opérateur "+ope+" n'est pas reconnu.");
                result = 0;
        }
        return result;
    }
}
