import hr.fer.nos.data.AESKey;
import hr.fer.nos.data.NOSException;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import org.apache.commons.io.FileUtils;
import org.eclipse.osgi.internal.signedcontent.Base64;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.spec.IvParameterSpec;

public class RSA {
	static final String pathToGeneretaPub = "./root/pub.txt";
	static final String pathToGeneretaPriv = "./root/priv.txt";
	static final String pathToInput = "./root/in.txt";
	static final String pathToOutput = "./root/out.txt";
	
	File inputFile = new java.io.File(pathToInput);
	File outputFile = new java.io.File(pathToOutput);
	File privFile = new java.io.File(pathToGeneretaPriv);
	File pubFile = new java.io.File(pathToGeneretaPub);
	static final File generatePub = new java.io.File(pathToGeneretaPub);
	static final File generatePriv = new java.io.File(pathToGeneretaPriv);

	JTextField inputJTextField = null;//getText()
	JTextField outputJTextField = null;
	JTextField publicJTextField = null;
	JTextField privateJTextField = null;
	JComboBox<Integer> keySize = null;//getSelectedItem()
	JComboBox<Integer> blockSize = null;
	JRadioButton crypt = null;//isSelected()
	JRadioButton librarySolution = null;

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	public static byte[] e(byte[] text, Key key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }
	
	private static byte[] encrypt(byte[] text, PublicKey key) {
	    byte[] cipherText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");
	      // encrypt the plain text using the public key
	      cipher.init(Cipher.ENCRYPT_MODE, key);
	      cipherText = cipher.doFinal(text);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return cipherText;
	  }
	 
