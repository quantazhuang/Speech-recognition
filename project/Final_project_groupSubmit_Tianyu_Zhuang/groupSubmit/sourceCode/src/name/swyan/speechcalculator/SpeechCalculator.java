package name.swyan.speechcalculator;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.*;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import name.swyan.speechcalculator.data.JSoundCapture;
import name.swyan.speechcalculator.data.DataBase;
import name.swyan.speechcalculator.data.ObjectIODataBase;
import name.swyan.speechcalculator.hmm.HMMGenerator;
import name.swyan.speechcalculator.calculator.Calculator;

public class SpeechCalculator extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane = null;
	private JSoundCapture soundCapture = null;
	private JTabbedPane tabbedPane = null;
	private JPanel calPanel = null;

	private JButton num1Button = null;
	private JButton num2Button = null;
	private JButton plusButton = null;
	private JButton minusButton = null;
	private JButton multiButton = null;
	private JButton divButton = null;
	private JButton calButton = null;
	private JButton trainButton;
	private JTextField number1 = null;
	private JTextField number2 = null;
	private JTextField result = null;
	private JLabel num1Label;
	private JLabel num2Label;
	private JLabel resLabel;

	private HMMGenerator hgr = new HMMGenerator();
	private Calculator cal = new Calculator();
	
	private float num1;
	private float num2;
	private float reslt;
	

	/**
	 * This is the default constructor
	 */
	public SpeechCalculator() {
		super();
		this.setSize(485, 335);
		this.setContentPane(getJContentPane());
		this.setTitle("Speech Calculator");
	}

	/**
	 * This method initializes tabbedPane
	 * 
	 * @return javax.swing.JTabbedPane
	 */
	private JTabbedPane getJTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setBounds(new Rectangle(10, 94, 449, 178));
			tabbedPane.addTab("Calculator", null, getCalculatorPanel(), null);
		}
		return tabbedPane;
	}

	/**
	 * This method initializes calPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getCalculatorPanel() {
		if (calPanel == null) {
			calPanel = new JPanel();
			calPanel.setLayout(null);
			calPanel.add(getNumber1Text(), null);
			calPanel.add(getNumber2Text(), null);
			calPanel.add(getNumber1Label());
			calPanel.add(getNumber2Label());
			calPanel.add(getNum1Button(), null);
			calPanel.add(getNum2Button(), null);
			calPanel.add(getResultText(), null);
			calPanel.add(getResultLabel());
			calPanel.add(getPlusButton(), null);
			calPanel.add(getMinusButton(), null);
			calPanel.add(getMultiButton(), null);
			calPanel.add(getDivButton(), null);
			calPanel.add(getCalButton(), null);
			calPanel.add(getTrainButton());
		}
		return calPanel;
	}

	/**
	 * This method initializes number1
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNumber1Text() {
		if (number1 == null) {
			number1 = new JTextField();
			number1.setBounds(new Rectangle(11, 38, 101, 24));
		}
		return number1;
	}

	/**
	 * This method initializes number2
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getNumber2Text() {
		if (number2 == null) {
			number2 = new JTextField();
			number2.setBounds(new Rectangle(11, 103, 101, 24));
		}
		return number2;
	}

	/**
	 * This method initializes result
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getResultText() {
		if (result == null) {
			result = new JTextField();
			result.setBounds(new Rectangle(240, 38, 101, 24));
		}
		return result;
	}

	private JLabel getResultLabel() {
		if (resLabel == null) {
			resLabel = new JLabel("result:");
			resLabel.setBounds(240, 11, 101, 14);
		}
		return resLabel;
	}

	/**
	 * This method initializes plusButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getPlusButton() {
		if (plusButton == null) {
			plusButton = new JButton("+");
			plusButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//System.out.println("plus");
					result.setText(cal.fmt(cal.plus(num1, num2)));
				}
			});
			plusButton.setBounds(new Rectangle(240, 77, 50, 24));
		}
		return plusButton;
	}

	/**
	 * This method initializes minusButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMinusButton() {
		if (minusButton == null) {
			minusButton = new JButton("-");
			minusButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//System.out.println("minus");
					result.setText(cal.fmt(cal.minus(num1, num2)));
				}
			});
			minusButton.setBounds(new Rectangle(240, 103, 50, 24));
		}
		return minusButton;
	}

	/**
	 * This method initializes multiButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMultiButton() {
		if (multiButton == null) {
			multiButton = new JButton("*");
			multiButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//System.out.println("multiply");
					System.out.println(cal.multiply(num1, num2));
					result.setText(cal.fmt(cal.multiply(num1, num2)));
				}
			});
			multiButton.setBounds(new Rectangle(300, 77, 50, 24));
		}
		return multiButton;
	}

	/**
	 * This method initializes divButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getDivButton() {
		if (divButton == null) {
			divButton = new JButton("/");
			divButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					//System.out.println("divide");
					System.out.println(cal.divide(num1, num2));
					result.setText(cal.fmt(cal.divide(num1, num2)));
				}
			});
			divButton.setBounds(new Rectangle(300, 103, 50, 24));
		}
		return divButton;
	}

	private JButton getTrainButton() {
		if (trainButton == null) {
			trainButton = new JButton("Train");
			trainButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					hgr.generateCodebook();
					hgr.trainHMM();
				}
			});
			trainButton.setBounds(345, 103, 85, 24);
		}
		return trainButton;
	}
    
	private JLabel getNumber1Label() {
		if (num1Label == null) {
			num1Label = new JLabel("number 1:");
			num1Label.setBounds(11, 11, 101, 14);
		}
		return num1Label;
	}

	private JLabel getNumber2Label() {
		if (num2Label == null) {
			num2Label = new JLabel("number 2:");
			num2Label.setBounds(11, 77, 101, 14);
		}
		return num2Label;
	}

	/**
	 * This method initializes num1Button
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNum1Button() {
		if (num1Button == null) {
			num1Button = new JButton("Recognize");
			num1Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (soundCapture.isSoundDataAvailable()) {
						number1.setText(hgr.hmmGetWordFromAmplitureArray(soundCapture.getAudioData()));
					    num1 = getFloatfromString(number1.getText());
					    //System.out.println(num1);

					} else {
						number1.setText("No Rec");
					}
				}
			});
			num1Button.setBounds(new Rectangle(120, 38, 100, 24));
		}
		return num1Button;
	}

	/**
	 * This method initializes num2Button
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getNum2Button() {
		if (num2Button == null) {
			num2Button = new JButton("Recognize");
			num2Button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (soundCapture.isSoundDataAvailable()) {
						number2.setText(hgr.hmmGetWordFromAmplitureArray(soundCapture.getAudioData()));
					    num2 = getFloatfromString(number2.getText());
					    //System.out.println(num2);

					} else {
						number2.setText("No Rec");
					}
				}
			});
			num2Button.setBounds(new Rectangle(120, 103, 100, 24));
		}
		return num2Button;
	}

	/**
	 * This method initializes calButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCalButton() {
		if (calButton == null) {
			calButton = new JButton("Calculate");
			calButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					if (soundCapture.isSoundDataAvailable()) {
						result.setText(cal.fmt(cal.compute(num1, num2, hgr.hmmGetWordFromAmplitureArray(soundCapture.getAudioData()))));

					} else {
						result.setText("No Rec");
					}
				}
			});
			calButton.setBounds(new Rectangle(345, 38, 85, 24));
		}
		return calButton;
	}

	private float getFloatfromString(String s) {
		float num = 0;
		switch (s) {
			case "one":       num = 1;
			                  break;
			case "two":       num = 2;
			                  break;
			case "three":     num = 3;
			                  break;
			case "four":      num = 4;
			                  break;
			case "five":      num = 5;
			                  break;
			case "six":       num = 6;
			                  break;
			case "seven":     num = 7;
			                  break;
			case "eight":     num = 8;
			                  break;
			case "nine":      num = 9;
			                  break;
			case "ten":       num = 10;
			                  break;
			case "eleven":    num = 11;
			                  break;
			case "twelve":    num = 12;
			                  break;
			case "thirteen":  num = 13;
			                  break;
			case "fourteen":  num = 14;
			                  break;
			case "fifteen":   num = 15;
			                  break;
			case "sixteen":   num = 16;
			                  break;
			case "seventeen": num = 17;
			                  break;
			case "eighteen":  num = 18;
			                  break;
			case "nineteen":  num = 19;
			                  break;
			case "twenty":    num = 20;
			                  break;
			default:          num = 0;
			                  break;
		}
		return num;
	}

	/**
	 * This method initializes contentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (contentPane == null) {
			contentPane = new JPanel();
			contentPane.setLayout(null);
			contentPane.add(getJTabbedPane());
			contentPane.add(getSoundCapture());
		}
		return contentPane;
	}

	private JSoundCapture getSoundCapture() {
		if (soundCapture == null) {
			soundCapture = new JSoundCapture(true, true);
			soundCapture.setBounds(10, 10, 431, 74);
		}
		return soundCapture;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SpeechCalculator test = new SpeechCalculator();
				test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				test.setResizable(false);
				test.setVisible(true);
			}
		});
	}
}
