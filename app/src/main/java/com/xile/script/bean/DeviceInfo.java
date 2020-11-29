package com.xile.script.bean;

/**
 * on 2017/5/7 11:00
 */
public class DeviceInfo {
    private String androidId;//Android Id
    private String IMEI;//IMEI
    private String IMSI;//IMSI
    private String brand;//设备牌子  Xiaomi
    private String device;//设备名  aries
    private String display;//显示设备号  LRX22G
    private String manufacturer;//厂商  Xiaomi
    private String model;//设备型号  MI 2S
    private String product;//产品型号  Xiaomi
    private String serial;//设备序列号  207d4706
    private String release;//系统版本号  5.0.2
    private String sdk;//apilevel  21
    private int sdk_int;//apilevel  21
    private String wifiName;//WIFI名称
    private String connectType;//网络连接类型
    private String macAddress;//MAC地址
    private String macArray;//MAC地址 HardwareAddress
    private String phoneNumber;//电话号码
    private String networkName;//网络运营商名称
    private String ipAddress;//Ip Address
    private String displayMetrics;//屏幕分辨率
    private String simSerialNumber;//Sim卡序列号(ICCID)
    private String cid;//SD卡序列号
    private String coreVersion;//内核版本
    private String arp;//连接本机的远端mac地址
    private String fingerprint;//指纹信息
    private String description;//描述信息
    private String bssid;//路由器mac地址

    public DeviceInfo() {
    }

    public DeviceInfo(String androidId, String IMEI, String IMSI, String brand, String device, String display, String manufacturer, String model, String product, String serial, String release, String sdk, int sdk_int, String wifiName, String connectType, String macAddress, String macArray, String phoneNumber, String networkName, String ipAddress, String displayMetrics, String simSerialNumber, String cid, String coreVersion, String arp, String fingerprint, String description, String bssid) {
        this.androidId = androidId;
        this.IMEI = IMEI;
        this.IMSI = IMSI;
        this.brand = brand;
        this.device = device;
        this.display = display;
        this.manufacturer = manufacturer;
        this.model = model;
        this.product = product;
        this.serial = serial;
        this.release = release;
        this.sdk = sdk;
        this.sdk_int = sdk_int;
        this.wifiName = wifiName;
        this.connectType = connectType;
        this.macAddress = macAddress;
        this.macArray = macArray;
        this.phoneNumber = phoneNumber;
        this.networkName = networkName;
        this.ipAddress = ipAddress;
        this.displayMetrics = displayMetrics;
        this.simSerialNumber = simSerialNumber;
        this.cid = cid;
        this.coreVersion = coreVersion;
        this.arp = arp;
        this.fingerprint = fingerprint;
        this.description = description;
        this.bssid = bssid;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getIMSI() {
        return IMSI;
    }

    public void setIMSI(String IMSI) {
        this.IMSI = IMSI;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getSdk() {
        return sdk;
    }

    public void setSdk(String sdk) {
        this.sdk = sdk;
    }

    public int getSdk_int() {
        return sdk_int;
    }

    public void setSdk_int(int sdk_int) {
        this.sdk_int = sdk_int;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacArray() {
        return macArray;
    }

    public void setMacArray(String macArray) {
        this.macArray = macArray;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDisplayMetrics() {
        return displayMetrics;
    }

    public void setDisplayMetrics(String displayMetrics) {
        this.displayMetrics = displayMetrics;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCoreVersion() {
        return coreVersion;
    }

    public void setCoreVersion(String coreVersion) {
        this.coreVersion = coreVersion;
    }

    public String getArp() {
        return arp;
    }

    public void setArp(String arp) {
        this.arp = arp;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getDeviceInfo() {
        return "DeviceInfo:" +
                "\n androidId='" + androidId + '\'' +
                "\n IMEI='" + IMEI + '\'' +
                "\n IMSI='" + IMSI + '\'' +
                "\n 设备牌子brand='" + brand + '\'' +
                "\n 设备名device='" + device + '\'' +
                "\n 显示设备号display='" + display + '\'' +
                "\n 厂商manufacturer='" + manufacturer + '\'' +
                "\n 设备型号model='" + model + '\'' +
                "\n 产品型号product='" + product + '\'' +
                "\n 设备序列号serial='" + serial + '\'' +
                "\n 系统版本号release='" + release + '\'' +
                "\n API LEVEL sdk='" + sdk + '\'' +
                "\n API LEVEL sdk_int='" + sdk_int + '\'' +
                "\n WIFI名称='" + wifiName + '\'' +
                "\n 网络连接类型connectType='" + connectType + '\'' +
                "\n MAC地址macAddress='" + macAddress + '\'' +
                "\n MAC地址macArray='" + macArray + '\'' +
                "\n 电话号码phoneNumber='" + phoneNumber + '\'' +
                "\n 网络运营商名称networkName='" + networkName + '\'' +
                "\n IP地址ipAddress='" + ipAddress + '\'' +
                "\n 屏幕分辨率displayMetrics='" + displayMetrics + '\'' +
                "\n sim卡序列号='" + simSerialNumber + '\'' +
                "\n SD卡序列号cid='" + cid + '\'' +
                "\n 路由器mac地址bssid='" + bssid + '\'' +
                "\n 指纹信息fingerprint='" + fingerprint + '\'' +
                "\n 描述信息description='" + description + '\'' +
                "\n 内核版本coreVersion='" + coreVersion + '\'' +
                "\n 远端mac地址arp=\n" + arp;

    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "androidId='" + androidId + '\'' +
                ", IMEI='" + IMEI + '\'' +
                ", IMSI='" + IMSI + '\'' +
                ", brand='" + brand + '\'' +
                ", device='" + device + '\'' +
                ", display='" + display + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", product='" + product + '\'' +
                ", serial='" + serial + '\'' +
                ", release='" + release + '\'' +
                ", sdk='" + sdk + '\'' +
                ", sdk_int=" + sdk_int +
                ", wifiName='" + wifiName + '\'' +
                ", connectType='" + connectType + '\'' +
                ", macAddress='" + macAddress + '\'' +
                ", macArray='" + macArray + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", networkName='" + networkName + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", displayMetrics='" + displayMetrics + '\'' +
                ", simSerialNumber='" + simSerialNumber + '\'' +
                ", cid='" + cid + '\'' +
                ", coreVersion='" + coreVersion + '\'' +
                ", arp='" + arp + '\'' +
                ", fingerprint='" + fingerprint + '\'' +
                ", description='" + description + '\'' +
                ", bssid='" + bssid + '\'' +
                '}';
    }
}