	public static byte[] decrypt(byte[] text, PrivateKey key) {
	    byte[] dectyptedText = null;
	    try {
	      // get an RSA cipher object and print the provider
	      final Cipher cipher = Cipher.getInstance("RSA");

	      // decrypt the text using the private key
	      cipher.init(Cipher.DECRYPT_MODE, key);
	      dectyptedText = cipher.doFinal(text);

	    } catch (Exception ex) {
	      ex.printStackTrace();
	    }

	    return dectyptedText;
	  }
	
	
	private void cryptVoid() {
		try {
			 final byte[] originalText = FileUtils.readFileToByteArray(inputFile);
		     ObjectInputStream inputStream;
		     inputStream = new ObjectInputStream(new FileInputStream(pubFile));
		     
		     final PublicKey publicKey = (PublicKey) inputStream.readObject();
		     final byte[] cipherText = encrypt(originalText, publicKey);
			
			
			FileUtils.writeByteArrayToFile(outputFile, cipherText);
			inputStream.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void decryptVoid() {
		try {
			byte[] cipherText = FileUtils.readFileToByteArray(inputFile);
			
			ObjectInputStream inputStream;
			inputStream = new ObjectInputStream(new FileInputStream(privFile));
		      
			final PrivateKey privateKey = (PrivateKey) inputStream.readObject();
		     final byte[] plainText = decrypt(cipherText, privateKey);
		      
		     
		     FileUtils.writeByteArrayToFile(outputFile, plainText);
		     inputStream.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void obaviKriptiranjeDekriptiranje() {
		if(crypt.isSelected()) {
			cryptVoid();
		} else {
			decryptVoid();
		}
	}
	
	public static void generate() {
		
	}
	
	public static KeyPair getKeys() {
		try {
		      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		      keyGen.initialize(1024);
		      final KeyPair key = keyGen.generateKeyPair();

		      
		      
		      return key;
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		return null;
	}
	
	private void generiraj() {
		try {
		      final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
		      keyGen.initialize(1024);
		      final KeyPair key = keyGen.generateKeyPair();

		      File privateKeyFile = privFile;
		      File publicKeyFile = pubFile;

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
	
	private void choosePriv() {
		final String dialogTitle = "Odaberi";
		
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("./root"));
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			privFile = chooser.getSelectedFile();
			privateJTextField.setText(privFile.toString());
		}
	}
	
	private void choosePub() {
		final String dialogTitle = "Odaberi";
		
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("./root"));
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			pubFile = chooser.getSelectedFile();
			publicJTextField.setText(pubFile.toString());
		}
	}
	
	private void chooseInFile() {
		final String dialogTitle = "Odaberi";
		
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("./root"));
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			inputFile = chooser.getSelectedFile();
			inputJTextField.setText(inputFile.toString());
		}
	}
	
	private void chooseOutFile() {
		final String dialogTitle = "Odaberi";
		
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("./root"));
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			outputFile = chooser.getSelectedFile();
			outputJTextField.setText(outputFile.toString());
		}
	}
	
	private void editKey() {
		try {
			Desktop.getDesktop().edit(privFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void editKeyPub() {
		try {
			Desktop.getDesktop().edit(pubFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void editInFIle() {
		try {
			Desktop.getDesktop().edit(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void editOutFIle() {
		try {
			Desktop.getDesktop().edit(outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RSA window = new RSA();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RSA() {
		initialize();
		privateJTextField.setText(pathToGeneretaPriv);
		publicJTextField.setText(pathToGeneretaPub);
		inputJTextField.setText(pathToInput);
		outputJTextField.setText(pathToOutput);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1026, 637);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnGeneriraj = new JButton("Generiraj");
		btnGeneriraj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generiraj();
			}
		});
		btnGeneriraj.setBounds(563, 26, 89, 34);
		frame.getContentPane().add(btnGeneriraj);
		
		JButton btnPogledaj_1 = new JButton("Pogledaj");
		btnPogledaj_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editInFIle();
			}
		});
		btnPogledaj_1.setBounds(563, 71, 89, 34);
		frame.getContentPane().add(btnPogledaj_1);
		
		JButton btnPogledaj_2 = new JButton("Pogledaj");
		btnPogledaj_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editOutFIle();
			}
		});
		btnPogledaj_2.setBounds(563, 116, 89, 34);
		frame.getContentPane().add(btnPogledaj_2);
		
		JButton btnPogledaj = new JButton("Pogledaj");
		btnPogledaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editKey();
			}
		});
		btnPogledaj.setBounds(464, 26, 89, 34);
		frame.getContentPane().add(btnPogledaj);
		
		JButton btnOdaberi_1 = new JButton("Odaberi");
		btnOdaberi_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseInFile();
			}
		});
		btnOdaberi_1.setBounds(464, 71, 89, 34);
		frame.getContentPane().add(btnOdaberi_1);
		
		JButton btnOdaberi_2 = new JButton("Odaberi");
		btnOdaberi_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chooseOutFile();
			}
		});
		btnOdaberi_2.setBounds(464, 116, 89, 34);
		frame.getContentPane().add(btnOdaberi_2);
		
		JButton btnOdaberi = new JButton("Odaberi");
		btnOdaberi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choosePriv();
			}
		});
		btnOdaberi.setBounds(365, 26, 89, 34);
		frame.getContentPane().add(btnOdaberi);
		
		JLabel lblAes = new JLabel("RSA");
		lblAes.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblAes);
		
		JLabel lblNewLabel = new JLabel("Priv");
		lblNewLabel.setBounds(39, 26, 37, 34);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblUlaznaDat = new JLabel("Ulazna datoteka:");
		lblUlaznaDat.setBounds(39, 71, 97, 34);
		frame.getContentPane().add(lblUlaznaDat);
		
		inputJTextField = new JTextField();
		inputJTextField.setColumns(10);
		inputJTextField.setBounds(157, 71, 297, 34);
		frame.getContentPane().add(inputJTextField);
		
		JLabel lblIzlaznaDat = new JLabel("Izlazna datoteka:");
		lblIzlaznaDat.setBounds(39, 116, 89, 34);
		frame.getContentPane().add(lblIzlaznaDat);
		
		crypt = new JRadioButton("Kriptiranje");
		crypt.setSelected(true);
		buttonGroup.add(crypt);
		crypt.setBounds(161, 181, 109, 23);
		frame.getContentPane().add(crypt);
		
		JRadioButton rdbtnDekriptiranje = new JRadioButton("Dekriptiranje");
		buttonGroup.add(rdbtnDekriptiranje);
		rdbtnDekriptiranje.setBounds(506, 181, 109, 23);
		frame.getContentPane().add(rdbtnDekriptiranje);
		
		JButton btnObaviKriptiranjedekriptiranje = new JButton("Obavi kriptiranje/dekriptiranje");
		btnObaviKriptiranjedekriptiranje.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				obaviKriptiranjeDekriptiranje();
			}
		});
		btnObaviKriptiranjedekriptiranje.setBounds(111, 211, 565, 68);
		frame.getContentPane().add(btnObaviKriptiranjedekriptiranje);
		
		JLabel lblStjepanAntivoIvica = new JLabel("Stjepan Antivo Ivica, 0036448356");
		lblStjepanAntivoIvica.setBounds(481, 292, 171, 14);
		frame.getContentPane().add(lblStjepanAntivoIvica);
		
		outputJTextField = new JTextField();
		outputJTextField.setColumns(10);
		outputJTextField.setBounds(157, 116, 297, 34);
		frame.getContentPane().add(outputJTextField);
		
		privateJTextField = new JTextField();
		privateJTextField.setColumns(10);
		privateJTextField.setBounds(105, 26, 250, 34);
		frame.getContentPane().add(privateJTextField);
		
		keySize = new JComboBox<Integer>();
		keySize.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {128, 192, 256}));
		keySize.setBounds(684, 58, 102, 25);
		frame.getContentPane().add(keySize);
		
		
		blockSize = new JComboBox<Integer>();
		blockSize.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {128, 192, 256}));
		blockSize.setBounds(684, 123, 102, 25);
		frame.getContentPane().add(blockSize);
		
		JLabel lblVelicinaKljua = new JLabel("Velicina klju\u010Da");
		lblVelicinaKljua.setBounds(684, 26, 89, 14);
		frame.getContentPane().add(lblVelicinaKljua);
		
		JLabel lblVelicinaBloka = new JLabel("Velicina bloka");
		lblVelicinaBloka.setBounds(684, 97, 89, 14);
		frame.getContentPane().add(lblVelicinaBloka);
		
		
		JButton btnOdaberi44 = new JButton("Odaberi");
		btnOdaberi44.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				choosePub();
			}
		});
		btnOdaberi44.setBounds(365, 356, 89, 34);
		frame.getContentPane().add(btnOdaberi44);
		
		
		
		JButton btnGeneriraj2 = new JButton("Generiraj");
		btnGeneriraj2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				generiraj();
			}
		});
		btnGeneriraj2.setBounds(570, 356, 89, 34);
		frame.getContentPane().add(btnGeneriraj2);
		
		JButton btnPogledaj_3 = new JButton("Pogledaj");
		btnPogledaj_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				editKeyPub();
			}
		});
		btnPogledaj_3.setBounds(470, 356, 89, 34);
		frame.getContentPane().add(btnPogledaj_3);
		
		JLabel lblNewLabel10 = new JLabel("Pub");
		lblNewLabel10.setBounds(39, 356, 37, 34);
		frame.getContentPane().add(lblNewLabel10);
		
		publicJTextField = new JTextField();
		publicJTextField.setColumns(7);
		publicJTextField.setBounds(105, 356, 250, 34);
		frame.getContentPane().add(publicJTextField);
		
		
		
		
	}
}
