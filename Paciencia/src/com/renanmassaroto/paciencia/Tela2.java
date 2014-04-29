package com.renanmassaroto.paciencia;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Tela2 extends JFrame implements MouseListener, MouseMotionListener, ActionListener{

	public static void main(String args[]) {
		Tela2 t = null;

		while(t == null) {
			try {
				t = new Tela2();
			}
			catch(IllegalStateException e) {
				System.out.println("Problemas durante a criação dos buffers, tente novamente.");
				System.out.println(e.getMessage());
				t = null;
			}
		}
	}

	Carta[] baralho = new Carta[52];
	FilaPaciencia filaMonte = new FilaPaciencia(24);
	PilhaPaciencia pilhaMonte = new PilhaPaciencia(24);

	PilhaPaciencia[] pilhaOrganizada = new PilhaPaciencia[4];
	ListaLigadaPaciencia[] listaLigadaColunas = new ListaLigadaPaciencia[7];

	BufferedImage cartaVerso, fundoTela;

	int mouseUltimaPosicaoX = 0, mouseUltimaPosicaoY = 0;
	int indiceOrigem = -1, indiceDestino = -1, indiceCartaSelecionada = -1;

	boolean pilhaMonteSelecionada = false;
	boolean fimDeJogo = false;

	JMenuBar barraDeMenus;
	JMenu menuJogo;
	JMenuItem itensDoMenu;

	public Tela2() {

		barraDeMenus = new JMenuBar();

		menuJogo = new JMenu("Jogo");
		barraDeMenus.add(menuJogo);

		itensDoMenu = new JMenuItem("Novo jogo");
		itensDoMenu.addActionListener(this);

		menuJogo.add(itensDoMenu);

		menuJogo.addSeparator();

		itensDoMenu = new JMenuItem("Sair");
		itensDoMenu.addActionListener(this);

		menuJogo.add(itensDoMenu);

		this.setJMenuBar(barraDeMenus);

		iniciarJogo();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(720, 600);
		this.setResizable(false);

		this.getContentPane().addMouseListener(this);
		this.getContentPane().addMouseMotionListener(this);

		this.setVisible(true);
		this.createBufferStrategy(2);
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		JMenuItem origem = (JMenuItem)arg0.getSource();

		origem.setArmed(false);

		if(origem.getText() == "Novo jogo") {
			this.iniciarJogo();
		}
		if(origem.getText() == "Sair") {
			System.exit(0);
		}
	}

	public BufferedImage carregarImagem(String NomeDaImagem) {
		BufferedImage imagem = null;
		try {
			imagem = ImageIO.read(ClassLoader.getSystemResource(NomeDaImagem + ".png"));
		} catch (IOException e) {
			try {
				imagem = ImageIO.read(ClassLoader.getSystemResource("Erro.png"));
			} catch (IOException e1) {
				imagem = new BufferedImage(71, 96, BufferedImage.TYPE_INT_RGB);
				e1.printStackTrace();
			}
		}
		return imagem;
	}

	public void calculaIndiceDestino(MouseEvent arg0) {
		int j = 7;

		for(; j >= 1; j--) {
			int x = 618 - ((7 - j) * 101);
			if(arg0.getX() >= x && arg0.getX() <= (x + 70))
				indiceDestino = j - 1;
		}
	}

	public void construirBaralho() {
		int p = 0;
		for(int i = 1; i <=13; i++) {
			for(int j = 0; j < 4; j++) {
				switch (j) {
				case 0:
					baralho[p] = new Carta(i, 'P');
					baralho[p].sprite = carregarImagem(i + "P");
					break;
				case 1:
					baralho[p] = new Carta(i, 'C');
					baralho[p].sprite = carregarImagem(i + "C");
					break;
				case 2:
					baralho[p] = new Carta(i, 'E');
					baralho[p].sprite = carregarImagem(i + "E");
					break;
				case 3:
					baralho[p] = new Carta(i, 'O');
					baralho[p].sprite = carregarImagem(i + "O");
					break;
				}
				p++;
			}
		}
		cartaVerso = carregarImagem("Verso");
		fundoTela = carregarImagem("fundo");
	}

	public void iniciarJogo() {
		baralho = new Carta[52];
		pilhaOrganizada = new PilhaPaciencia[4];
		filaMonte = new FilaPaciencia(24);
		pilhaMonte = new PilhaPaciencia(24);
		listaLigadaColunas = new ListaLigadaPaciencia[7];
		cartaVerso = null;
		fundoTela = null;

		reiniciarPonteiros();

		pilhaMonteSelecionada = false;

		for(int i = 0; i < 7; i++) {
			listaLigadaColunas[i] = new ListaLigadaPaciencia();
			if(i < 4)
				pilhaOrganizada[i] = new PilhaPaciencia(13);
		}

		construirBaralho();
		embaralharCartas(baralho, 108);
		distribuirCartas();

		this.update(this.getGraphics());
	}

	public void distribuirCartas() {
		for(int i = 0; i < 52; i++) {
			if(i < 24) {
				baralho[i].Position = new Point(20, 80);
				filaMonte.Enfileirar(baralho[i]);
			}
			else {
				NoPaciencia no = new NoPaciencia();

				if(listaLigadaColunas[0].ContarNos() < 1) {
					baralho[i].Position = new Point(20, ((i - 24) * 20 + 206));
					no.c = baralho[i];
					no.status = true;
					listaLigadaColunas[0].InserirFinal(no);
				}
				else {
					if(listaLigadaColunas[1].ContarNos() < 2) {
						baralho[i].Position = new Point(121, ((i - 25) * 20 + 206));
						no.c = baralho[i];
						if(listaLigadaColunas[1].ContarNos() < 1)
							no.status = false;
						else
							no.status = true;

						listaLigadaColunas[1].InserirFinal(no);
					}
					else {
						if(listaLigadaColunas[2].ContarNos() < 3) {
							baralho[i].Position = new Point(222, ((i - 27) * 20 + 206));
							no.c = baralho[i];
							if(listaLigadaColunas[2].ContarNos() < 2)
								no.status = false;
							else
								no.status = true;

							listaLigadaColunas[2].InserirFinal(no);
						}
						else {
							if(listaLigadaColunas[3].ContarNos() < 4) {
								baralho[i].Position = new Point(323, ((i - 30) * 20 + 206));
								no.c = baralho[i];
								if(listaLigadaColunas[3].ContarNos() < 3)
									no.status = false;
								else
									no.status = true;

								listaLigadaColunas[3].InserirFinal(no);
							}
							else {
								if(listaLigadaColunas[4].ContarNos() < 5) {
									baralho[i].Position = new Point(424, ((i - 34) * 20 + 206));
									no.c = baralho[i];
									if(listaLigadaColunas[4].ContarNos() < 4)
										no.status = false;
									else
										no.status = true;

									listaLigadaColunas[4].InserirFinal(no);
								}
								else {
									if(listaLigadaColunas[5].ContarNos() < 6) {
										baralho[i].Position = new Point(525, ((i - 39) * 20 + 206));
										no.c = baralho[i];
										if(listaLigadaColunas[5].ContarNos() < 5)
											no.status = false;
										else
											no.status = true;

										listaLigadaColunas[5].InserirFinal(no);
									}
									else {
										if(listaLigadaColunas[6].ContarNos() < 7) {
											baralho[i].Position = new Point(626, ((i - 45) * 20 + 206));
											no.c = baralho[i];
											if(listaLigadaColunas[6].ContarNos() < 6)
												no.status = false;
											else
												no.status = true;

											listaLigadaColunas[6].InserirFinal(no);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public Carta[] embaralharCartas(Carta[] c, int qtdeEmbaralhar) {
		int aux1, aux2;
		Carta c1, c2;
		if(qtdeEmbaralhar > 0) {

			aux1 = (int) (Math.random() * 52);
			aux2 = (int) (Math.random() * 52);
			c1 = c[aux1];
			c2 = c[aux2];
			c[aux1] = c2;
			c[aux2] = c1;

			return embaralharCartas(c, qtdeEmbaralhar - 1);
		}
		else {
			return c;
		}
	}

	public void jogoTerminado() {
		Icon icone = new ImageIcon("imgs/fimdejogo.gif");
		
		int RespostaUsuario = JOptionPane.showConfirmDialog(this, "Você venceu!\nDeseja jogar novamente?", "Fim do jogo", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, icone);
		switch(RespostaUsuario) {
		case 0:
			iniciarJogo();
			break;
		case 1:
			System.exit(0);
			break;
		case 2:
			break;
		default:
			System.exit(0);
		}
	}

	public void retornarCartaListaLigada() {
		listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c.Position = listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c.lastPosition;

		NoPaciencia NoTemp = listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada);

		while(NoTemp != null) {
			NoTemp.c.Position = NoTemp.c.lastPosition;

			NoTemp = NoTemp.prox;
		}
	}

	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {

		int moveX = 0, moveY = 0;

		if(mouseUltimaPosicaoX == 0 && mouseUltimaPosicaoY == 0) {
			mouseUltimaPosicaoX = arg0.getX();
			mouseUltimaPosicaoY = arg0.getY();
		}
		else {
			moveX = arg0.getX() - mouseUltimaPosicaoX;
			moveY = arg0.getY() - mouseUltimaPosicaoY;
		}

		if(indiceOrigem != -1 && indiceCartaSelecionada != -1) {
			int CartasParaAtualizar = listaLigadaColunas[indiceOrigem].ContarNos();

			for(int i = indiceCartaSelecionada; i <= CartasParaAtualizar; i++) {
				double x, y;

				x = (listaLigadaColunas[indiceOrigem].BuscarNo(i).c.Position.getX() + moveX);

				y = (listaLigadaColunas[indiceOrigem].BuscarNo(i).c.Position.getY() + moveY) + (((i + 1) - (indiceCartaSelecionada + 1)) * 20);
				y = (listaLigadaColunas[indiceOrigem].BuscarNo(i).c.Position.getY() + moveY);

				listaLigadaColunas[indiceOrigem].BuscarNo(i).c.Position = new Point((int)x, (int)y);
			}
		}
		else {
			if(pilhaMonteSelecionada) {
				double x, y;

				x = arg0.getX();
				y = arg0.getY();

				x = (pilhaMonte.ElementoTopo().Position.getX() + moveX);

				y = (pilhaMonte.ElementoTopo().Position.getY() + moveY);
				y = (pilhaMonte.ElementoTopo().Position.getY() + moveY);

				pilhaMonte.ElementoTopo().Position = new Point((int)x, (int)y);
			}
		}
		mouseUltimaPosicaoX = arg0.getX();
		mouseUltimaPosicaoY = arg0.getY();
		update(getGraphics());
	}

	public void mouseEntered(MouseEvent arg0) {

	}

	public void mouseExited(MouseEvent arg0) {

	}

	public void mouseMoved(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {
		if(arg0.getY() >= 158) {
			if(indiceOrigem == -1) {
				int i = 20, j = 7;

				for(; j >= 1; j--) {
					int x = 618 - ((7 - j) * 101);
					if(arg0.getX() >= x && arg0.getX() <= (x + 70)) {
						indiceOrigem = j - 1;
					}
				}

				if(indiceOrigem >= 0) {

					if(listaLigadaColunas[indiceOrigem].ContarNos() > 0) {

						for(; i > 0; i--) {

							int y = (568 - 30) - ((20 - i) * 20);

							if(arg0.getY() >= y && arg0.getY() <= (y + 95)) {
								if(listaLigadaColunas[indiceOrigem].BuscarNo(i) != null) {
									if(listaLigadaColunas[indiceOrigem].BuscarNo(i).status) {
										indiceCartaSelecionada = i;
										i = 0;
									}
								}
							}
						}

						if(indiceCartaSelecionada != -1) {
							if(arg0.getButton() == 1) {
								listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c.lastPosition = listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c.Position;
							}
							else {
								if(arg0.getButton() == 3) {
									if(listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).prox == null) {
										for(i = 0; i < 4; i++) {
											if(inserirPilha(pilhaOrganizada[i], listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c)) {
												listaLigadaColunas[indiceOrigem].Remover(pilhaOrganizada[i].ElementoTopo());
												i = 4;
												try {
													listaLigadaColunas[indiceOrigem].ElementoFim().status = true;
												}
												catch(Exception e) {

												}
											}
										}
										reiniciarPonteiros();
									}
									reiniciarPonteiros();
								}
								reiniciarPonteiros();
							}
						}
						else {
							reiniciarPonteiros();
						}
					}
					else {
						reiniciarPonteiros();
					}

				}
			}
		}
		else {
			if(arg0.getY() >= 32 && arg0.getY() <= 127) {
				if(arg0.getX() >= 12 && arg0.getX() <= 82) {
					if(!filaMonte.FilaVazia()) {
						filaMonte.ElementoInicio().Position = new Point(101, 80);
						pilhaMonte.Empilhar(filaMonte.Desenfileirar());
					}
					else {
						PilhaPaciencia auxiliaOrdenacao = new PilhaPaciencia(24);
						while(!pilhaMonte.PilhaVazia()) {
							auxiliaOrdenacao.Empilhar(pilhaMonte.Desempilhar());
						}
						while(!auxiliaOrdenacao.PilhaVazia()) {
							filaMonte.Enfileirar(auxiliaOrdenacao.Desempilhar());
						}
						auxiliaOrdenacao = null;
					}
				}
				else {
					if(arg0.getX() >= 98 && arg0.getX() <= 168) {
						if(!pilhaMonte.PilhaVazia()) {
							if(arg0.getButton() == 1) {
								pilhaMonteSelecionada = true;
								pilhaMonte.ElementoTopo().lastPosition = pilhaMonte.ElementoTopo().Position;
							}
							else {
								if(arg0.getButton() == 3) {
									for(int i = 0; i < 4; i++) {
										if(inserirPilha(pilhaOrganizada[i], pilhaMonte.ElementoTopo())) {
											pilhaMonte.Desempilhar();
											pilhaMonteSelecionada = false;
											i = 4;
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}


	public void mouseReleased(MouseEvent arg0) {
		if(indiceOrigem != -1 && indiceCartaSelecionada != -1) {
			calculaIndiceDestino(arg0);

			if(arg0.getY() >= 158) {
				if(indiceDestino != -1) {
					if(validaInsercaoListaLigada(listaLigadaColunas[indiceDestino], listaLigadaColunas[indiceOrigem].BuscarNo(indiceCartaSelecionada).c)) {
						movimentaListaLigadaParaListaLigada(listaLigadaColunas[indiceOrigem], listaLigadaColunas[indiceDestino]);
					}
					else {
						retornarCartaListaLigada();
					}
				}
				else {
					retornarCartaListaLigada();
				}
			}
			else {
				if(arg0.getY() >= 33 && arg0.getY() <= 128) {
					if(indiceDestino > 2 && indiceDestino < 7) {
						movimentaListaLigadaParaPilha(listaLigadaColunas[indiceOrigem], pilhaOrganizada[indiceDestino - 3]);
					}
					else {
						retornarCartaListaLigada();
					}
				}
				else {
					retornarCartaListaLigada();
				}
			}

		}
		else {
			if(pilhaMonteSelecionada) {
				if(arg0.getY() >= 158) {
					calculaIndiceDestino(arg0);

					if(indiceDestino != -1) {
						if(validaInsercaoListaLigada(listaLigadaColunas[indiceDestino], pilhaMonte.ElementoTopo())) {
							movimentaPilhaParaListaLigada(pilhaMonte, listaLigadaColunas[indiceDestino]);
							pilhaMonteSelecionada = false;
						}
						else {
							retornarCartaPilhaMonte();
						}
					}
					else {
						retornarCartaPilhaMonte();
					}
				}
				else {
					if(arg0.getX() >= 323 && arg0.getX() <= 696) {
						if(arg0.getY() >= 33 && arg0.getY() <= 128) {
							calculaIndiceDestino(arg0);

							if(indiceDestino > 2 && indiceDestino < 7) {
								if(inserirPilha(pilhaOrganizada[indiceDestino - 3], pilhaMonte.ElementoTopo())) {
									pilhaMonte.Desempilhar();
									pilhaMonteSelecionada = false;
								}
								else {
									retornarCartaPilhaMonte();
								}

							}
							else {
								retornarCartaPilhaMonte();
							}
						}
						else {
							retornarCartaPilhaMonte();
						}
					}
					else {
						retornarCartaPilhaMonte();
					}
				}
			}
		}
		reiniciarPonteiros();

		mouseUltimaPosicaoX = 0;
		mouseUltimaPosicaoY = 0;
		update(getGraphics());
	}

	public void movimentaListaLigadaParaListaLigada(ListaLigadaPaciencia origem, ListaLigadaPaciencia destino) {
		boolean trocaEfetuada = false;

		if(!origem.ListaVazia()) {
			NoPaciencia NoTemp = origem.primeiro;
			NoPaciencia NoProx = NoTemp.prox;

			for(int i = 1; i <= origem.ContarNos(); i++) {
				if(NoTemp.status) {
					try {
						while(validaInsercaoListaLigada(destino, NoTemp.c)) {

							origem.Remover(NoTemp.c);

							Point2D p;

							if(destino.ListaVazia()) {

								double x, y;

								x = 626 - ((6 - indiceDestino) * 101);
								y = 206;

								p = new Point((int)x, (int)y);
							}
							else {
								p = new Point((int)destino.ultimo.c.Position.getX(), (int)(destino.ultimo.c.Position.getY() + 20));
							}

							NoTemp.c.Position = p;
							NoTemp.c.lastPosition = p;

							destino.InserirFinal(NoTemp);

							try {
								NoTemp = NoProx;
								NoProx = NoTemp.prox;
							} catch (Exception e) {
								NoTemp = null;
								NoProx = null;
							}

							trocaEfetuada = true;
						}
					}
					catch(NullPointerException e) {

					}
				}

				try {
					NoTemp = NoProx;
					NoProx = NoTemp.prox;
				} catch (Exception e) {
					NoTemp = null;
					NoProx = null;
				}
			}
			if(trocaEfetuada)
				if(!origem.ListaVazia())
					origem.ultimo.status = true;
		}
	}

	public void movimentaListaLigadaParaPilha(ListaLigadaPaciencia origem, PilhaPaciencia destino) {
		if(inserirPilha(destino, origem.BuscarNo(indiceCartaSelecionada).c)) {
			origem.Remover(origem.BuscarNo(indiceCartaSelecionada).c);

			try {
				origem.ultimo.status = true;
			}
			catch (Exception e) {

			}
		}
		else {
			retornarCartaListaLigada();
		}
	}

	public void movimentaPilhaParaListaLigada(PilhaPaciencia origem, ListaLigadaPaciencia destino) {
		if(validaInsercaoListaLigada(destino, origem.ElementoTopo())) {
			NoPaciencia NoTemp = new NoPaciencia();
			NoTemp.c = origem.Desempilhar();
			NoTemp.status = true;

			int x, y;

			if(listaLigadaColunas[indiceDestino].ListaVazia()) {
				x = 626 - ((6 - indiceDestino) * 101);
				y = 206;
			}
			else {
				x = (int)listaLigadaColunas[indiceDestino].ElementoFim().c.Position.getX();
				y = (int)(listaLigadaColunas[indiceDestino].ElementoFim().c.Position.getY() + 20);
			}

			NoTemp.c.Position = new Point(x, y);
			NoTemp.c.lastPosition = NoTemp.c.Position;

			destino.InserirFinal(NoTemp);
		}
	}

	public void paint(Graphics g) {
		BufferStrategy bf = this.getBufferStrategy();

		try {
			g = bf.getDrawGraphics();

			super.paint(g);
			
			
			g = desenharTela(g);

			g.dispose();
			bf.show();

			Toolkit.getDefaultToolkit().sync();
		}
		catch (Exception e) {

		}

		if(!fimDeJogo)
			verificaFimDoJogo();
	}
	
	public Graphics desenharTela(Graphics g) {
		g.drawImage(fundoTela, 0, 47, null);

		if(!pilhaOrganizada[0].PilhaVazia()) {
			g.drawImage(pilhaOrganizada[0].ElementoTopo().sprite, 323, 80, null);
		}
		if(!pilhaOrganizada[1].PilhaVazia()) {
			g.drawImage(pilhaOrganizada[1].ElementoTopo().sprite, 424, 80, null);
		}
		if(!pilhaOrganizada[2].PilhaVazia()) {
			g.drawImage(pilhaOrganizada[2].ElementoTopo().sprite, 525, 80, null);
		}
		if(!pilhaOrganizada[3].PilhaVazia()) {
			g.drawImage(pilhaOrganizada[3].ElementoTopo().sprite, 626, 80, null);
		}

		if(indiceCartaSelecionada != -1 && indiceOrigem != -1) {
			if(!filaMonte.FilaVazia()) {
				g.drawImage(cartaVerso, 20, 80, null);
			}

			if(!pilhaMonte.PilhaVazia()) {
				g.drawImage(pilhaMonte.ElementoTopo().sprite, (int)pilhaMonte.ElementoTopo().Position.getX(), (int)pilhaMonte.ElementoTopo().Position.getY(), null);
			}

			for(int i = 0; i < 7; i++) {
				if(indiceOrigem != i) {
					for(int j = 1; j <= listaLigadaColunas[i].ContarNos(); j++) {
						if(listaLigadaColunas[i].BuscarNo(j).status) {
							g.drawImage(listaLigadaColunas[i].BuscarNo(j).c.sprite, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
						else {
							g.drawImage(cartaVerso, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
					}
				}
			}
			for(int j = 1; j <= listaLigadaColunas[indiceOrigem].ContarNos(); j++) {
				if(listaLigadaColunas[indiceOrigem].BuscarNo(j).status) {
					g.drawImage(listaLigadaColunas[indiceOrigem].BuscarNo(j).c.sprite, (int)listaLigadaColunas[indiceOrigem].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[indiceOrigem].BuscarNo(j).c.Position.getY(), null);
				}
				else {
					g.drawImage(cartaVerso, (int)listaLigadaColunas[indiceOrigem].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[indiceOrigem].BuscarNo(j).c.Position.getY(), null);
				}
			}
		}
		else {
			if(pilhaMonteSelecionada) {
				for(int i = 0; i < 7; i++) {
					for(int j = 1; j <= listaLigadaColunas[i].ContarNos(); j++) {
						if(listaLigadaColunas[i].BuscarNo(j).status) {
							g.drawImage(listaLigadaColunas[i].BuscarNo(j).c.sprite, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
						else {
							g.drawImage(cartaVerso, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
					}
				}
				if(!filaMonte.FilaVazia()) {
					g.drawImage(cartaVerso, 20, 80, null);
				}
				if(!pilhaMonte.PilhaVazia()) {
					g.drawImage(pilhaMonte.ElementoTopo().sprite, (int)pilhaMonte.ElementoTopo().Position.getX(), (int)pilhaMonte.ElementoTopo().Position.getY(), null);
				}
			}
			else {
				if(!filaMonte.FilaVazia()) {
					g.drawImage(cartaVerso, 20, 80, null);
				}
				if(!pilhaMonte.PilhaVazia()) {
					g.drawImage(pilhaMonte.ElementoTopo().sprite, (int)pilhaMonte.ElementoTopo().Position.getX(), (int)pilhaMonte.ElementoTopo().Position.getY(), null);
				}
				for(int i = 0; i < 7; i++) {
					for(int j = 1; j <= listaLigadaColunas[i].ContarNos(); j++) {
						if(listaLigadaColunas[i].BuscarNo(j).status) {
							g.drawImage(listaLigadaColunas[i].BuscarNo(j).c.sprite, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
						else {
							g.drawImage(cartaVerso, (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getX(), (int)listaLigadaColunas[i].BuscarNo(j).c.Position.getY(), null);
						}
					}
				}
			}
		}
		return g;
	}
	
	public void retornarCartaPilhaMonte() {
		pilhaMonte.ElementoTopo().Position = pilhaMonte.ElementoTopo().lastPosition;
		pilhaMonteSelecionada = false;
	}

	public void reiniciarPonteiros() {
		indiceCartaSelecionada = -1;
		indiceDestino = -1;
		indiceOrigem = -1;
	}

	public boolean validaInsercaoListaLigada(ListaLigadaPaciencia destino, Carta carta) {
		if(!destino.ListaVazia()) {
			if((destino.ultimo.c.valor - 1) == carta.valor) {
				if((destino.ultimo.c.naipe == 'P' || destino.ultimo.c.naipe == 'E') && (carta.naipe == 'C' || carta.naipe == 'O')) {
					return true;
				}
				else {
					if((destino.ultimo.c.naipe == 'C' || destino.ultimo.c.naipe == 'O') && (carta.naipe == 'P' || carta.naipe == 'E')) {
						return true;
					}
				}
			}
			else {
				return false;
			}
		}
		else {
			if(carta.valor == 13) {
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}

	public boolean inserirPilha(PilhaPaciencia destino, Carta carta) {
		if(destino.PilhaVazia() && carta.valor == 1) {
			if(destino.Empilhar(carta) != true) {
				return false;
			}
		}
		else {
			if(destino.PilhaVazia() != true && (destino.ElementoTopo().valor == carta.valor - 1) && destino.ElementoTopo().naipe == carta.naipe) {
				if(destino.Empilhar(carta) != true) {
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}

	public void verificaFimDoJogo() {
		fimDeJogo = true;

		for(int i = 0; i < 4; i++)
			fimDeJogo = fimDeJogo && pilhaOrganizada[i].PilhaCheia();

		if(fimDeJogo)
			jogoTerminado();
	}
}