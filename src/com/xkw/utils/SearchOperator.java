package com.xkw.utils;


/**
 * 通过查询中的, 查询参数以及操作的封装
 * @author anonymous
 *
 */
public class SearchOperator {

	/**
	 * 操作的种类
	 * @author anonymous
	 *
	 */
	public enum Operator {
		EQ, NOTEQ, LIKE, NOTLIKE, GT, LT, GTE, LTE, NULL, NOTNULL, IN, NOTIN;
	}

	public String fieldName;
	public Operator operator;
	public Object fieldValue;

	public SearchOperator(String fieldName, Operator operator, Object fieldValue) {
		this.fieldName = fieldName;
		this.operator = operator;
		this.fieldValue = fieldValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldName == null) ? 0 : fieldName.hashCode());
		result = prime * result + ((fieldValue == null) ? 0 : fieldValue.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
		SearchOperator other = (SearchOperator) obj;
		if (fieldName == null) {
			if (other.fieldName != null)
				return false;
		} else if (!fieldName.equals(other.fieldName))
			return false;
		if (fieldValue == null) {
			if (other.fieldValue != null)
				return false;
		} else if (!fieldValue.equals(other.fieldValue))
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}
	
}
