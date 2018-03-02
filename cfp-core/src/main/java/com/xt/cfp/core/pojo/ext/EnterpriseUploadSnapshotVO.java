package com.xt.cfp.core.pojo.ext;

import com.xt.cfp.core.pojo.Attachment;
import com.xt.cfp.core.pojo.EnterpriseUploadSnapshot;

public class EnterpriseUploadSnapshotVO extends EnterpriseUploadSnapshot {

	private Attachment attachment;

	private String fileName;

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
