/*
 * FractalFrame.java
 *
 * Created on __DATE__, __TIME__
 */

package org.dwbzen.common.math.ui;

/**
 *
 * @author  __USER__
 */
public class FractalFrame extends javax.swing.JFrame {

	/** Creates new form FractalFrame */
	public FractalFrame() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc=" Generated Code ">
	private void initComponents() {
		buttonGroup1 = new javax.swing.ButtonGroup();
		buttonGroup2 = new javax.swing.ButtonGroup();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jTabbedPane3 = new javax.swing.JTabbedPane();
		jTabbedPane4 = new javax.swing.JTabbedPane();
		jTabbedPane2 = new javax.swing.JTabbedPane();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jTabbedPane1.addTab("Formula", jTabbedPane3);

		jTabbedPane1.addTab("Coloring", jTabbedPane4);

		jTabbedPane1.addTab("Location", jTabbedPane2);

		jTabbedPane1.setSelectedComponent(jTabbedPane4);

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				org.jdesktop.layout.GroupLayout.TRAILING,
				layout.createSequentialGroup().addContainerGap(511,
						Short.MAX_VALUE).add(jTabbedPane1,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 362,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup().addContainerGap().add(
						jTabbedPane1,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 245,
						org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(366, Short.MAX_VALUE)));
		pack();
	}// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FractalFrame().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.ButtonGroup buttonGroup2;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTabbedPane jTabbedPane2;
	private javax.swing.JTabbedPane jTabbedPane3;
	private javax.swing.JTabbedPane jTabbedPane4;
	// End of variables declaration//GEN-END:variables

}