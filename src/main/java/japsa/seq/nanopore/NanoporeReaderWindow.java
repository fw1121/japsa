/*****************************************************************************
 * Copyright (c) Minh Duc Cao, Monash Uni & UQ, All rights reserved.         *
 *                                                                           *
 * Redistribution and use in source and binary forms, with or without        *
 * modification, are permitted provided that the following conditions        *
 * are met:                                                                  * 
 *                                                                           *
 * 1. Redistributions of source code must retain the above copyright notice, *
 *    this list of conditions and the following disclaimer.                  *
 * 2. Redistributions in binary form must reproduce the above copyright      *
 *    notice, this list of conditions and the following disclaimer in the    *
 *    documentation and/or other materials provided with the distribution.   *
 * 3. Neither the names of the institutions nor the names of the contributors*
 *    may be used to endorse or promote products derived from this software  *
 *    without specific prior written permission.                             *
 *                                                                           *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS   *
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, *
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR    *
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR         *
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,     *
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,       *
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR        *
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF    *
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING      *
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS        *
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.              *
 ****************************************************************************/

/**************************     REVISION HISTORY    **************************
 * 17 Apr 2015 - Minh Duc Cao: Created                                        
 *  
 ****************************************************************************/
package japsa.seq.nanopore;

import japsa.util.Logging;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.SeriesRenderingOrder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYAreaRenderer;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimePeriod;
import org.jfree.data.time.TimeTableXYDataset;

/**
 * @author minhduc
 *
 */
public class NanoporeReaderWindow implements Runnable{

	private JFrame frmNanoporeReader;
	private int height = 600, width = 800;
	private int topR = 100, topC = 100;
	String downloadFolder;	
	
	TimeTableXYDataset dataSet;
	NanoporeReaderStream reader;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					NanoporeReaderWindow window = new NanoporeReaderWindow(null,null);
					window.frmNanoporeReader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public NanoporeReaderWindow(NanoporeReaderStream r, TimeTableXYDataset dataset) throws IOException {
		reader = r;
		this.dataSet = dataset;
		
		initialize();		
		//frmNanoporeReader.pack();
		frmNanoporeReader.setVisible(true);
		
	}			


	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		downloadFolder = (new File(".")).getCanonicalPath();
		frmNanoporeReader = new JFrame();
		frmNanoporeReader.setTitle("Nanopore Reader");
		frmNanoporeReader.setBounds(topC, topR, 1218, 716);
		frmNanoporeReader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNanoporeReader.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel controlPanel = new JPanel();
		frmNanoporeReader.getContentPane().add(controlPanel, BorderLayout.WEST);
		controlPanel.setPreferredSize(new Dimension(320, height));
		controlPanel.setLayout(null);

		JPanel inputPanel = new JPanel();
		inputPanel.setBounds(0, 23, 400, 157);
		inputPanel.setBorder(BorderFactory.createTitledBorder("Input"));
		controlPanel.add(inputPanel);
		inputPanel.setLayout(null);

		JRadioButton rdbtnInputStream = new JRadioButton("Read files from input Stream");
		rdbtnInputStream.setBounds(8, 24, 384, 23);
		inputPanel.add(rdbtnInputStream);

