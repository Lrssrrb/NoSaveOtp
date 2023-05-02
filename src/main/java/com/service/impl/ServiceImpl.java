package com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.entity.EntityTable;
import com.service.Services;
import com.utils.Totp;

import jakarta.mail.internet.MimeMessage;

@Service
public class ServiceImpl implements Services {

	@Autowired
	private Totp totp;
	
	@Autowired 
	private JavaMailSender javaMailSender;
	
	@Override
	public String get(EntityTable entityTable) {
		
		if(entityTable.getReceiver().contains("@")) {
		
			String res = entityTable.getReceiver().split("@")[0];
			String ans = "";
			
			for (int i = 0; i < res.length(); i++) {
				ans += Character.getNumericValue(res.charAt(i));
			}
			
			
			String otp = totp.getOTP(ans);
			
			
			if(entityTable.getReceiver().contains("@")) {
				
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper;
				
				try {
//					message. setFrom(new InternetAddress("abhayrana.sri420@gmail.com"));
					helper=new MimeMessageHelper(message,true);
					helper.setTo(entityTable.getReceiver());
					helper.setSubject("Otp verification");
					helper.setText("From luckyraj's side this is your Otp <"+otp+"> for our sevices.");
					javaMailSender.send(message);
						return "Mail Sent Successfully...";
					}
				catch (Exception e) {
					return e.getMessage();
				}
			}
		}
		
		return "Mail could not Sent Successfully...";
		
	}

	@Override
	public boolean varify(EntityTable entityTable){
		
		boolean validate = false;
			
		String res = entityTable.getReceiver().split("@")[0];
		String ans = "";
		
		for (int i = 0; i < res.length(); i++) {
			ans += Character.getNumericValue(res.charAt(i));
		}
		
		try {
			String s = ans;
			validate = totp.validate(s, entityTable.getOtp());
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return validate;
		
	}

}
