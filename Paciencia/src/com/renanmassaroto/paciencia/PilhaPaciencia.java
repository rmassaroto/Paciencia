package com.renanmassaroto.paciencia;

public class PilhaPaciencia {
	int tam;
	Carta vet[];
	int topo = -1;
	
	public PilhaPaciencia(int t) {
		tam = t;
		vet = new Carta[tam];
	}
	
	public boolean PilhaVazia() {
		if(topo < 0) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean PilhaCheia() {
		if(topo < (tam - 1)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	public Carta ElementoTopo() {
		if(PilhaVazia() != true) {
			return vet[topo];
		}
		else {
			return null;
		}
	}
	
	public boolean Empilhar(Carta elem) {
		if(PilhaCheia()) {
			System.out.println("Pilha cheia");
			return false;
		}
		else {
			topo++;
			vet[topo] = elem;
			return true;
		}
	}
	
	public Carta Desempilhar() {
		if(PilhaVazia()) {
			System.out.println("Pilha Vazia");
			return null;
		}
		else {
			topo--;
			return vet[topo + 1];
		}
	}
	
	public void MostrarPilha() {
		int i = topo;
		if(i >= 0) {
			System.out.println(i + "Carta " + vet[i].valor + "" + vet[i].naipe);
			i--;
		}
	}
}
