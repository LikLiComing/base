package com.escredit.base.email.service;

import com.escredit.base.email.config.BaseEmailProperties;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.lang.ObjectUtils;
import com.escredit.base.email.entity.MailRequest;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class EmailService {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private Configuration config;

	private String emailFrom;
	private String defaultFtl;
	private String ftlFolder;

	public EmailService(BaseEmailProperties baseEmailProperties){
		this.emailFrom = baseEmailProperties.getUsername();
		this.defaultFtl = baseEmailProperties.getFtlName();
		this.ftlFolder = baseEmailProperties.getFtlFolder();
	}

	public DTO sendEmail(MailRequest request, String ftlName) {
		return sendEmail(request, ftlName, null);
	}

	public DTO sendEmail(MailRequest request, File attachFile) {
		return sendEmail(request,null, attachFile);
	}

	public DTO sendEmail(MailRequest request, String ftlName, File attachFile) {
		DTO resultDTO = new DTO();
		boolean issuccess = true;
		MimeMessage message = sender.createMimeMessage();
		try {
			// set mediaType
			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			// add attachment
			if(attachFile != null && attachFile.exists()){
				helper.addAttachment(attachFile.getName(),attachFile);
			}
			ftlName = StringUtils.isEmpty(ftlName)?defaultFtl:ftlName;
			//多个模块路径
			config.setClassForTemplateLoading(this.getClass(),"/templates");
			Template t = config.getTemplate(ftlFolder+"/"+ftlName);

			Map dataModel = new HashMap();
			if(request.getDataModel()!=null){
				dataModel.putAll(request.getDataModel());
			}
			dataModel.putAll(ObjectUtils.toMap(request));

			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, dataModel);
			helper.setTo(request.getTo());
			helper.setText(html, true);
			helper.setSubject(request.getSubject());
			helper.setFrom(emailFrom);
			sender.send(message);

		} catch (MessagingException | IOException | TemplateException e) {
			issuccess = false;
			resultDTO.putErr("-1",e.getMessage());
		}
		return resultDTO.setSuccess(issuccess);
	}


}
