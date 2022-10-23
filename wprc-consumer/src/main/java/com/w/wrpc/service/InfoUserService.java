package com.w.wrpc.service;

import com.w.wrpc.pojo.InfoUser;

import java.util.List;
import java.util.Map;

/**
 * @author wsy
 * @date 2021/9/21 8:51 下午
 * @Description consumer通过该个接口远程调用provider的方法
 */
public interface InfoUserService {
    /**
     *
     * @param infoUser
     * @return
     */
    List<InfoUser> insertInfoUser(InfoUser infoUser);

    /**
     *
     * @param id
     * @return
     */
    InfoUser getInfoUserById(String id);

    /**
     *
     * @param id
     */
    void deleteInfoUserById(String id);

    /**
     *
     * @param id
     * @return
     */
    String getNameById(String id);

    /**
     *
     * @return
     */
    Map<String, InfoUser> getAllUser();
}
