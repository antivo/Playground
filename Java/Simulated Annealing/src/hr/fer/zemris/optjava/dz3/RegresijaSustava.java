package hr.fer.zemris.optjava.dz3;

public class RegresijaSustava {

	public static void main(String[] args) {
		if(2 != args.length) {
			System.err.println("Expected two arguments. File path and exectuion plan");
			System.exit(1);
		}
		
		String filePath = args[0];
		String modus = args[1];
		
		boolean valid = false;;
		if(modus.equalsIgnoreCase("decimal")) {
			valid = true;
			
			
		} else {
			String[] assigment = modus.split(":");
			if(2 == assigment.length) {
				modus = assigment[0];
				if(modus.equalsIgnoreCase("binary")) {
					String number = assigment[1];
					int num = Integer.getInteger(number);
					if(num >= 5 && num <= 30) {
						valid = true;
						
					}
				}
			}			
		}

		if(!valid) {
			System.err.println("Second argument invalid");
			System.exit(1);
		}

		
	}

}
