package name.swyan.speechcalculator.calculator;

public class Calculator {

	public Calculator() {
	}

	public float plus(float n1, float n2) {
		return n1 + n2;
	}

	public float minus(float n1, float n2) {
		return n1 - n2;
	}

	public float multiply(float n1, float n2) {
		return n1 * n2;
	}

	public float divide(float n1, float n2) {
		return n1 / n2;
	}

	public float compute(float n1, float n2, String s) {
		float num;
		switch (s) {
			case "plus" :   num = n1 + n2;
			                break;
			case "minus" :  num = n1 - n2;
			                break;
			case "times" :  num = n1 * n2;
			                break;
			case "divide" : num = n1 / n2;
			                break;  
			default:        num = 0;
			                break;            
		}
		return num;
	}

	public static String fmt(float d) {
		if(d == (long) d)
			return String.format("%d", (long) d);
		else
			return String.format("%s", d);
	}
}
