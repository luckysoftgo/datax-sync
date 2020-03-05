package com.application.base.rpc.test;

import com.application.base.rpc.util.IpUtil;

import java.net.UnknownHostException;

/**
 * @author admin 2018-10-23
 */
public class IpUtilTest {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(IpUtil.getIp());
        System.out.println(IpUtil.getIpPort(8080));
    }

}
