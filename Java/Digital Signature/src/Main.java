import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;


public class Main {
	private static File envelopeFile = new java.io.File("./root/envelope.txt");
	private static File inputFile = new java.io.File("./root/in.txt");
	private static File outputFile = new java.io.File("./root/out.txt");
	
	
	
	private static Scanner in;

	
	public static void aes() {
		AES.main(null);
	}
	
	public static void rsa() {
		RSA.main(null);
	}
	
	public static String toHexString(byte[] b) {
		  String result = "";
		  for (int i=0; i < b.length; i++) {
		    result +=
		          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		  }
		  return result;
	}

	public static byte[] SHA1(byte[] convertme) {
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    return md.digest(convertme);
	}
	
	public static String toSHA1(byte[] convertme) {
	    MessageDigest md = null;
	    try {
	        md = MessageDigest.getInstance("SHA-1");
	    }
	    catch(NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } 
	    return toHexString(md.digest(convertme));
	}
	
	public static void sha_1() {
		try {
			
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			
			String out = toSHA1(in);
		
			System.out.println("m: " + new String(in));
			System.out.println("h(m): " + out);
			System.out.println("");
			System.out.println();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("<privatni_kljuc_FILE> <javni_kljuc_FILE> <duljina kljuca> <ulazna_FILE> <izlazna_FILE> <Kriptiraj/Dekriptiraj_CHAR>");
		
	}
	
	public static void sha_3() {
		
	}
	
	public static void digital_envelope() {
		try {
			byte[] k = AES.generate();
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			
			byte[] out = AES.encrypt(in, k);
			
			KeyPair p = RSA.getKeys();
			PublicKey pb = p.getPublic();
			
			byte[] e = RSA.e(k, pb);
			
			System.out.println("m: " + new String(in));
			System.out.println("K: " + new String(k));
			System.out.println("E(m, K): " + new String(out));
			
			System.out.println("");
			System.out.println("Pb: " + pb.toString());
			System.out.println("E(K, Pb): " + new String(e));
			
			System.out.println("");
			System.out.println("");
			System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void digital_signature() {
		try {
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			byte[] Qm = SHA1(in);	
			
			KeyPair p = RSA.getKeys();
			PrivateKey Sa = p.getPrivate();
			
			byte[] e = RSA.e(Qm, Sa);
			System.out.println("m: " + new String(in));
			System.out.println("Q(m): " + new String(Qm));
			System.out.println("Sa: " + Sa.toString());
			System.out.println("E(Q(m), Sa]: " + new String(e));
			
			System.out.println("");
			System.out.println("");
			System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void digital_seal() {
		try {
			byte[] k = AES.generate();
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			
			byte[] out = AES.encrypt(in, k);
			
			KeyPair p = RSA.getKeys();
			PublicKey pb = p.getPublic();
			
			byte[] e = RSA.e(k, pb);
			
			Envelope envelope = new Envelope();
			envelope.E1 = out;
			envelope.E2 = e;
			
			System.out.println("m: " + new String(in));
			System.out.println("K: " + new String(k));
			System.out.println("E(m, K): " + new String(out));
			
			System.out.println("");
			System.out.println("Pb: " + pb.toString());
			System.out.println("E(K, Pb): " + new String(e));
			
			 ObjectOutputStream publicKeyOS = new ObjectOutputStream(
			          new FileOutputStream(envelopeFile));
			      publicKeyOS.writeObject(envelope);
			      publicKeyOS.close();
			
			byte[] env = FileUtils.readFileToByteArray(envelopeFile);
			byte[] Qm = SHA1(env);	
			
			PrivateKey Sa = p.getPrivate();
			
			byte[] signed = RSA.e(Qm, Sa);
			System.out.println("m: " + new String(in));
			System.out.println("Q(m): " + new String(Qm));
			System.out.println("Sa: " + Sa.toString());
			System.out.println("E[Q(m), Sa]: " + new String(signed));
			
			
			Sa.getEncoded()[0] = (byte)4;
			
			byte[] unsigned = RSA.decrypt(Qm,Sa);
			
			
			
			System.out.println("");
			System.out.println("");
			System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		 	
		
	}
	
	public static void main(String[] args) {
		in = new Scanner(System.in);
		
		int choice = 6;
		while(0 != choice) {
			System.out.println("Odaberite:\n1) AES\n2) RSA\n3) SHA-1\n4) SHA-3\n5) Envelope\n6) Signature\n7) Seal\n0) EXIT\n");
			
			choice = in.nextInt();
			switch(choice) {
			case 0: { break; }
			case 1: {
				aes();
				break;
			}
			case 2: {
				rsa();
				break;
			}
			case 3: {
				sha_1();
				break;
			}
			case 4: {
				sha_3();
				break;
			}
			case 5: {
				digital_envelope();
				break;
			}
			case 6: {
				digital_signature();
				break;
			}
			case 7: {
				digital_seal();
				break;
			}
			}
		}
		
		in.close();
	}

}
