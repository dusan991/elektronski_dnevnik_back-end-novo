package com.iktpreobuka.elektronskidnevnik.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption {

	public static String getPassEncoded(String pass) {
		BCryptPasswordEncoder bCryptPasswordEncoder= new BCryptPasswordEncoder();
		System.out.println(pass);
		return bCryptPasswordEncoder.encode(pass);
		}
		public static void main(String[] args) {
		System.out.println(getPassEncoded("aleksa1234"));
		}
		
}
