/*
 * LabelIcon.java
 *
 * Created on July 25, 2014, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ztest;

import com.rameses.rcp.support.ImageIconSupport;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author wflores
 */
public class LabelIcon extends JLabel 
{
    private ImageIcon icon;
    private boolean hideRightIcon;
    
    public LabelIcon() {
        icon = ImageIconSupport.getInstance().getIcon("com/rameses/rcp/icons/search.png");
        setRightIcon(icon);
    }

    public ImageIcon getRightIcon() { return icon; }
    public void setRightIcon(ImageIcon icon) {
        this.icon = icon; 
    }
    
    public void setHideRightIcon(boolean hideRightIcon) {
        this.hideRightIcon = hideRightIcon; 
    } 
    
    public Insets getInsets(Insets insets) {
        Insets ins = super.getInsets(insets); 
        if (ins == null) ins = new Insets(0, 0, 0, 0); 
        
        ImageIcon iicon = getRightIcon();
        if (iicon == null || hideRightIcon) return ins;
        
        Insets ins0 = new Insets(ins.top, ins.left, ins.bottom, ins.right); 
        int iconWidth = iicon.getIconWidth(); 
        int rightPad = Math.max(ins0.right, iconWidth+3); 
        ins0.right = rightPad;
        return ins0; 
    }
    
    public void paint(Graphics g) { 
        super.paint(g);
        
        ImageIcon iicon = getRightIcon();
        if (iicon != null && !hideRightIcon) 
        {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.8f));
            
            int imgWidth = iicon.getIconWidth();
            int imgHeight = iicon.getIconHeight();
            int x = this.getWidth() - (imgWidth + 3);
            int y = (this.getHeight() - imgHeight) / 2;
            iicon.paintIcon(this, g2, x, y);
            g2.dispose();
        }        
    }    
}
