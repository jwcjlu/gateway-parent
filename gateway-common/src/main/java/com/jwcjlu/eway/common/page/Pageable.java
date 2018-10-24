package com.jwcjlu.gateway.common.page;

import lombok.Data;

import java.io.Serializable;

/**
 * Pageable.
 * @author xiaoyu(Myth)
 */
@Data
public class Pageable implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * 每页显示记录数
	 */
	private int showCount = 10;

	/**
	 * 总记录数
	 */
	private int totalResult = 0;

	/**
	 * 总页数
	 */
	private int totalPage = 0;

	/**
	 *  当前页
	 */
	private int currentPage = 1;
    
	/**
     * 查询起始位置
     */
	private int start;

    /**
     * 排序字符串
     */
	private String orderByStr;


	public int getPageStart() {
        return (this.getCurrentPage() - 1) * this.showCount;
    }

	public String getOrderByStr() {
		return orderByStr;
	}

	public void setOrderByStr(String orderByStr) {
		this.orderByStr = orderByStr;
	}

	public int getTotalPage() {
        if (totalResult % showCount == 0){
            totalPage = totalResult / showCount;
        }else{
            totalPage = (totalResult / showCount) + 1;
        }
        return totalPage;
    }

	public int getCurrentPage() {
        if (currentPage <= 0) {
            currentPage = 1;
        }
        return currentPage;
    }
	
	public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
