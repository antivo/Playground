package main;

import hr.fer.zemirs.fenc.container.EntityContainer;
import hr.fer.zemirs.fenc.container.UndefinedEntity;
import hr.fer.zemris.fenc.definer.DomainDefiner;
import hr.fer.zemris.fenc.domain.DeclarationException;
import hr.fer.zemris.fenc.domain.Domain;
import hr.fer.zemris.fenc.domain.Enumerated;

public class MainTestDefiner {

	public static void main(String[] args) throws DeclarationException, UndefinedEntity {
		
		EntityContainer<Domain> dc = new EntityContainer<Domain>();
		
		Domain e1 = DomainDefiner.defineDomain("enumerated {x1, x2, x3}", null);
		Domain e2 = DomainDefiner.defineDomain("enumerated {z1, z2}", null);
		Domain e3 = DomainDefiner.defineDomain("enumerated {u1, u2}", null);
		
		dc.set("e1", e1);
		dc.set("e2", e2);
		dc.set("e3", e3);
		
		Domain e = DomainDefiner.defineDomain("cartesian e1,e2,e3", dc);
		
		
		for(String s : e.getDomainComponents()) {
			System.out.println(s);
		}
	
	}

}
