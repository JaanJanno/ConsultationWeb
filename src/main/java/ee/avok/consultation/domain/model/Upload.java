package ee.avok.consultation.domain.model;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Upload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Basic(fetch = FetchType.LAZY)
	private byte[] upload;
	@Column
	private String filename;

	public Upload() {
		super();
	}

	public int getId() {
		return id;
	}

	public byte[] getUpload() {
		return upload;
	}

	public void setUpload(byte[] upload) {
		this.upload = upload;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Override
	public String toString() {
		return "Upload [id=" + id + ", upload=" + Arrays.toString(upload) + ", filename=" + filename + "]";
	}

}
