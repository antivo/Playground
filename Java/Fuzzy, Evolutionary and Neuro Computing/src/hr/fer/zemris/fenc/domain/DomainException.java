package hr.fer.zemris.fenc.domain;

public class DomainException extends Exception {
	private static final long serialVersionUID = 1L;

	public DomainException() {
		super("Given element does not belong to assigned domain");
	}
}
