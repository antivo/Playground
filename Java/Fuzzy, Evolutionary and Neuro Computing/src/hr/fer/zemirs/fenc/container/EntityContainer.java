package hr.fer.zemirs.fenc.container;

import hr.fer.zemris.fenc.domain.DeclarationException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EntityContainer<T> {
	private final Map<String, T> container = new HashMap<String, T>();
	private final Set<String> strictlyAllowedNames;
	
	public EntityContainer(String[] allowedValues) {
		if(null != allowedValues) {
			this.strictlyAllowedNames = new HashSet<String>(Arrays.asList(allowedValues));
		} else {
			this.strictlyAllowedNames = null;
		}
	}
	
	public EntityContainer() {
		this(null);
	}
	
	private void assertAllowedOperator(String entityName) throws DeclarationException {
		if(null != strictlyAllowedNames) {
			if(!strictlyAllowedNames.contains(entityName)) {
				throw new DeclarationException(entityName + " is not allowed");
			}
		}
	}
	
	public void set(String entityName, T entity) throws DeclarationException {
		assertAllowedOperator(entityName);
		container.put(entityName, entity);
	}
	
	public T get(String entityName) throws UndefinedEntity {
		if(container.containsKey(entityName)) {
			return container.get(entityName);
		} else {
			throw new UndefinedEntity(entityName + " is not set");
		}
	}
	
	public boolean contains(String entityName) {
		return container.containsKey(entityName);
	}

}
