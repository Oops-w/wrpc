package wrpc;

import com.w.wrpc.WrpcServerInstance;
import com.w.wrpc.factory.JDKProxyFacotry;
import wrpc.service.InfoUserService;
import wrpc.service.impl.InfoUserServiceImpl;

import java.io.IOException;

/**
 * @author wsy
 * @date 2022/10/29 6:18 PM
 * @Description
 */
public class Main {
    public static void main(String[] args) throws Exception {
        InfoUserService proxy = JDKProxyFacotry.getJdkProxy(InfoUserServiceImpl.class);
        System.out.println(proxy.getNameById("123"));
//        WrpcServerInstance wrpcServerInstance = new WrpcServerInstance();
//
//        System.in.read();
    }
}
