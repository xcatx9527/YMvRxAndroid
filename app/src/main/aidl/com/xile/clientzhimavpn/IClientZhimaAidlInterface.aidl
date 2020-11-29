// IClientZhimaAidlInterface.aidl
package com.xile.clientzhimavpn;

// Declare any non-default types here with import statements

interface IClientZhimaAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);


    void linkVpn(int linkMoude, String city, in Map vpnlist,String vpnUserName,String vpnPassWord);

    void closeVpn();


    String getVpnState();




}
