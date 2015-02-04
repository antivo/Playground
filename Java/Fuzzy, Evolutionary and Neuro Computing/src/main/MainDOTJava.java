package main;


import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import brodic.Concluder;

public class MainDOTJava {
	
	public static void main(String[] args) throws Exception {
		Concluder concluder = new Concluder();
		Map<String, Double> m = new HashMap<String, Double>();
		Scanner scanner = new Scanner(System.in);
		while(true) {
			String line = scanner.nextLine();
			if(line.equals("KRAJ")) {
				break;
			}
			
			String[] params = line.split(" ");
			Double[] ps = new Double[params.length];
			for(int i = 0 ; i <  ps.length; ++i) {
				ps[i] = Double.parseDouble(params[i]);
			}
		
			
			// NEXTLINE umjesto LINE .. lesson well learned
			
			double l = ps[0];
			double d = ps[1];
			double lk = ps[2];
			double dk = ps[3];
			double v = ps[4];
			double s = ps[5];
			
			
			m.put("L", l);
			m.put("D", d);
			m.put("LK", lk);
			m.put("DK", dk);
			m.put("V", v);
			m.put("S", s);
			
			
			int[] res = concluder.conclude(m);
			int a = res[0];
			int k = res[1];
			
			System.out.println(a + " " + k);
			System.out.flush();
		}
		
		scanner.close();

	}

}
