package com.example.service;

import cn.hutool.core.util.ObjectUtil;
import com.example.common.enums.RoleEnum;
import com.example.common.enums.RoomEnum;
import com.example.entity.Account;
import com.example.entity.Orders;
import com.example.entity.Room;
import com.example.entity.Type;
import com.example.mapper.OrdersMapper;
import com.example.mapper.RoomMapper;
import com.example.mapper.TypeMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 客房信息表业务处理
 **/
@Service
public class RoomService {

    @Resource
    private RoomMapper roomMapper;
    @Resource
    private TypeMapper typeMapper;
    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 新增
     */
    public void add(Room room) {
        room.setStatus(RoomEnum.STATUS_OK.status);
        roomMapper.insert(room);

        // 向对应的酒店里面该分类下面的房间数量+1
        Type type = typeMapper.selectById(room.getTypeId());
        if (ObjectUtil.isNotEmpty(type)) {
            type.setNum(type.getNum() + 1);
            typeMapper.updateById(type);
        }
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        roomMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            roomMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Room room) {
        roomMapper.updateById(room);
    }

    /**
     * 根据ID查询
     */
    public Room selectById(Integer id) {
        return roomMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Room> selectAll(Room room) {
        return roomMapper.selectAll(room);
    }

    /**
     * 分页查询
     */
    public PageInfo<Room> selectPage(Room room, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (RoleEnum.HOTEL.name().equals(currentUser.getRole())) {
            room.setHotelId(currentUser.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Room> list = roomMapper.selectAll(room);
        return PageInfo.of(list);
    }

    public List<Room> selectByTypeId(String orderId) {
        Orders orders = ordersMapper.selectByOrderId(orderId);
        return roomMapper.selectByTypeId(orders.getTypeId());
    }

    /**
     * 统计酒店中某类型的房间数量（未删除）
     */
    public int countByTypeAndHotel(Integer hotelId, Integer typeId) {
        System.out.println("Received request with typeId: " + typeId + " and hotelId: " + hotelId);
        return roomMapper.countByTypeAndHotel(hotelId, typeId);
    }
}