package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.CustomerUploadSnapshot;

/**
 * Created by lenovo on 2015/7/7.
 */
public class CustomerUploadSnapshotVO extends CustomerUploadSnapshot {

	private Attachment attachment;

	private String fileName;// 文件名 如：123456789.jpg

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
