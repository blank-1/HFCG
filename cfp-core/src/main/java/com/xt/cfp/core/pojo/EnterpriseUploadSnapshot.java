package com.xt.cfp.core.pojo;

/**
 * 企业上传凭证快照
 */
public class EnterpriseUploadSnapshot {
    private Long snapshotId;//快照id
    private Long attachId;//附件ID
    private Long enterpriseId;//企业ID
    private Integer seqNum;//展示顺序
    private String type;//类型
    private String status;//状态
    private String isdisplay;//是否前台显示
    
    /**
     * 状态：0为正常，1为删除
     */
    public static final String STATUS_ENABLED = "0";
    public static final String STATUS_DISABLED = "1";
    
    /**
     * 是否前台显示：0为显示，1为不显示
     */
    public static final String ISDISPLAY_ENABLED = "0";
    public static final String ISDISPLAY_DISABLED = "1";

    public Long getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(Long snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Long getAttachId() {
        return attachId;
    }

    public void setAttachId(Long attachId) {
        this.attachId = attachId;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getIsdisplay() {
        return isdisplay;
    }

    public void setIsdisplay(String isdisplay) {
        this.isdisplay = isdisplay == null ? null : isdisplay.trim();
    }
}