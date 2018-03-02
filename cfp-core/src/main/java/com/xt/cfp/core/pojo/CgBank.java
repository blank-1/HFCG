package com.xt.cfp.core.pojo;

/**
 * <pre>
 * 银行表
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/5
 */
public class CgBank {
    private String code;
    private String name;
    private String type;
    private Long iconCode;
    private Integer idType;

    public static enum IdTypeEnum {
        PERSON(1, "个人"), ENTERPRISE(2, "企业");

        private final String desc;
        private final int code;

        public String getDesc() {
            return desc;
        }

        public int getCode() {
            return code;
        }

        IdTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }
    }

    public Long getIconCode() {
        return iconCode;
    }

    public void setIconCode(Long iconCode) {
        this.iconCode = iconCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIdType() {
        return idType;
    }

    public void setIdType(Integer idType) {
        this.idType = idType;
    }

    @Override
    public String toString() {
        return "CgBank{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", iconCode='" + iconCode + '\'' +
                ", idType=" + idType +
                '}';
    }
}
