package com.xt.cfp.core.pojo;

/**
 * <pre>
 * TODO
 * </pre>
 *
 * @author LUYANFENG @ 2017/12/4
 */
public class CgBankBranch {
    private String code;
    private String branchName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return "CgBankBranch{" +
                "code='" + code + '\'' +
                ", branchName='" + branchName + '\'' +
                '}';
    }
}
