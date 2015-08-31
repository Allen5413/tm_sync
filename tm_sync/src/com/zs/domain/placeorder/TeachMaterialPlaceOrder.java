package com.zs.domain.placeorder;

import java.util.Date;

import javax.persistence.*;

import com.feinno.framework.common.domain.AbstractEntity;

/**
 * 预订单
 * @author yanghaosen
 *
 */
@Entity
@Table(name = "teach_material_place_order")
public class TeachMaterialPlaceOrder extends AbstractEntity{

	public static final int ISSTOCK_YES = 0;    //是
	public static final int ISSTOCK_NOT = 1;    //否

	public static final String STATE_CONFIRMED = "1";    //已生成
	public static final String STATE_SORTING = "2";      //分拣中
	public static final String STATE_PACK = "3";         //已打包
	public static final String STATE_SEND = "4";         //已发出
	public static final String STATE_SIGN = "5";         //已签收
	public static final String STATE_DIFFICULT = "6";    //疑难
	public static final String STATE_RETURNSIGN = "7";   //退签
	public static final String STATE_RETURN = "8";       //退回
	
	//ID
	private Long id;   
	//中心编码
	private String spotCode;
	//学期ID
	private Long semesterId;
	//专业编码
	private String specCode;
	//层次编码
	private String levelCode;
	//入学年
	private String enYear;
	//入学季
	private String enQuarter;
	//订单编号
	private String orderCode;
	//订单价格
	private Float orderPrice;
	//订单状态
	private String orderStatus;
	//邮寄地址
	private String address;
	//邮政编码
	private String postalCode;
	//手机号码
	private String phone;
	//座机
	private String tel;
	//收件人
	private String adminName;
	//创建者
	private String creator;
	//创建时间
	private Date createTime;
	//操作者
	private String operator;
	//操作时间
	private Date operateTime;
	//打包ID
	private Long packageId;
	//是否有库存[0:是；1：否]
	private Integer isStock;
	//数据版本
	private Integer version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSpotCode() {
		return spotCode;
	}
	public void setSpotCode(String spotCode) {
		this.spotCode = spotCode;
	}
	public Long getSemesterId() {
		return semesterId;
	}
	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}
	public String getSpecCode() {
		return specCode;
	}
	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}
	public String getLevelCode() {
		return levelCode;
	}
	public void setLevelCode(String levelCode) {
		this.levelCode = levelCode;
	}
	public String getEnYear() {
		return enYear;
	}
	public void setEnYear(String enYear) {
		this.enYear = enYear;
	}
	public String getEnQuarter() {
		return enQuarter;
	}
	public void setEnQuarter(String enQuarter) {
		this.enQuarter = enQuarter;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Float getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Version
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Long getPackageId() {
		return packageId;
	}
	public void setPackageId(Long packageId) {
		this.packageId = packageId;
	}
	public Integer getIsStock() {
		return isStock;
	}
	public void setIsStock(Integer isStock) {
		this.isStock = isStock;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
}

