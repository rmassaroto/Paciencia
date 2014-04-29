package com.renanmassaroto.paciencia;

public class FilaPaciencia {

	int tam, inicio, fim, total;
	Carta[] cartas;

	public FilaPaciencia(int t) {
		tam = t;
		cartas = new Carta[tam];
		inicio = 0;
		fim = 0;
	}

	public boolean FilaVazia() {
		if(total <= 0) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean FilaCheia() {
		if(total >= tam) {
			return true;
		}
		else {
			return false;
		}
	}

	public Carta ElementoInicio() {
		if(!FilaVazia()) {
//			System.out.println("Fila vai devolver a carta " + cartas[inicio].valor + cartas[inicio].naipe);
			return cartas[inicio];
		}
		else {
//			System.out.println("Fila não tem cartas para devolver");
			return null;
		}
	}

	public Carta ElementoFim() {
		if(!FilaVazia()) {
			return cartas[fim];
		}
		else {
			return null;
		}
	}

	public void Enfileirar(Carta c) {
		if(!FilaCheia()) {
			cartas[fim] = c;
			fim++;
			total++;
			if(fim == tam) {
				fim = 0;
			}
		}
		else {
			System.out.println("Fila cheia! Carta nao empilhada");
		}
	}

	public Carta Desenfileirar() {
		if(!FilaVazia()) {
			Carta aux;
			aux = cartas[inicio];
			inicio++;
			total--;
			if(inicio == tam) {
				inicio = 0;
			}
			return aux;
		}
		return null;
	}

	public void imprimirFila() {
		if(!FilaVazia()) {
			int i, j;
			j = inicio;
			for(i = 0; i < total; i++) {
				if(j == tam) {
					j = 0;
				}
				System.out.println("Posicao " + i + " da fila: " + cartas[j].valor + cartas[j].naipe);
				j++;
			}
		}
		else {
			System.out.println("Fila vazia");
		}
	}

//	public static void main(String args[]) {
//		Fila f = new Fila(3);
//		f.imprimirFila();
//		f.Enfileirar(new Carta(3,'E'));
//		f.imprimirFila();
//		f.Enfileirar(new Carta(6, 'C'));
//		f.Enfileirar(new Carta(7, 'O'));
//		f.imprimirFila();
//		f.Enfileirar(new Carta(8, 'P'));
//		f.imprimirFila();
//		f.Desenfileirar();
//		f.Desenfileirar();
//		f.Enfileirar(new Carta(1, 'C'));
//		f.imprimirFila();
//	}
}
