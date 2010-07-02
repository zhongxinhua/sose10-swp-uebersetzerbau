
public class test_Math {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		for (int i = 0; i <10; i++) {
			System.out.println("Math.pow(2,"+i+") = "+ Math.pow(2, i));
		}
		System.out.println("Math.pow(2,-2) = "+ Math.pow(2,-2));
		System.out.println("Math.pow(-2,2) = "+ Math.pow(-2,2));
		System.out.println("Math.pow(-2,-2) = "+ Math.pow(-2,-2));
		
		
		for (int i = 0; i <10; i++) {
			System.out.println("Math.sqrt("+i+") = "+ Math.sqrt(i));
		}
		System.out.println("Math.sqrt(-2) = "+ Math.sqrt(-2));
		
		for (int i = 0; i <10; i++) {
			System.out.println("Math.ggt(2589,"+i+") = "+ Math.ggt(2589, i));
		}
		System.out.println("Math.ggt(2589,-2) = "+ Math.ggt(2589,-2));
		System.out.println("Math.ggt(-2589,2) = "+ Math.ggt(-2589,2));
		System.out.println("Math.ggt(-2589,-2) = "+ Math.ggt(-2589,-2));
		
		for (int i = 0; i <10; i++) {
			System.out.println("Math.fak("+i+") = "+ Math.fak(i));
		}
		System.out.println("Math.fak(-2) = "+ Math.fak(-2));
		
		for (int i = 0; i <10; i++) {
			System.out.println("Math.fib("+i+") = "+ Math.fib(i));
		}
		System.out.println("Math.fib(-2) = "+ Math.fib(-2));
	}

}
