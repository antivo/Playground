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
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;

import javax.crypto.spec.IvParameterSpec;

public class AES {
	private static final String pathToGeneretaKey = "./root/aes_kljuc.txt";
	private static final String pathToInput = "./root/in.txt";
	private static final String pathToOutput = "./root/out.txt";
	
	private File inputFile = new java.io.File(pathToInput);
	private File outputFile = new java.io.File(pathToOutput);
	private File keyFile = new java.io.File(pathToGeneretaKey);
	private static final File generateKeyFile = new java.io.File(pathToGeneretaKey);

	private JTextField inputJTextField = null;//getText()
	private JTextField outputJTextField = null;
	private JTextField keyJTextField = null;
	private JComboBox<Integer> keySize = null;//getSelectedItem()
	private JComboBox<Integer> blockSize = null;
	private JRadioButton crypt = null;//isSelected()
	private JRadioButton librarySolution = null;

	private JFrame frame;
	private final ButtonGroup buttonGroup = new ButtonGroup();

public static byte[] encrypt(byte[] plainText, byte[] encryptionKey) throws Exception {
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");

int length = plainText.length / 16 + ((plainText.length % 16 != 0)?1:0);
length *= 16;

byte[] text = new byte[length];
for(int i = 0; i < plainText.length; ++i) {
	text[i] = plainText[i];
}

while(length != plainText.length) {
	--length;
	text[length] = 0;
}

cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(encryptionKey));
return cipher.doFinal(text);
}
	 
public static byte[] decrypt(byte[] cipherText, byte[] encryptionKey) throws Exception{
Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(encryptionKey));
return cipher.doFinal(cipherText);
}
	
	
	private void cryptVoid() {
		try {
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			byte[] key = FileUtils.readFileToByteArray(keyFile);
			byte[] encoded = encrypt(in, key);
			
			FileUtils.writeByteArrayToFile(outputFile, encoded);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void decryptVoid() {
		try {
			byte[] in = FileUtils.readFileToByteArray(inputFile);
			byte[] key = FileUtils.readFileToByteArray(keyFile);
			
			
			byte[] decoded = decrypt(in, key);
			
			
			FileUtils.writeByteArrayToFile(outputFile, decoded);
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
	
	public static byte[] generate() {
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom(); // cryptograph. secure random 
		
			keyGen.init(128, random); 
			SecretKey secretKey = keyGen.generateKey();
			byte[] encoded =  secretKey.getEncoded();
			
			return encoded;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	
	private void generiraj() {
		Integer _keySize = (Integer) keySize.getSelectedItem();
		Integer _blockSize = (Integer) blockSize.getSelectedItem();
		
		KeyGenerator keyGen;
		try {
			keyGen = KeyGenerator.getInstance("AES");
			SecureRandom random = new SecureRandom(); // cryptograph. secure random 
		
			keyGen.init(_keySize, random); 
			SecretKey secretKey = keyGen.generateKey();
			byte[] encoded =  secretKey.getEncoded();
			
			keyJTextField.setText(pathToGeneretaKey);
			keyFile = generateKeyFile;
			
			FileUtils.writeByteArrayToFile(keyFile, encoded);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void chooseKeyFile() {
		final String dialogTitle = "Odaberi";
		
		JFileChooser chooser = new JFileChooser(); 
		chooser.setCurrentDirectory(new java.io.File("./root"));
		chooser.setDialogTitle(dialogTitle);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { 
			keyFile = chooser.getSelectedFile();
			keyJTextField.setText(keyFile.toString());
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
			Desktop.getDesktop().edit(keyFile);
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
					AES window = new AES();
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
	public AES() {
		initialize();
		keyJTextField.setText(pathToGeneretaKey);
		inputJTextField.setText(pathToInput);
		outputJTextField.setText(pathToOutput);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 826, 437);
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
				chooseKeyFile();
			}
		});
		btnOdaberi.setBounds(365, 26, 89, 34);
		frame.getContentPane().add(btnOdaberi);
		
		JLabel lblAes = new JLabel("AES");
		lblAes.setBounds(10, 11, 46, 14);
		frame.getContentPane().add(lblAes);
		
		JLabel lblNewLabel = new JLabel("Key:");
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
		
		keyJTextField = new JTextField();
		keyJTextField.setColumns(10);
		keyJTextField.setBounds(105, 26, 250, 34);
		frame.getContentPane().add(keyJTextField);
		
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
	}
}
