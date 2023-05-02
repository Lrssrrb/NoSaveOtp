package com.utils;

import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;


@Component
public class Totp {
	
	public boolean validate(final String key, final String otp) {
		return this.getOTP(key).equals(otp);
	}

	public String getOTP(final String key) {
		
		long currentTimeMillis = System.currentTimeMillis()/1000/60/10;
		
		String steps = Long.toHexString(currentTimeMillis).toUpperCase();
		
		while (steps.length() < 16) {
			steps = "0" + steps;
		}

		final byte[] msg = this.hexStr2Bytes(steps);
		
		final byte[] k = this.hexStr2Bytes(key);

		final byte[] hash = this.hmac_sha1(k, msg);

		String otp = this.hash2Otp(hash);
		
		return otp;
	}
	
	private byte[] hexStr2Bytes(final String hex) {
		final byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
		final byte[] ret = new byte[bArray.length - 1];

		System.arraycopy(bArray, 1, ret, 0, ret.length);
		return ret;
	}

	private byte[] hmac_sha1(final byte[] keyBytes, final byte[] text) {
		try {
			final Mac hmac = Mac.getInstance("HmacSHA1");
			final SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmac.init(macKey);
			return hmac.doFinal(text);
		} catch (final GeneralSecurityException gse) {
			throw new UndeclaredThrowableException(gse);
		}
	}

	private String hash2Otp(byte[] hash) {
		final int offset = hash[hash.length - 1] & 0xf;
		final int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16) | ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
		final int otp = binary % 1000000;

		String result = Integer.toString(otp);
		while (result.length() < 6) {
			result = "0" + result;
		}
		
		return result;
	}

}