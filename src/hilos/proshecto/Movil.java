/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilos.proshecto;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author Nicol Acosta
 */
public class Movil extends Thread {
    protected static int DETENER = 50;
    protected int velocidad, x, y, w, h, vuelo = -1;
    protected Inex i;
    protected static Random rand = new Random();
    protected Color col;
    protected boolean salida = true;
    public Movil(Inex i, int x, int y, int w, int h) {
        this.i = i;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        velocidad = rand.nextInt(6)+4;
        col = new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
    }
    @Override
    public void run() {
        try {
            mover(100, true);
            mover(470, false);
            int mover = 30, mov = i.fila();
            for (int j = 0; j < mov; j++) mover += 40;
            mover(mover, true);
            mover(380, false);
            i.atender(mov, (rand.nextInt(10)+1)*1000);
            vuelo = rand.nextInt(5);
            if(mov < 13) mover(410, false);
            mover(600, true);
            mover(270, false);
            mover = 0;
            mov = -1;
            int moverx = 20, movery = 60;
            while(mov < 0) mov = i.checarAsiento(mover++);
            mover--;
            for (int j = 0; j < mover; j++) moverx += 30; 
            for (int j = 0; j < mov; j++) movery += 30;
            mover(moverx, true);
            mover(movery, false);
            while(salida) System.out.print("");
            i.salida(mover, mov);
            mover(240, false);
            mover(600, true);
            mover(150, false);
            mover(620, true);
            Thread.sleep(500);
            mover(800, true);
            i.salir(this);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
    public void mover(int pos, boolean derecha) throws InterruptedException {
        int diferencia = 0;
        while(true) {
            diferencia = (derecha? x:y)-pos;
            if(diferencia < 5 && diferencia > -5) {
                break;
            }
            if(diferencia < 0) {
                if(derecha) x = x + velocidad;
                else y = y + velocidad;
            }
            else {
                if(derecha) x = x - velocidad;
                else y = y - velocidad;
            }
            Thread.sleep(DETENER);
        }
    }
    public void paint(Graphics2D g) {
        g.setColor(col);
        g.setStroke(new BasicStroke(3));
        g.fillRect(x, y, w, h);
        g.setColor(Color.black);
        g.drawString(vuelo + "", x+5, y+15);
    }
}
