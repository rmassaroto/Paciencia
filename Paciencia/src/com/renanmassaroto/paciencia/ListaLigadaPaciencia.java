package com.renanmassaroto.paciencia;

public class ListaLigadaPaciencia {

	public NoPaciencia primeiro = null;
	public NoPaciencia ultimo = null;
	public boolean selected = false;

	public int BuscarNo(Carta elem) {
		int pos = 0; // No pseudo código esta variável não é inicializada com 0, mas para evitar mensagens de erro eu inicializei com o valor 0
		NoPaciencia aux = primeiro;
		
		while(aux != null) {
			pos = pos + 1;
			
			if((aux.c.valor + aux.c.naipe) == (elem.valor + elem.naipe)) {
				return pos;
			}
			aux = aux.prox;
		}
		return pos;
	}
	
	public NoPaciencia BuscarNo(int posicao) {
		if(!ListaVazia()) {
			if(posicao <= ContarNos()) {
				int pos = 1;
				NoPaciencia aux = primeiro;
				
				while(pos != posicao) {
					aux = aux.prox;
					pos++;
				}
				return aux;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}
	
	public int ContarNos() {
		NoPaciencia aux = primeiro;
		int cont = 0;

		while(aux != null) {
			aux = aux.prox;
			cont++;
		}
//		System.out.println("Qtde de nos encontrados " + cont);
		return cont;
	}
	
	public void Destruir() {
		primeiro = null;
		ultimo = null;
	}
	
	public NoPaciencia ElementoFim() {
		if(!ListaVazia()) {
			return ultimo;
		}
		else {
			return null;
		}
	}
	
	public NoPaciencia ElementoInicio() {
		if(!ListaVazia()) {
			return primeiro;
		}
		else {
			return null;
		}
	}
	
	public void InserirFinal(NoPaciencia elem) {
		if(ListaVazia()) {
			primeiro = elem;
			ultimo = elem;
		}
		else {
			ultimo.prox = elem;
			ultimo = elem;
//			ultimo.prox = null; // Eu  que fiz isso
		}
	}
	
	public void newInserirFinal(NoPaciencia elem) {
		if(ListaVazia()) {
			primeiro = elem;
		}
		else {
			ultimo.prox = elem;
		}
		ultimo = elem;
	}
	
	public void InserirInicio(NoPaciencia elem) {
		if(ListaVazia()) {
			primeiro = elem;
			ultimo = elem;
		}
		else {
			elem.prox = primeiro;
			primeiro = elem;
		}
	}
	
	public void InserirMeio(NoPaciencia NovoNo, int posicao) {
		NoPaciencia NoTemp = primeiro;
		int NroNos, PosAux = 1;
		
		NroNos = ContarNos();
		
		if(posicao <= 1) {
			InserirInicio(NovoNo);
		}
		else {
			if(posicao > NroNos) {
				InserirFinal(NovoNo);
			}
			else {
				while(PosAux < posicao) {
					NoTemp = NoTemp.prox;
					PosAux++;
				}
				NovoNo.prox = NoTemp.prox;
				NoTemp.prox = NovoNo;
			}
		}
	}
	
	public boolean ListaVazia() {
		if(primeiro == null || ultimo == null) {
//			System.out.println("Lista está vazia");
			return true;
		}
		else {
//			System.out.println("Lista não está vazia");
			return false;
		}
	}
	
	public void MostrarLista() {
		NoPaciencia aux = primeiro;
		
		while(aux != null) {
			System.out.println(aux.c.valor + aux.c.naipe);
			aux = aux.prox;
		}
	}
	
	public void Remover(Carta elem) {
		NoPaciencia NoTemp = primeiro;
		NoPaciencia NoAnt = null;
		
		if((primeiro.c.valor == elem.valor) && (primeiro.c.naipe == elem.naipe)) { //Provavelmente terei que mexer aqui
			primeiro = primeiro.prox;
		}
		else {
			while(NoTemp != null && ((NoTemp.c.valor != elem.valor) || (NoTemp.c.naipe != elem.naipe))) { //Provavelmente terei que mexer aqui
				NoAnt = NoTemp;
				NoTemp = NoTemp.prox;
			}
			if(NoTemp != null) {
				NoAnt.prox = NoTemp.prox;
			}
			if(NoTemp == ultimo) {
				ultimo = NoAnt;
			}
		}
		if(primeiro == null) {
			Destruir();
		}
	}
	
//	public void Remover(Carta elem) {
//		String log = "";
//		
//		log = log + "\nIniciando a remoção da carta " + elem.valor + elem.naipe;
//		log = log + "\nBuscando a posição da carta na ListaLigada";
//		
//		int pos = BuscarNo(elem), i;
//		
//		log = log + "\nPosição da carta " + pos;
//		
//		NoPaciencia aux = primeiro;
//		
//		log = log + "\npos(" + pos + ") != 0 && pos(" + pos + " <= ContarNos(" + ContarNos() + ")";
//		log = log + "\n" + (pos != 0) + " && " + (pos <= ContarNos());
//		
//		if(pos != 0 && pos <= ContarNos()) {
//		
//			log = log + "\nIniciando a movimentação do ponteiro na ListaLigada";
//			log = log + "\nfor(1; 1 <= (" + (pos - 2) + "); 1++)";
//			
//			for(i = 1; i <= (pos - 2); i++) {
//			
//				log = log + "\nfor(" + i + " = 1; " + i + " <= " + (pos - 2) + "; " + i + "++)";
//				
//				aux = aux.prox;
//			}
//			
//			log = log + "\nCarta removida da ListaLigada";
//			
//			boolean oldCode = true;
//			if(oldCode) {
//				aux.prox = aux.prox.prox;
//			}
//			else {
//				if(aux.prox != null) {
//					aux.prox = aux.prox.prox;
//				}
//				else {
//					Destruir();
//				}
//			}
//		}
//		
//		log = log + "\nfim";
//		
////		System.out.println(log);
//	}
}
