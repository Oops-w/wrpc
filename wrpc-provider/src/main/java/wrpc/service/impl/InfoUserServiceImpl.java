package wrpc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.w.wrpc.RpcService;
import com.w.wrpc.serializa.SerializationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wrpc.pojo.InfoUser;
import wrpc.service.InfoUserService;

import java.util.*;

/**
 * @author wsy
 * @date 2021/9/21 8:53 下午
 * @Description
 */
@RpcService(serialization = SerializationEnum.JSON, version = 1)
public class InfoUserServiceImpl implements InfoUserService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    //当做数据库，存储用户信息
    Map<String, InfoUser> infoUserMap = new HashMap<>();


    @Override
    public List<InfoUser> insertInfoUser(InfoUser infoUser) {
        logger.info("新增用户信息:{}", JSONObject.toJSONString(infoUser));
        infoUserMap.put(infoUser.getId(), infoUser);
        return getInfoUserList();
    }

    @Override
    public InfoUser getInfoUserById(String id) {
        InfoUser infoUser = infoUserMap.get(id);
        logger.info("查询用户ID:{}", id);
        return infoUser;
    }

    public List<InfoUser> getInfoUserList() {
        List<InfoUser> userList = new ArrayList<>();
        Iterator<Map.Entry<String, InfoUser>> iterator = infoUserMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, InfoUser> next = iterator.next();
            userList.add(next.getValue());
        }
        logger.info("返回用户信息记录数:{}", userList.size());
        return userList;
    }

    @Override
    public void deleteInfoUserById(String id) {
        logger.info("删除用户信息:{}", JSONObject.toJSONString(infoUserMap.remove(id)));
    }

    @Override
    public String getNameById(String id) {
        logger.info("根据ID查询用户名称:{}", id);
        return infoUserMap.get(id).getName();
    }

    @Override
    public Map<String, InfoUser> getAllUser() {
        logger.info("查询所有用户信息{}", infoUserMap.keySet().size());
        return infoUserMap;
    }
}
