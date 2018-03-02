package com.xt.cfp.core.pojo;

public class SalesOrganizationInfo {
    private Long organizeId;

    private String organizeCode;

    private String organizeName;

    private String description;

    private Long parentId;

    private String organizeAttrType;

    private String organizeAttrValue;

    public Long getOrganizeId() {
        return organizeId;
    }

    public void setOrganizeId(Long organizeId) {
        this.organizeId = organizeId;
    }

    public String getOrganizeCode() {
        return organizeCode;
    }

    public void setOrganizeCode(String organizeCode) {
        this.organizeCode = organizeCode == null ? null : organizeCode.trim();
    }

    public String getOrganizeName() {
        return organizeName;
    }

    public void setOrganizeName(String organizeName) {
        this.organizeName = organizeName == null ? null : organizeName.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getOrganizeAttrType() {
        return organizeAttrType;
    }

    public void setOrganizeAttrType(String organizeAttrType) {
        this.organizeAttrType = organizeAttrType == null ? null : organizeAttrType.trim();
    }

    public String getOrganizeAttrValue() {
        return organizeAttrValue;
    }

    public void setOrganizeAttrValue(String organizeAttrValue) {
        this.organizeAttrValue = organizeAttrValue == null ? null : organizeAttrValue.trim();
    }
}