package com.myrontuttle.fin.trade.web.panels;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.lang.Bytes;

import com.myrontuttle.fin.trade.adapt.Candidate;
import com.myrontuttle.fin.trade.web.data.DBAccess;

public class CandidateUploadPanel extends Panel {

	private FileUploadField fileUploadField;
	
	public CandidateUploadPanel(String id, final String groupId) {
		super(id);
		fileUploadField = new FileUploadField("fileUploadField");

		Form form = new Form("form"){
			@Override
			protected void onSubmit() {
				super.onSubmit();

				FileUpload fileUpload = fileUploadField.getFileUpload();

				try {
					File file = new File(System.getProperty("java.io.tmpdir") + "/" +
							fileUpload.getClientFileName());

					fileUpload.writeTo(file);
					
					FileInputStream fin = new FileInputStream(file);
					ObjectInputStream ois = new ObjectInputStream(fin);
					Candidate c = (Candidate) ois.readObject();
					ois.close();
					
					DBAccess.getDAO().addCandidate(c, groupId);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		form.setMultiPart(true);
		//set a limit for uploaded file's size
		form.setMaxSize(Bytes.kilobytes(100));
		form.add(fileUploadField);
		add(form);
	}
}
