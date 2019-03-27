package com.iktpreobuka.elektronskidnevnik.services;

import com.iktpreobuka.elektronskidnevnik.entities.OcenaEntity;

public interface EmailService {

	void sendTemplateMessage(OcenaEntity ocena) throws Exception;

}
