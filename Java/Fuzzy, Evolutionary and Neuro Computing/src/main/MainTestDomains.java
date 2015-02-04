package main;


import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.DomainException;
import hr.fer.zemris.fenc.domain.Enumerated;
import hr.fer.zemris.fenc.domain.Integer;
import hr.fer.zemris.fenc.domain.Real;
import hr.fer.zemris.fenc.domain.Cartesian;;

public class MainTestDomains {

	public static void main(String[] args) throws DeclarationException, DomainException {
		Domain a1 = new Integer(2, 4, 1);
		Domain b1 = new Integer(2, 4, 1);
		
		boolean test1 = (a1.equals(b1)) == true;
		System.out.println(test1);
		
		Domain a2 = new Real(2.5, 4.7, 13.3);
		Domain b2 = new Real(2.5, 15.9, 13.3);
		
		boolean test2 = (a2.equals(b2)) == false;
		System.out.println(test2);
		
		Domain a3 = new Enumerated( "x3");
		Domain b3 = new Enumerated("x2", "x3", "x4");
		
		boolean test3 = (a3.equals(b3)) == false;
		System.out.println(test3);
		
		List<Domain> l = new ArrayList<Domain>();
		l.add(b3);
		l.add(a3);
		
		Domain a4 = new Cartesian(b1, a1);
		
		
		Domain b4 = new Cartesian(a1, b1);
		
		boolean test4 = (a4.equals(b4)) == true;
		System.out.println(test4);
		
		
	
		//Domain A1 = new Real(5,6,0.001);
	
		
		Domain b5 = new Cartesian(a1, b1,a3,a2);
		
		
		
		System.out.println(b5.hasElement("2,2,x3,2.5"));
		System.out.println(b5.getCardinality());
		
		String ss ="(AKOMEOSTAVIS)";
		System.out.println(ss.charAt(ss.length()-1));
		/*
		Domain i = new Enumerated("");

		for(String s : b5.getDomainComponents()) {
			System.out.println(s);
		}
		
		Domain DD = new Real(0, 10, 0.1);
		for(String e : DD.getDomainComponents()) {
			System.out.println(e);
		}
		
		System.out.println("===");
		for(double j = 0; j < 10.05; j += 0.1) {
			Double dou = new Double(j);
			System.out.println(dou.toString() + " " +DD.getIndexOfElement(dou.toString()));
		}
		
		*/
		System.out.println("==");
		for(String s : b5.getDomainComponents()) {
			//System.out.println(b5.hasElement("2,2,x3,2.5"));
			//System.out.println(b5.hasElement(s));
			//System.out.println(b5.getIndexOfElement(s));
			System.out.println(s);
		}
	}

}
