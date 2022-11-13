package crypto.scratch.keys.controller;

import crypto.scratch.keys.Keys;
import crypto.scratch.keys.service.KeysService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/keys")
public class KeysController {
	private final KeysService service;

	@GetMapping("/generate")
	public Keys generate() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			NoSuchProviderException {
		return service.generateKeys();
	}

	@GetMapping("/sign")
	public String sign(
			@RequestParam("private_key") String privateKey, @RequestParam("message") String message
	                  ) throws Exception {
		return service.sign(privateKey, message);
	}
}
