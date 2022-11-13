package crypto.scratch.keys.service;

import crypto.scratch.keys.Keys;
import crypto.scratch.keys.util.KeysUtil;
import org.springframework.stereotype.Service;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class KeysService {
	public Keys generateKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			NoSuchProviderException {
		return KeysUtil.generateKeys();
	}

	public String sign(String privateKey, String message) throws Exception {
		return KeysUtil.sign(privateKey, message);
	}
}
