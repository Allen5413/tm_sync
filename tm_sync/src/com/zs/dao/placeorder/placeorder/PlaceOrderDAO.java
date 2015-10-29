package com.zs.dao.placeorder.placeorder;


import java.util.List;

public interface PlaceOrderDAO {
	
	public List<Object[]> querySpotCourseScheduMaterial(String spotCode, int enYear, int enQuarter, String specCode, String levelCode);
	
	public String queryMaxOrderNumber(String spotCode, long semesterId);
}
