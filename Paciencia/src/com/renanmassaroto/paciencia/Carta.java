package com.renanmassaroto.paciencia;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

public class Carta {

	public int valor;
	public char naipe;
	public BufferedImage sprite = null;
	public boolean IsMoving = false;
	public Point2D lastPosition = null;
	public Point2D Position = null;
	
	public Carta(int v, char n) {
		valor = v;
		naipe = n;
	}
	
//	Custom
	public Carta (int v, char n, BufferedImage img) {
		valor = v;
		naipe = n;
		sprite = img;
	}
	
	public BufferedImage obterSprite() {
		return sprite;
	}
}
