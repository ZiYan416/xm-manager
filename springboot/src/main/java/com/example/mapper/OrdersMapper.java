package com.example.mapper;

import com.example.entity.Orders;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 操作orders相关数据接口
*/
public interface OrdersMapper {

    /**
      * 新增
    */
    int insert(Orders orders);

    /**
      * 删除
    */
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Orders orders);

    /**
      * 根据ID查询
    */
    Orders selectById(Integer id);

    /**
      * 查询所有
    */
    List<Orders> selectAll(Orders orders);

    List<Orders> selectAllPhysical(Orders orders); // 物理查询方法

    @Select("select * from orders where order_id = #{orderId}")
    Orders selectByOrderId(String orderId);
}
