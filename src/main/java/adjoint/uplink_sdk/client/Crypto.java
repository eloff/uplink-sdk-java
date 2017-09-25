package adjoint.uplink_sdk.client;
import adjoint.uplink_sdk.client.Base58convert;
import com.sun.jersey.core.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA3Digest;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;

public class Crypto {
  public static String DeriveContractAddress(String script){
    Long timestamp = (System.currentTimeMillis()*1000);
    String ts = timestamp.toString();
    String contractAddr = ts + script;
    contractAddr = Base58convert.encode(sha3_256(contractAddr.getBytes()));

    return contractAddr;
  }
  public static String DeriveAssetAddress(byte[] signedBytes, String issuer) throws UnsupportedEncodingException{
    // Decode and Extract R & S
    ASN1Sequence sequence = ASN1Sequence.getInstance(signedBytes);

    // Convert R or S to Base64 and to string
    String R = new String(Base64.encode(sequence.getObjectAt(0).toString()), "ascii");
    String S = new String(Base64.encode(sequence.getObjectAt(1).toString()), "ascii");

    Long timestamp = (System.currentTimeMillis()/1000);
    String ts = timestamp.toString();

    String assetAddr = R + S + ts + issuer;
    assetAddr = Base58convert.encode(sha3_256(assetAddr.getBytes()));
    return assetAddr;
  }

  public static String DeriveAccountAddress(PublicKey publicKey){
  // Derives address from public key: b58(sha256(sha256(ripemd160(sha256(pubkey)))))
    String X = ((BCECPublicKey) publicKey).getW().getAffineX().toString();
    String Y = ((BCECPublicKey) publicKey).getW().getAffineY().toString();

    String XY = X + Y;

    byte[] hashed = sha3_256(sha3_256(ripemd160(sha3_256(XY.getBytes()))));
    String address = Base58convert.encode(hashed);
    return address;
  }

  public static byte[] sha3_256(byte[] data){
    SHA3Digest sha3 = new SHA3Digest(256);
    sha3.reset();
    sha3.update(data, 0, data.length);
    byte[] hashedBytes = new byte[256 / 8];
    sha3.doFinal(hashedBytes, 0);
    return hashedBytes;
  }

  public static byte[] ripemd160(byte[] data){
    RIPEMD160Digest ripe = new RIPEMD160Digest();
    ripe.update(data, 0, data.length);
    byte[] o = new byte[ripe.getDigestSize()];
    ripe.doFinal(o, 0);
    return o;
  }

  public static KeyPair GenerateKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException{
    Provider BC = new BouncyCastleProvider();

    ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", BC);

    keyGen.initialize(ecSpec,new SecureRandom());

    KeyPair pair = keyGen.generateKeyPair();
    return pair;
  }


  public static byte[] ReadKeyFromFile(String file) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
    File pemFile = new File(file);
    PemReader pemReader = new PemReader(new InputStreamReader(new FileInputStream(pemFile)));
    byte[] keyBytes = pemReader.readPemObject().getContent();
    return keyBytes;
  }

  public static PrivateKey ReadPrivateKey(byte[] keyBytes) throws  NoSuchAlgorithmException, InvalidKeySpecException, IOException{
    Provider BC = new BouncyCastleProvider();
    KeyFactory kf = KeyFactory.getInstance("ECDSA", BC);

    KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
    PrivateKey privateKey =  kf.generatePrivate(spec);
    return privateKey;
  }

  public static PublicKey ReadPublicKey(byte[] keyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
    Provider BC = new BouncyCastleProvider();
    KeyFactory kf = KeyFactory.getInstance("ECDSA", BC);

    KeySpec spec = new X509EncodedKeySpec(keyBytes);
    PublicKey publicKey =  kf.generatePublic(spec);
    return publicKey;
  }

  public static void SaveKeyToFile(byte[] Key, String filename, String keyType) throws IOException{
    StringWriter stringWriter = new StringWriter();
    PemWriter pemWriter = new PemWriter(stringWriter);
    PemObjectGenerator pemObject = new PemObject("EC " + keyType + " Key", Key);
    pemWriter.writeObject(pemObject);
    pemWriter.flush();
    pemWriter.close();
    String privateKeyString = stringWriter.toString();
    FileUtils.writeStringToFile(new File( filename + ".pem"), privateKeyString);
  }

  public static byte[] SignBytes(byte[] message, PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException{
    Security.addProvider(new BouncyCastleProvider());
    Provider BC = new BouncyCastleProvider();

    Signature ecdsa = Signature.getInstance("SHA3-256withECDSA", BC);
    ecdsa.initSign(privateKey);

    ecdsa.update(message);
    byte[] signedBytes = ecdsa.sign();

    return signedBytes;
  }

  public static String Sign(byte[] signedBytes) throws UnsupportedEncodingException, IOException{

    // Decode and Extract R & S ==========
    ASN1Sequence sequence = ASN1Sequence.getInstance(signedBytes);

    // Extract R and s ==========
    String R = sequence.getObjectAt(0).toString();
    String S = sequence.getObjectAt(1).toString();

    // RLength by bytes ==========
    final ByteArrayOutputStream dataR = new ByteArrayOutputStream();
    final DataOutputStream streamR = new DataOutputStream(dataR);
    Integer rLen = R.length();
    streamR.writeShort(rLen);
    streamR.flush();
    byte[] rLenBytes = dataR.toByteArray();
    String RLen = new String(rLenBytes);

    // SLength by bytes ==========
    final ByteArrayOutputStream dataS = new ByteArrayOutputStream();
    final DataOutputStream streamS = new DataOutputStream(dataS);
    Integer sLen = S.length();
    streamS.writeShort(sLen);
    streamS.flush();
    byte[] sLenBytes = dataS.toByteArray();
    String SLen = new String(sLenBytes);

    // concat and base64 encode to create signature ==========
    String rEnc = RLen + R;
    String sEnc = SLen + S;

    String signature =  new String(Base64.encode(rEnc + ":" + sEnc), "ascii");
    return signature;
  }

  public static Boolean VerifySignature(byte[] message, PublicKey publicKey, byte[] signedBytes) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException{
    Security.addProvider(new BouncyCastleProvider());
    Provider BC = new BouncyCastleProvider();
    Signature ecdsa = Signature.getInstance("SHA3-256withECDSA", BC);

    ecdsa.initVerify(publicKey);
    ecdsa.update(message);
    Boolean verification = ecdsa.verify(signedBytes);
    return verification;
  }

  private static byte[] hash(SHA3Digest shA3Digest, Object[] data) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }
  // Removes the first two padding 00 for publickey serialization of X Y
  public static byte[] RemovePadding(byte[] input){
   byte[] array = input;
   if (array[0] == 0) {
       byte[] tmp = new byte[array.length - 1];
       System.arraycopy(array, 1, tmp, 0, tmp.length);
       array = tmp;
   }
   return array;
  }
}