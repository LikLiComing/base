package com.escredit.base.email.service;

import com.escredit.base.email.config.EmailProperties;
import com.escredit.base.entity.DTO;
import com.escredit.base.util.lang.ObjectUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration config;

	@Autowired
	private EmailProperties emailProperties;

	@Value("${spring.mail.username}")
	private String emailFrom;

	public EmailProperties getEmailProperties() {
		return emailProperties;
	}

	public void setEmailProperties(EmailProperties emailProperties) {
		this.emailProperties = emailProperties;
	}

	/**
	 * 发送邮件
	 * @param ftl ftl配置
	 * @param attachFile
	 * @return
	 */
	public DTO sendEmail(EmailProperties.Ftl ftl, File[] attachFile) {
		ftl.setEnable(true);
		this.emailProperties.setFtl(ftl);
		return sendEmail(attachFile);
	}

	/**
	 * 发送邮件
	 * @param common 常规参数
	 * @param ftl ftl参数
	 * @param attachFile
	 * @return
	 */
	public DTO sendEmail(EmailProperties.Common common,EmailProperties.Ftl ftl, File[] attachFile) {
		this.emailProperties.setCommon(common);
		ftl.setEnable(true);
		this.emailProperties.setFtl(ftl);
		return sendEmail(attachFile);
	}

	/**
	 * 发送邮件
	 * @param common 常规参数
	 * @param attachFile
	 * @return
	 */
	public DTO sendEmail(EmailProperties.Common common, File[] attachFile) {
		this.emailProperties.setCommon(common);
		return sendEmail(attachFile);
	}

	/**
	 * 发送邮件
	 * 采用yml配置
	 * @param
	 * @return
	 */
//	public DTO sendEmail(File attachFile) {
//		DTO resultDTO = new DTO();
//		boolean issuccess = true;
//		MimeMessage message = mailSender.createMimeMessage();
//		EmailProperties.Common common = emailProperties.getCommon();
//
//		try {
//			// set mediaType
//			MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
//					StandardCharsets.UTF_8.name());
//			// add attachment
//			if(attachFile != null && attachFile.exists()){
//				helper.addAttachment(attachFile.getName(),attachFile);
//			}
//
//			setText(helper,emailProperties);
//			helper.setTo(common.getTo());
//			helper.setSubject(common.getSubject());
//			helper.setFrom(emailFrom);
//			mailSender.send(message);
//
//		} catch (MessagingException | IOException | TemplateException e) {
//			issuccess = false;
//			resultDTO.putErr("-1",e.getMessage());
//		}
//		return resultDTO.setSuccess(issuccess);
//	}

	public DTO sendEmail(File[] attachFiles) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			if(attachFiles != null && attachFiles.length > 0){
				for (File attachFile : attachFiles) {
					//解决文件名乱码 xuwucheng 2020.01.15
					helper.addAttachment(MimeUtility.encodeWord(attachFile.getName()),attachFile);
				}
			}
		} catch (Exception e) {
			return new DTO(false).putErr("-1",e.getMessage());
		}
		return sendEmail(helper,message);
	}

	public DTO sendEmail(String attachmentFilename, InputStreamSource inputStreamSource) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());
			helper.addAttachment(MimeUtility.encodeWord(attachmentFilename),inputStreamSource);
		} catch (Exception e) {
			return new DTO(false).putErr("-1",e.getMessage());
		}
		return sendEmail(helper,message);
	}

	private DTO sendEmail(MimeMessageHelper helper,MimeMessage message) {
		DTO resultDTO = new DTO();
		boolean issuccess = true;
		EmailProperties.Common common = emailProperties.getCommon();
		try {
			setText(helper,emailProperties);
			helper.setTo(common.getTo());
			if (null != common.getCc() && common.getCc().length > 0) {
				helper.setCc(common.getCc());
			}
			if (null != common.getBcc() && common.getBcc().length > 0) {
				helper.setBcc(common.getBcc());
			}
			helper.setSubject(common.getSubject());
			helper.setFrom(emailFrom);
			mailSender.send(message);
		} catch (MessagingException | IOException | TemplateException e) {
			issuccess = false;
			resultDTO.putErr("-1",e.getMessage());
		}
		return resultDTO.setSuccess(issuccess);
	}

	/**
	 * 设置邮件正文
	 * @param helper
	 * @param emailProperties
	 * @throws IOException
	 * @throws TemplateException
	 * @throws MessagingException
	 */
	private void setText(MimeMessageHelper helper,EmailProperties emailProperties) throws IOException, TemplateException, MessagingException {
		EmailProperties.Ftl ftl = emailProperties.getFtl();
		EmailProperties.Common common = emailProperties.getCommon();
		String ftlName = ftl.getFtlName();
		String ftlFolder = ftl.getFtlFolder();
		String ftlBasePackagePath = ftl.getFtlBasePackagePath();

		//是否启动模版
		if(ftl.isEnable() && StringUtils.isNotEmpty(ftlBasePackagePath) && StringUtils.isNotEmpty(ftlName) ){
			//多个模块路径
//			config.setClassForTemplateLoading(this.getClass(),ftlBasePackagePath);
			Template t = config.getTemplate(ftlFolder+"/"+ftlName);

			Map dataModel = new HashMap();
			if(ftl.getDataModel()!=null){
				dataModel.putAll(ftl.getDataModel());
			}
			dataModel.putAll(ObjectUtils.toMap(common));

			String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, dataModel);
			helper.setText(html, true);
		}else{
			helper.setText(emailProperties.getCommon().getContent(),false);
		}
	}


}
