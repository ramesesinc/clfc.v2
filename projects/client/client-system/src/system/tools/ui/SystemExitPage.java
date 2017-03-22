/*
 * ExitPage.java
 *
 * Created on November 4, 2010, 4:23 PM
 */

package system.tools.ui;

/**
 *
 * @author  ms
 */
public class SystemExitPage extends javax.swing.JPanel {
    
    /** Creates new form ExitPage */
    public SystemExitPage() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        xButton1 = new com.rameses.rcp.control.XButton();
        xButton2 = new com.rameses.rcp.control.XButton();
        xButton3 = new com.rameses.rcp.control.XButton();
        xButton4 = new com.rameses.rcp.control.XButton();

        setPreferredSize(new java.awt.Dimension(418, 266));
        xButton1.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        xButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system/images/shutdown.png")));
        xButton1.setText("Shutdown");
        xButton1.setFont(new java.awt.Font("Verdana", 0, 12));
        xButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        xButton1.setIconTextGap(20);
        xButton1.setName("shutdown");
        xButton1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        xButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        xButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        xButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system/images/logoff.png")));
        xButton2.setText("Log Off");
        xButton2.setFont(new java.awt.Font("Verdana", 0, 12));
        xButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        xButton2.setIconTextGap(20);
        xButton2.setName("logoff");
        xButton2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        xButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        xButton3.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        xButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/system/images/suspend.png")));
        xButton3.setText("Suspend");
        xButton3.setFont(new java.awt.Font("Verdana", 0, 12));
        xButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        xButton3.setIconTextGap(20);
        xButton3.setName("suspend");
        xButton3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        xButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        xButton4.setText("Cancel");
        xButton4.setName("cancel");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(29, 29, 29)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(xButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(xButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 109, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 114, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(42, 42, 42)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(xButton3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(xButton2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(72, 72, 72)
                .add(xButton4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XButton xButton1;
    private com.rameses.rcp.control.XButton xButton2;
    private com.rameses.rcp.control.XButton xButton3;
    private com.rameses.rcp.control.XButton xButton4;
    // End of variables declaration//GEN-END:variables
    
}