		JRadioButton rdbtnF = new JRadioButton("Read files from download directory");
		rdbtnF.setBounds(8, 51, 289, 23);
		inputPanel.add(rdbtnF);

		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnInputStream);
		group.add(rdbtnF);
		

		final JTextField txtDir = new JTextField(this.downloadFolder);		
		txtDir.setBounds(30, 82, 362, 20);
		inputPanel.add(txtDir);
		
		final JButton btnChange = new JButton("Change");
		btnChange.setBounds(28, 109, 117, 25);
		inputPanel.add(btnChange);

		final JCheckBox chckbxInc = new JCheckBox("Include fail folder",reader.doFail);
		chckbxInc.setBounds(173, 110, 207, 23);
		inputPanel.add(chckbxInc);


		btnChange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setDialogTitle("Select download directory");
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setCurrentDirectory(new java.io.File("."));
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					downloadFolder = fileChooser.getSelectedFile().getPath();
					txtDir.setText(downloadFolder);	            
				}
			}
		});
		
		
		rdbtnF.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e){
				if (e.getStateChange() == ItemEvent.SELECTED){
					txtDir.setEnabled(true);
					btnChange.setEnabled(true);
					chckbxInc.setEnabled(true);
					
				}else{
					txtDir.setEnabled(false);
					btnChange.setEnabled(true);
					chckbxInc.setEnabled(false);
				}
			}           
		});

		JPanel outputPanel = new JPanel();
		outputPanel.setBounds(0, 192, 400, 169);
		outputPanel.setBorder(BorderFactory.createTitledBorder("Output"));
		controlPanel.add(outputPanel);
		outputPanel.setLayout(null);
		
		final JRadioButton rdbtnOut2Str = new JRadioButton("Output to output stream");
		rdbtnOut2Str.setBounds(8, 25, 302, 23);
		outputPanel.add(rdbtnOut2Str);
		
		final JRadioButton rdbtnOut2File = new JRadioButton("Output to file");	
		rdbtnOut2File.setBounds(8, 51, 302, 23);
		outputPanel.add(rdbtnOut2File);
		
		ButtonGroup group2 = new ButtonGroup();
		group2.add(rdbtnOut2Str);		
		group2.add(rdbtnOut2File);
		
		final JTextField txtOFile = new JTextField(this.downloadFolder);		
		txtOFile.setBounds(26, 77, 362, 20);
		outputPanel.add(txtOFile);
		
		final JButton btnFileChange = new JButton("Change");
		btnFileChange.setBounds(26, 109, 117, 25);
		outputPanel.add(btnFileChange);
		
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBounds(0, 373, 400, 132);
		optionPanel.setBorder(BorderFactory.createTitledBorder("Options"));
		controlPanel.add(optionPanel);
		optionPanel.setLayout(null);
		
		JCheckBox chckReads = new JCheckBox("Include template and complement reads",true);
		chckReads.setBounds(8, 23, 384, 23);
		optionPanel.add(chckReads);
		
		JCheckBox chckbxAddAUnicqu = new JCheckBox("Add a unique number to read name",reader.number);
		chckbxAddAUnicqu.setBounds(8, 52, 373, 23);
		optionPanel.add(chckbxAddAUnicqu);
		
		JLabel lblMinReadLength = new JLabel("Min read length");
		lblMinReadLength.setBounds(8, 83, 154, 15);
		optionPanel.add(lblMinReadLength);
		
		JTextField textPane = new JTextField();
		textPane.setText(reader.minLength+"");
		textPane.setBounds(137, 77, 71, 21);
		optionPanel.add(textPane);
		
		JPanel lPanel = new JPanel();
		lPanel.setBounds(0, 508, 400, 83);
		controlPanel.add(lPanel);
		lPanel.setLayout(null);		
		
		JButton btnStart = new JButton("Start");
		btnStart.setBounds(68, 36, 117, 25);
		btnStart.setEnabled(true);
		lPanel.add(btnStart);
		
		
		JButton btnStop = new JButton("Stop");		
		btnStop.setBounds(197, 36, 117, 25);
		btnStop.setEnabled(false);
		lPanel.add(btnStop);
		
				
		JPanel mainPanel = new JPanel();
		frmNanoporeReader.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setBorder(BorderFactory.createTitledBorder("Statistics"));
		mainPanel.setLayout(null);
		
		JPanel panelStats = new JPanel();
		panelStats.setBounds(12, 12, 603, 403);
		mainPanel.add(panelStats);
		
		JPanel panelCounts = new JPanel();
		panelCounts.setBounds(622, 12, 268, 367);
		mainPanel.add(panelCounts);
		panelCounts.setLayout(null);
		
		
		//////////////////////////////////////////////////
		JLabel lblFiles = new JLabel("Total fast5 files");
		lblFiles.setBounds(10, 10, 140, 20);
		panelCounts.add(lblFiles);
		
		txtTFiles = new JTextField("0");
		txtTFiles.setEditable(false);
		txtTFiles.setBounds(150, 10, 110, 20);
		panelCounts.add(txtTFiles);
		txtTFiles.setColumns(10);
		
		////////////////////////////////////////////////
		JLabel lblpFiles = new JLabel("Pass files");
		lblpFiles.setBounds(10, 35, 140, 20);
		//lblpFiles.setBounds(63, 61, 68, 15);
		panelCounts.add(lblpFiles);
		
		txtPFiles = new JTextField("0");
		txtPFiles.setEditable(false);
		txtPFiles.setBounds(150, 35, 110, 20);
		panelCounts.add(txtPFiles);
		txtPFiles.setColumns(10);
		
		JLabel lblFFiles = new JLabel("Fail files");
		lblFFiles.setBounds(10, 60, 140, 20);
		panelCounts.add(lblFFiles);
		
		txtFFiles = new JTextField("0");
		txtFFiles.setEditable(false);
		txtFFiles.setBounds(150, 60, 110, 20);
		panelCounts.add(txtFFiles);
		txtFFiles.setColumns(10);
		
		
		
		JLabel lbl2DReads = new JLabel("2D reads");
		lbl2DReads.setBounds(10, 90, 110, 20);
		panelCounts.add(lbl2DReads);		
		
		JLabel lblTempReads = new JLabel("Template reads");
		lblTempReads.setBounds(10, 115, 140, 20);
		panelCounts.add(lblTempReads);	
		
		
		JLabel lblCompReads = new JLabel("Complement reads");
		lblCompReads.setBounds(10, 140, 140, 20);
		panelCounts.add(lblCompReads);
		
		txtTempReads= new JTextField("0");
		txtTempReads.setEditable(false);
		txtTempReads.setBounds(150, 115, 110, 20);
		panelCounts.add(txtTempReads);
		txtTempReads.setColumns(10);		
		
		txt2DReads= new JTextField("0");
		txt2DReads.setEditable(false);
		txt2DReads.setBounds(150, 90, 110, 20);
		panelCounts.add(txt2DReads);
		txt2DReads.setColumns(10);		
		
		txtCompReads= new JTextField("0");
		txtCompReads.setEditable(false);
		txtCompReads.setBounds(150, 140, 110, 20);
		panelCounts.add(txtCompReads);
		txtCompReads.setColumns(10);
		
		
		
		JFreeChart chart = ChartFactory.createStackedXYAreaChart(
				"Read counts",      // chart title
				"Time",             // domain axis label
				"Read number",                   // range axis label
				this.dataSet   
				);			

		final StackedXYAreaRenderer render = new StackedXYAreaRenderer();

		DateAxis domainAxis = new DateAxis();
		domainAxis.setAutoRange(true);
		domainAxis.setDateFormatOverride(new SimpleDateFormat("HH:mm:ss"));
		//domainAxis.setTickUnit(new DateTickUnit(DateTickUnitType.SECOND, 1));

		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setRenderer(render);
		plot.setDomainAxis(domainAxis);
		plot.setSeriesRenderingOrder(SeriesRenderingOrder.FORWARD);
		plot.setForegroundAlpha(0.5f);

		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setNumberFormatOverride(new DecimalFormat("#,###.#"));
		rangeAxis.setAutoRange(true);
		
		ChartPanel chartLabel = new ChartPanel(chart,	            
	            600,
	            370,
	            600,
	            370,
	            600,
	            370,
	            true,
	            true,  // properties
	            true,  // save
	            true,  // print
	            true,  // zoom
	            true   // tooltips
	        );		
		
		
		panelStats.add(chartLabel);
		
	}
	
	JTextField txtCompReads, txtTempReads, txt2DReads;
	JTextField txtPFiles, txtFFiles, txtTFiles;
	
	public void run() {
		while(true) {				                
			//synchronized(reader) {//avoid concurrent update					
			TimePeriod period = new Second();
			dataSet.add(period, reader.twoDCount,"2D");
			dataSet.add(period, reader.compCount,"complement");
			dataSet.add(period, reader.tempCount,"template");


			txtTFiles.setText(reader.fileNumber+"");	                
			txtPFiles.setText(reader.passNumber+"");
			txtFFiles.setText(reader.failNumber+"");
			
			txt2DReads.setText(reader.twoDCount+"");
			txtCompReads.setText(reader.compCount+"");
			txtTempReads.setText(reader.tempCount+"");	

			//}  
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				Logging.error(ex.getMessage());
			}
		}
	}
}