package securechat.com.securechat.crypto;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;



import securechat.com.securechat.util.Constants;

/**
 * @author JavaDigest
 * 
 */
public class RSA {

  /**
   * String to hold name of the encryption algorithm.
   */
  public static final String ALGORITHM = "RSA";

  /**
   * String to hold the name of the private key file.
   */
  public static final String PRIVATE_KEY_FILE = Constants.FILE_EXTENSION_PATH+"private.key";

  /**
   * String to hold name of the public key file.
   */
  public static final String PUBLIC_KEY_FILE =Constants.FILE_EXTENSION_PATH+ "public.key";

    public static final String OTHER_KEY_FILE =Constants.FILE_EXTENSION_PATH+ "other.key";

  /**
   * Generate key which contains a pair of private and public key using 1024
   * bytes. Store the set of keys in Prvate.key and Public.key files.
   * 
   * @throws NoSuchAlgorithmException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static void generateKey(Context context) {
    try {
      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
      keyGen.initialize(1024);
      final KeyPair key = keyGen.generateKeyPair();

      File privateKeyFile = new File(context.getFilesDir(),PRIVATE_KEY_FILE);
      File publicKeyFile = new File(context.getFilesDir(),PUBLIC_KEY_FILE);

      // Create files to store public and private key
      if (privateKeyFile.getParentFile() != null) {
        privateKeyFile.getParentFile().mkdirs();
      }
      privateKeyFile.createNewFile();

      if (publicKeyFile.getParentFile() != null) {
        publicKeyFile.getParentFile().mkdirs();
      }
      publicKeyFile.createNewFile();

      // Saving the Public key in a file
      ObjectOutputStream publicKeyOS = new ObjectOutputStream(
          new FileOutputStream(publicKeyFile));
      publicKeyOS.writeObject(key.getPublic());
      publicKeyOS.close();

      // Saving the Private key in a file
      ObjectOutputStream privateKeyOS = new ObjectOutputStream(
          new FileOutputStream(privateKeyFile));
      privateKeyOS.writeObject(key.getPrivate());
      privateKeyOS.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  /**
   * The method checks if the pair of public and private key has been generated.
   * 
   * @return flag indicating if the pair of keys were generated.
   */
  public static boolean areKeysPresent(Context context) {

    File privateKey = new File(context.getFilesDir(),PRIVATE_KEY_FILE);
    File publicKey = new File(context.getFilesDir(),PUBLIC_KEY_FILE);

    if (privateKey.exists() && publicKey.exists()) {
      return true;
    }
    return false;
  }

