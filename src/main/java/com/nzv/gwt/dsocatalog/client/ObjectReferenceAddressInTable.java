package com.nzv.gwt.dsocatalog.client;


public class ObjectReferenceAddressInTable {
	private int row;
	private int column;
	
	public ObjectReferenceAddressInTable(int row, int column) {
		super();
		this.row = row;
		this.column = column;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + column;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ObjectReferenceAddressInTable other = (ObjectReferenceAddressInTable) obj;
		if (column != other.column)
			return false;
		if (row != other.row)
			return false;
		return true;
	}
}
