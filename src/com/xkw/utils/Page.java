/**
 * 软件著作权：学科网
 * 系统名称：xy360
 * 创建日期： 2015-01-27
 */
package com.xkw.utils;


import java.util.List;

/**
 * 分页的封装类
 * @author anonymous
 * @param <T>
 */

public class Page<T> {
	
	//内容
	private List<T> content;
	//单页显示记录数
	private Integer size;
	//当前页
	private Integer number;
	//总页数
	private Integer totalPages;
	//总记录数
	private Long totalElements;
	
	public Page(List<T> content, Integer number, Long totalElements, Integer size) {
		this.content = content;
		this.number = (number<0) ? 0 : number;
		this.totalElements = totalElements;
		this.size = size;
		
		totalPages = (size == 0) ? 1 : (int) Math.ceil((double) totalElements / (double) size);
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	public Long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Long totalElements) {
		this.totalElements = totalElements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((number == null) ? 0 : number.hashCode());
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result
				+ ((totalElements == null) ? 0 : totalElements.hashCode());
		result = prime * result
				+ ((totalPages == null) ? 0 : totalPages.hashCode());
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
		Page<?> other = (Page<?>) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (totalElements == null) {
			if (other.totalElements != null)
				return false;
		} else if (!totalElements.equals(other.totalElements))
			return false;
		if (totalPages == null) {
			if (other.totalPages != null)
				return false;
		} else if (!totalPages.equals(other.totalPages))
			return false;
		return true;
	}
	
	
	
	
}