  /**
   * Encrypt the plain text using public key.
   * 
   * @param text
   *          : original plain text
   * @param key
   *          :The public key
   * @return Encrypted text
   * @throws Exception
   */
  public static byte[] encrypt(String text, PublicKey key) {
    byte[] cipherText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);
      // encrypt the plain text using the public key
      cipher.init(Cipher.ENCRYPT_MODE, key);
      cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return cipherText;
  }
  public static byte[] encryptUsingPrivateKey(String text, PrivateKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text.getBytes());
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }

  /**
   * Decrypt text using private key.
   * 
   * @param text
   *          :encrypted text
   * @param key
   *          :The private key
   * @return plain text
   * @throws Exception
   */
  public static String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
      // get an RSA cipher object and print the provider
      final Cipher cipher = Cipher.getInstance(ALGORITHM);

      // decrypt the text using the private key
      cipher.init(Cipher.DECRYPT_MODE, key);
      dectyptedText = cipher.doFinal(text);

    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return new String(dectyptedText);
  }
  public static String getMd5Hash(String input)
  {
	  try{
	  MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(input.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();
	  }
	  catch(NoSuchAlgorithmException e)
	  {
		  e.printStackTrace();
	  }
	  return null;

  }
  public static String decryptUsingPublicKey(byte[] text, PublicKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance(ALGORITHM);

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(text);

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return new String(dectyptedText);
	  }
 


  /**
   * Test the EncryptionUtil
   */
  public static void main(String[] args) {

    try {

      // Check if the pair of keys are present else generate those.
     /* if (!areKeysPresent()) {
        // Method generates a pair of keys using the RSA algorithm and stores it
        // in their respective files
        generateKey();
      }*/

      final String originalText = "Text to be encrypted ";
      ObjectInputStream inputStream = null;

      // Encrypt the string using the public key
      inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
      final PublicKey publicKey = (PublicKey) inputStream.readObject();
      final byte[] cipherText = encrypt(originalText, publicKey);

      // Decrypt the cipher text using the private key.
      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
      final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
      final String plainText = decrypt(cipherText, privateKey);

      // Printing the Original, Encrypted and Decrypted Text
      System.out.println("Original: " + originalText);
      System.out.println("Encrypted: " +cipherText.toString());
      System.out.println("Decrypted: " + plainText);
      
      
      // Encrypt the string using the private key
      inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
      final PrivateKey prKey = (PrivateKey) inputStream.readObject();
      final byte[] ctext = encryptUsingPrivateKey(originalText, prKey);

      // Decrypt the cipher text using the public key.
      inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
      final PublicKey pubKey = (PublicKey) inputStream.readObject();
      final String pText = decryptUsingPublicKey(ctext, pubKey);

      // Printing the Original, Encrypted and Decrypted Text
      System.out.println("Original: " + originalText);
      System.out.println("Encrypted: " +ctext.toString());
      System.out.println("Decrypted: " + pText);

      System.out.println(getMd5Hash(originalText));

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static void testEncryption(Context context)
  {
      final String originalText = "Text to be encrypted ";
      ObjectInputStream inputStream = null;
    try{
          // Encrypt the string using the public key
          inputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(), PUBLIC_KEY_FILE)));
          final PublicKey publicKey = (PublicKey) inputStream.readObject();
          final byte[] cipherText = encrypt(originalText, publicKey);

          // Decrypt the cipher text using the private key.
          inputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(),PRIVATE_KEY_FILE)));
          final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
          final String plainText = decrypt(cipherText, privateKey);

          // Printing the Original, Encrypted and Decrypted Text
          System.out.println("Original: " + originalText);
          System.out.println("Encrypted: " +cipherText.toString());
          System.out.println("Decrypted: " + plainText);
    } catch ( Exception e)
    {
        e.printStackTrace();
    }


      // Encrypt the string using the private key
     /* inputStream = new ObjectInputStream(new FileInputStream(PRIVATE_KEY_FILE));
      final PrivateKey prKey = (PrivateKey) inputStream.readObject();
      final byte[] ctext = encryptUsingPrivateKey(originalText, prKey);

      // Decrypt the cipher text using the public key.
      inputStream = new ObjectInputStream(new FileInputStream(PUBLIC_KEY_FILE));
      final PublicKey pubKey = (PublicKey) inputStream.readObject();
      final String pText = decryptUsingPublicKey(ctext, pubKey);

      // Printing the Original, Encrypted and Decrypted Text
      System.out.println("Original: " + originalText);
      System.out.println("Encrypted: " +ctext.toString());
      System.out.println("Decrypted: " + pText);

*/
  }
  public static byte[] encryptUsingOthersPublicKey(Context context,String message)
  {
      try {
          ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(), OTHER_KEY_FILE)));
          final PublicKey publicKey = (PublicKey) inputStream.readObject();
          final byte[] cipherText = encrypt(message, publicKey);

          Log.e(" cypher"," "+cipherText.toString());
          return  cipherText;
      }
      catch(Exception e)
      {
          e.printStackTrace();
      }
      return  null;
  }
    public static String decryptUsingOthersPublicKey(Context context,String message)
    {
        return message;
        /*try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(), OTHER_KEY_FILE)));
            final PublicKey publicKey = (PublicKey) inputStream.readObject();
            final String cipherText = decryptUsingPublicKey(message.getBytes(Charset.forName("UTF-8")), publicKey);

            Log.e(" cypher"," "+cipherText);
            return  new String(cipherText);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/

    }
    public static String generateHashAndEncryptWithPrivateKey(Context context,String message)
    {
        try {
            String hashValue=getMd5Hash(message);
            ObjectInputStream   inputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(),PRIVATE_KEY_FILE)));
            final PrivateKey prKey = (PrivateKey) inputStream.readObject();
            final byte[] ctext = encryptUsingPrivateKey(hashValue, prKey);



            Log.e(" cypher"," "+new String(ctext));
            return  ctext.toString();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }
    public static String encryptUsingMyPrivateKey(Context context,byte[] message)
    {
        try {
         ObjectInputStream   InputStream = new ObjectInputStream(new FileInputStream(new File(context.getFilesDir(),PRIVATE_KEY_FILE)));
            final PrivateKey privateKey = (PrivateKey) InputStream.readObject();
            final String plainText = decrypt(message, privateKey);

            Log.e(" cypher"," "+plainText);
            return  plainText;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return  null;
    }
}
