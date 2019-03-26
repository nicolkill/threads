/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos.proshecto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Nicol Acosta
 */
public class Inex extends javax.swing.JFrame {
    private ArrayList<Movil> lista;
    private Thread print, generate, outs;
    private boolean fila[] = new boolean[14];
    private int aceleracion = 1, vuelo = -1;
    protected boolean asientos[][] = new boolean[18][6];
    public Inex() {
        initComponents();
        this.setSize(700, 700);
        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                print.stop();
                generate.stop();
                outs.stop();
                for(Movil m: lista) m.stop();
                super.windowClosing(e);
            }
            
        });
        
        for (int i = 0; i < fila.length; i++) fila[i] = false;
        for (int i = 0; i < asientos.length; i++) for(int j = 0;j < asientos[i].length;j++) asientos[i][j] = false;
        lista = new ArrayList();
        print = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    long ticksPS = 1000 / 30;
                    long startTime=0;
                    long sleepTime=0;
                    while(true) {
                            repaint();
                            sleepTime = ticksPS-(System.currentTimeMillis() - startTime);
                            if (sleepTime > 0) Thread.sleep(sleepTime);
                            else Thread.sleep(10);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        print.start();
        generate = new Thread(new Runnable() {
            @Override
            public void run() {
                Movil m;
                while(true) {
                    try {
                        m = new Movil(Inex.this, 50, 650, 20, 20);
                        lista.add(m);
                        new Thread(m).start();
                        Thread.sleep(1500/aceleracion);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
//                for (int i = 0; i < 150; i++) lista.add(new Movil(Inex.this, 50, 650, 20, 20));
//                for (Movil m: lista) {
//                    try {
//                        m.start();
//                        Thread.sleep(1500/aceleracion);
//                    } catch (InterruptedException ex) {
//                        ex.printStackTrace();
//                    }
//                }
            }
        });
        generate.start();
        outs = new Thread(new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                try {
                    while(true) {
                        Thread.sleep((r.nextInt(5)+1)*10000/aceleracion);
                        if(vuelo >= 5) vuelo = -1;
                        vuelo++;
                        for(Movil e: lista) {
                            if(e.vuelo == vuelo) e.salida = false;
//                            Thread.sleep(100);
                        }
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
        outs.start();
    }
    public synchronized void salir(Movil e) {
        lista.remove(e);
        e.stop();
    }
    public void salida(int x, int y) {
        asientos[x][y] = false;
    }
    public synchronized int checarAsiento(int fila) {
        int ind = -1;
        if(fila >= asientos.length) return ind;
        for (int i = 0; i < asientos[fila].length; i++) {
            if(!asientos[fila][i]) {
                ind = i;
                asientos[fila][i] = true;
                break;
            }
        }
        return ind;
    }
    public void atender(int ind, long espera) throws InterruptedException {
        Thread.sleep(espera/aceleracion);
        fila[ind] = false;
        synchronized(this) {
            notifyAll();
        }
    }
    public boolean libre() {
        for (int i = 0; i < fila.length; i++) {
            if(!fila[i]) return false;
        }
        return true;
    }
    public synchronized int fila() throws InterruptedException {
        int ind = -1;
        while(libre()) wait();
        for (int i = 0; i < fila.length; i++) {
            if(!fila[i]) {
                ind = i;
                break;
            }
        }
        fila[ind] = true;
        notifyAll();
        return ind;
    }
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2=(Graphics2D)g;
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.fillRect(0, 350, 590, 20);
        
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(0, 300, 590, 300);
        g2.drawLine(680, 130, 720, 130);
        g2.drawLine(680, 180, 720, 180);
        g2.drawString("Salida vuelo N." + vuelo, 50, 45);
        
        for(Movil m: lista) m.paint(g2);
        g2.setColor(Color.red);
        for(int i = 30;i < 570;i+=40) g2.fillRect(i, 320, 20, 20);
        g2.setColor(Color.black);
        g2.fillRect(650, 100, 20, 20);
        g2.setColor(Color.white);
        g2.fillRect(650, 100, 20, 3);
        g2.drawString("Estrelladioro", 50, 310);
        this.getToolkit().sync();
        g2.dispose();
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        javax.swing.JCheckBox jCheckBox1 = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jCheckBox1.setText("Acelerar");
        jCheckBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox1ItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jCheckBox1)
                .addGap(0, 680, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 513, Short.MAX_VALUE)
                .addComponent(jCheckBox1))
        );

        pack();
    }// </editor-fold>                        

    private void jCheckBox1ItemStateChanged(java.awt.event.ItemEvent evt) {                                            
        if(evt.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
            Movil.DETENER = 25;
            aceleracion = 2;
        }
        else {
            Movil.DETENER = 50;
            aceleracion = 1;
        }
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Inex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Inex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Inex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Inex.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Inex().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
