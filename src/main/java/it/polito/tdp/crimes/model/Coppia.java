package it.polito.tdp.crimes.model;

import java.util.Objects;

public class Coppia {

	private String e1;
	private String e2;
	private Integer n;
	
	public Coppia(String e1, String e2, Integer n) {
		super();
		this.e1 = e1;
		this.e2 = e2;
		this.n = n;
	}

	public String getE1() {
		return e1;
	}

	public String getE2() {
		return e2;
	}

	public Integer getN() {
		return n;
	}

	@Override
	public int hashCode() {
		return Objects.hash(e1, e2, n);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coppia other = (Coppia) obj;
		return Objects.equals(e1, other.e1) && Objects.equals(e2, other.e2) && Objects.equals(n, other.n);
	}

	@Override
	public String toString() {
		return e1 + " " + e2 + " " + n;
	}
	
	
}
