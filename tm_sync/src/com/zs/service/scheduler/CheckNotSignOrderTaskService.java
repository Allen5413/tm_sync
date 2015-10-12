package com.zs.service.scheduler;

/**
 * 检查未签收的订单的物流状态，如果已经签收了，就修改订单状态
 * Created by Allen on 2015/10/11.
 */
public interface CheckNotSignOrderTaskService {
    public void CheckNotSignOrder();
}
