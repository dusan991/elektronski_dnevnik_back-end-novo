package com.iktpreobuka.elektronskidnevnik.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;
import com.iktpreobuka.elektronskidnevnik.repositories.OcenaRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.RoditeljRepository;
import com.iktpreobuka.elektronskidnevnik.repositories.UcenikRepository;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	public JavaMailSender emailSender;

	@Override
	public void sendTemplateMessage(OcenaEntity ocena) throws Exception {
		MimeMessage mail = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mail, true);
		helper.setTo(ocena.getUcenik().getRoditelj().getEmailRoditelja());
		helper.setSubject("Ocena nova");
		String text = "<html><body>"
				+ "<table style='border:2px solid black; border-collapse: collapse;text-align: center;'>"
				+ "<tr><th>ucenik</th><th>predmet</th><th>ocena</th><th>datum</th></tr>" + "<tr><td>"
				+ ocena.getUcenik().getImeUcenika() + " " + ocena.getUcenik().getPrezimeUcenika() + "</td>" + "<td>"
				+ ocena.getOdeljenjePredmetNastavnik().getPredmet().getImePredmeta() + "</td>" + "<td>"
				+ ocena.getOcena() + "</td>" + "<td>" + ocena.getDatumOcene() + "</td></tr></table></body></html>";
		helper.setText(text, true);

		emailSender.send(mail);
	}
}
