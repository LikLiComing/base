package com.escredit.base.entity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DTO extends EntityBO {

	/**
	 * 成功标记
	 */
	private boolean success = false;

	/**
	 * 实体对象
	 */
	protected Object object;

	/**
	 * 实例对象列表
	 */
	protected List<?> list;

	/**
	 * 参数列表
	 */
	protected Map<String, Object> paramsMap;

	/**
	 * 返回结果
	 */
	protected Map<String, Object> resultMap;

	/**
	 * 正常/异常信息
	 */
	protected String errmsg;

	protected String errcode;

	/**
	 * 分页
	 */
	protected PageIO page;

	/**
	 * 排序
	 */
	protected SortBO sort;

	private List<Sort> sortList;


	public String getErrmsg() {
		return errmsg;
	}

	public DTO setErrmsg(String errmsg) {
		this.errmsg = errmsg;
		return this;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public List<Sort> getSortList() {
		return sortList;
	}

	public void setSortList(List<Sort> sortList) {
		this.sortList = sortList;
	}

	public DTO putSortList(String field,String direction) {
		if (this.sortList == null){
			this.sortList = new LinkedList<Sort>();
		}
		sortList.add(new Sort(field,direction));
		return this;
	}

	public static class Sort{
		public static final String ASC = "1";
		public static final String DESC = "2";

		private String field;
		private String direction;

		public Sort(String field, String direction) {
			this.field = field;
			this.direction = direction;
		}

		public String getField() {
			return field;
		}

		public void setField(String field) {
			this.field = field;
		}

		public String getDirection() {
			return direction;
		}

		public void setDirection(String direction) {
			this.direction = direction;
		}
	}

	public DTO() {

	}

	public DTO(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public DTO setSuccess(boolean success) {
		this.success = success;
		return this;
	}


	public DTO putErr(String errcode) {
		this.errcode = errcode;
		return this;
	}

	public DTO putErr(String errcode, String errmsg) {
		this.errcode = errcode;
		this.errmsg = errmsg;
		return this;
	}

	/**
	 * 取实体对象
	 *
	 * @return object
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject() {
		return (T) object;
	}

	public DTO setObject(Object object) {
		this.object = object;
		return this;
	}

	/**
	 * 取实体对象列表
	 *
	 * @return 实体对象的List
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList() {
		return (List<T>) list;
	}

	public DTO setList(List<?> list) {
		this.list = list;
		return this;
	}

	public Map<String, Object> getParamsMap() {
		return paramsMap;
	}

	public DTO setParamsMap(Map<String, Object> paramsMap) {
		this.paramsMap = paramsMap;
		return this;
	}

	public DTO putParam(String key, Object value) {
		if (this.paramsMap == null)
			this.paramsMap = new LinkedHashMap<String, Object>();
		this.paramsMap.put(key, value);
		return this;
	}

	public DTO putParam(String key, Object value, String operate) {
		return putParam(operate+ Operate.connect+key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getParam(String key) {
		return this.paramsMap == null ? null : (T) this.paramsMap.get(key);
	}

	public void removeParam() {
		if (this.paramsMap != null){
			this.paramsMap.clear();
		}
	}

	public void removeParam(String key) {
		if (this.paramsMap != null)
			this.paramsMap.remove(key);
	}

	public Map<String, Object> getResultMap() {
		return resultMap;
	}

	public DTO setResultMap(Map<String, Object> resultMap) {
		this.resultMap = resultMap;
		return this;
	}

	public DTO putResult(String key, Object value) {
		if (this.resultMap == null)
			this.resultMap = new LinkedHashMap<String, Object>();
		this.resultMap.put(key, value);
		return this;
	}

	public <T> T getResult(String key) {
		return this.resultMap == null ? null : (T) this.resultMap.get(key);
	}

	public void removeResult(String key) {
		if (this.resultMap != null)
			this.resultMap.remove(key);
	}

	public PageIO getPage() {
		return page;
	}

	public DTO setPage(PageIO page) {
		this.page = page;
		return this;
	}

	public long getTotalCount() {
		return getPage() == null ? 0 : getPage().getTotalCount();
	}

	public SortBO getSort() {
		return sort;
	}

	public DTO setSort(SortBO sort) {
		this.sort = sort;
		return this;
	}

	public class Operate{
		public static final String connect = ":";

		public static final String equal = "=";

		public static final String notEqual = "!=";

		public static final String like = "like";

		public static final String greaterThanOrEqualTo = ">=";

		public static final String lessThanOrEqualTo = "<=";

	}

}
