package com.recsystem.enumeration;

/**
 * Created by sunny on 16/1/5.
 */
public enum UserDataFieldEnum {

    //behavior
    CLICK_ADV(0, "clickadv"),
    RECORD(1, "record"),

    WEIBOXIU_BROWSER(2, "weiboxiu_browser"),
    WEIBOXIU_SHARE(3, "weiboxiu_share"),
    WEIBOXIU_URL(4, "weiboxiu_url"),

    FEILAIXUN_BROWSER(5, "feilaixun_browser"),
    FEILAIXUN_SHARE(6, "feilaixun_share"),

    CAIJINGYUAN_BROWSER(7, "caijingyuan_browser"),
    CAIJINGYUAN_SHARE(8, "caijingyuan_share"),

    ZHIYINBANG_BROWSER(9, "zhiyinbang_browser"),
    ZHIYINBANG_SHARE(10, "zhiyinbang_share"),
    ZHIYINBANG_PHONE(11, "zhiyinbang_phone"),

    HUOBAODIAN_BROWSER(12, "huobaodian_browser"),
    HUOBAODIAN_SHARE(13, "huobaodian_share"),
    HUOBAODIAN_PHONE(14, "huobaodian_phone"),

    QIANGPIANYI_BROWSER(15, "qiangpianyi_browser"),

    //base info
    REGUSER(20, "reguser"),
    GUID(21, "guid"),
    UUID(22, "uuid"),
    OPEN_TIME(23, "open"),

    ;


    private int code;
    private String desc;

    UserDataFieldEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public int code() {
        return getCode();
    }

    public String desc() {
        return getDesc();
    }
}
