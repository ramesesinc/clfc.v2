/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rameses.clfc.patch.loan;

import com.rameses.osiris2.themes.OKCancelPage;
import com.rameses.rcp.ui.annotations.StyleSheet;
import com.rameses.rcp.ui.annotations.Template;

/**
 *
 * @author louie
 */

@StyleSheet
@Template(OKCancelPage.class)
public class AssessmentOtherChargePage extends javax.swing.JPanel {

    /**
     * Creates new form AssessmentOtherChargeController
     */
    public AssessmentOtherChargePage() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        xFormPanel1 = new com.rameses.rcp.control.XFormPanel();
        xLookupField1 = new com.rameses.rcp.control.XLookupField();
        xDecimalField1 = new com.rameses.rcp.control.XDecimalField();

        xLookupField1.setCaption("Account");
        xLookupField1.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        xLookupField1.setExpression("${entity.title}");
        xLookupField1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        xLookupField1.setHandler("lookupItemAccount");
        xLookupField1.setName("account"); // NOI18N
        xLookupField1.setPreferredSize(new java.awt.Dimension(200, 20));
        xLookupField1.setRequired(true);
        xFormPanel1.add(xLookupField1);

        xDecimalField1.setCaption("Amount");
        xDecimalField1.setName("entity.amount"); // NOI18N
        xDecimalField1.setRequired(true);
        xFormPanel1.add(xDecimalField1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(xFormPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.rameses.rcp.control.XDecimalField xDecimalField1;
    private com.rameses.rcp.control.XFormPanel xFormPanel1;
    private com.rameses.rcp.control.XLookupField xLookupField1;
    // End of variables declaration//GEN-END:variables
}
