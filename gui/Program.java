package gui;
//ParamPasta by ViveTheJoestar
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.File;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import cmd.ParamCopy;

public class Program {
	private static void errorBeep(Toolkit tk) {
		Runnable runWinErrorSnd = (Runnable) tk.getDesktopProperty("win.sound.exclamation");
		if (runWinErrorSnd != null) runWinErrorSnd.run();
	}
	private static void start() {
		Color bgColor = new Color(34, 31, 30);
		String htmlText = "<html><p style='color: green; text-align: center;'>Drag & Drop</p>";
		htmlText += "<p style='color: white; text-align: center;'>DAT Files</p>";
		htmlText += "<p style='color: red; text-align: center;'>To Copy</p></html>";
		String title = "ParamPasta";
		Toolkit tk = Toolkit.getDefaultToolkit();
		Image pasta = tk.getImage(ClassLoader.getSystemResource("img/pasta.png"));
		pasta = pasta.getScaledInstance(128, 128, Image.SCALE_SMOOTH);
		ImageIcon ico = new ImageIcon(pasta);
		JLabel icoLbl = new JLabel(" ");
		JLabel label = new JLabel(htmlText);
		JFrame frame = new JFrame(title);
		JPanel panel = new JPanel();
		icoLbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		icoLbl.setHorizontalAlignment(JLabel.CENTER);
		icoLbl.setIcon(ico);
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		label.setBackground(bgColor);
		label.setFont(new Font("Tahoma", Font.BOLD, 30));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setToolTipText("Only works on unpacked character costume files.");
		panel.setBackground(bgColor);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setTransferHandler(new TransferHandler() {
			@Override
			public boolean canImport(TransferHandler.TransferSupport ts) {
				if (!ts.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) return false;
				return true;
			}
			@Override
			@SuppressWarnings("unchecked")
			public boolean importData(TransferHandler.TransferSupport ts) {
				if (!canImport(ts)) return false;
				try {
					List<File> files = (List<File>) ts.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
					String results = ParamCopy.copyDatFilesToCostumeDirs(files);
					if (results != null) {
						tk.beep();
						JOptionPane.showMessageDialog(frame, results, title, JOptionPane.INFORMATION_MESSAGE);
					}
				}
				catch (Exception e) {
					exceptionMsg(e, tk); 
					return false;
				}
				return true;
			}
		});
		panel.add(Box.createVerticalGlue());
		panel.add(label);
		panel.add(icoLbl);
		panel.add(Box.createVerticalGlue());
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(pasta);
		frame.setSize(512, 512);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void exceptionMsg(Exception ex, Toolkit tk) {
		String err = ex.getClass().getSimpleName() + "" + ex.getMessage();
		errorBeep(tk);
		JOptionPane.showMessageDialog(null, err, "Exception", JOptionPane.ERROR_MESSAGE);
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			start();
		} catch (Exception e) {
			exceptionMsg(e, Toolkit.getDefaultToolkit());
		}
	}
}