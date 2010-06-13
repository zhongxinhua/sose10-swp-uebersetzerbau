/*Diese Java-Datei wird auf Grundlage der Datei
	"../bib/stdlib.xml" erzeugt und beinhaltet
	einfache Mathematikfunktionen.
*/
//package stdlib; auskommentiert 

class Math {
	public static int pow(int base, int expo) {
		if (base < 0 || expo < 0) {
			return -1;
		} else if (true) {
			int temp = base;
			while (expo > 1) {
				temp = temp * base;
				expo = expo - 1;
			}
			return temp;
		}
		return -1;
	}

	public static float sqrt(float root) {
		if (root < 0) {
			return -1;
		}
		float s = 0.0f;
		float preci = 1.0E-4f;
		while (s * s < root) {
			s = s + preci;
		}
		return s;
	}

	public static int ggt(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		return a;
	}

	public static int fak(int n) {
		if (n < 0) {
			return -1;
		} else if (n == 0) {
			return 1;
		} else if (n > 0) {
			int temp = 1;
			while (n > 0) {
				temp = temp * n;
				n = n - 1;
			}
			return temp;
		}
		return -1;
	}

	public static int fib(int n) {
		if (n >= 2) {
			int pred0 = 0;
			int pred1 = 1;
			int curr = pred0 + pred1;
			while (n >= 2) {
				int cache = curr;
				curr = curr + pred0;
				pred1 = pred0;
				pred0 = cache;
				n = n - 1;
			}
			return curr;
		} else if (n == 1) {
			return 1;
		} else if (n == 0) {
			return 0;
		} else if (n < 0) {
			return -1;
		}
		return -1;
	}
}
