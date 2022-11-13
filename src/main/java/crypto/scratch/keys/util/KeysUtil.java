package crypto.scratch.keys.util;

import crypto.scratch.keys.Keys;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeysUtil {
	private static final String EC_GEN_ALGORITHM = "secp256k1";
	private static final String SIGNATURE_ALGORITHM = "SHA256withECDSA";
	private static final String EC_ALGORITHM = "EC";
	private static final String BOUNCY_CASTLE_PROVIDER = "BC";
	private static final Charset ENCODING = StandardCharsets.UTF_8;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private KeysUtil() {
	}

	public static Keys generateKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			NoSuchProviderException {
		ECGenParameterSpec ecSpec = new ECGenParameterSpec(EC_GEN_ALGORITHM);
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(EC_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
		keyGen.initialize(ecSpec, new SecureRandom());
		KeyPair keyPair = keyGen.generateKeyPair();
		String privateKey = KeysUtil.getStringFromPrivateKey(keyPair.getPrivate());
		String publicKey = KeysUtil.getStringFromPublicKey(keyPair.getPublic());
		return new Keys(publicKey, privateKey);
	}

	private static String getStringFromPrivateKey(PrivateKey key) {
		return Base64.getEncoder()
		             .encodeToString(key.getEncoded());
	}

	private static String getStringFromPublicKey(PublicKey key) {
		return Base64.getEncoder()
		             .encodeToString(key.getEncoded());
	}

	public static String sign(String stringPrivateKey, String message) throws Exception {
		PrivateKey privateKey = getPrivateKeyFromString(stringPrivateKey);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
		signature.initSign(privateKey);
		signature.update(message.getBytes());
		return Base64.getEncoder()
		             .encodeToString(signature.sign());
	}

	private static PrivateKey getPrivateKeyFromString(String key) throws Exception {
		return KeyFactory.getInstance(EC_ALGORITHM)
		                 .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder()
		                                                                .decode(key)));
	}

	public static boolean verify(String stringPublicKey, String authorSignature, String message) throws Exception {
		PublicKey publicKey = getPublicKeyFromString(stringPublicKey);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, BOUNCY_CASTLE_PROVIDER);
		signature.initVerify(publicKey);
		byte[] decodedSignature = Base64.getDecoder()
		                                .decode(authorSignature);
		signature.update(message.getBytes(ENCODING));
		return signature.verify(decodedSignature);
	}

	private static PublicKey getPublicKeyFromString(String key) throws Exception {
		return KeyFactory.getInstance(EC_ALGORITHM)
		                 .generatePublic(new X509EncodedKeySpec(Base64.getDecoder()
		                                                              .decode(key)));
	}
}