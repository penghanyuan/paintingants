package org.polytechtours.performance.tp.fourmispeintre;
// package PaintingAnts_v2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

// version : 2.0

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

/**
 * <p>
 * Titre : Painting Ants
 * </p>
 * <p>
 * Description :
 * </p>
 * <p>
 * Copyright : Copyright (c) 2003
 * </p>
 * <p>
 * Société : Equipe Réseaux/TIC - Laboratoire d'Informatique de l'Université de
 * Tours
 * </p>
 *
 * @author Nicolas Monmarché
 * @version 1.0
 */

public class CPainting extends Canvas implements MouseListener {
    private static final long serialVersionUID = 1L;
    // matrice servant pour le produit de convolution
    private static int[][] mMatriceConv9 = {{1, 2, 1}, {2, 4, 2}, {1, 2, 1}};
    private static int[][] mMatriceConv25 = {{1, 1, 2, 1, 1}, {1, 2, 3, 2, 1}, {2, 3, 4, 3, 2}, {1, 2, 3, 2, 1}, {1, 1, 2, 1, 1}};
    private static int[][] mMatriceConv49 =
            {{1, 1, 2, 2, 2, 1, 1},
                    {1, 2, 3, 4, 3, 2, 1},
                    {2, 3, 4, 5, 4, 3, 2},
                    {2, 4, 5, 8, 5, 4, 2},
                    {2, 3, 4, 5, 4, 3, 2},
                    {1, 2, 3, 4, 3, 2, 1},
                    {1, 1, 2, 2, 2, 1, 1}};
    // Objet de type Graphics permettant de manipuler l'affichage du Canvas
    private Graphics mGraphics;
    // Objet ne servant que pour les bloc synchronized pour la manipulation du
    // tableau des couleurs
    private Object mMutexCouleurs = new Object();
    // tableau des couleurs, il permert de conserver en memoire l'état de chaque
    // pixel du canvas, ce qui est necessaire au deplacemet des fourmi
    // il sert aussi pour la fonction paint du Canvas
    private Color[] mCouleurs;
    // couleur du fond
    private Color mCouleurFond = new Color(255, 255, 255);
    // dimensions
    private Dimension mDimension;

    private PaintingAnts mApplis;

    private BufferStrategy bs;

    private boolean mSuspendu = false;

    /******************************************************************************
     * Titre : public CPainting() Description : Constructeur de la classe
     ******************************************************************************/
    public CPainting(Dimension pDimension, PaintingAnts pApplis) {
        int i, j;
        addMouseListener(this);


        mApplis = pApplis;

        mDimension = pDimension;
        setBounds(new Rectangle(0, 0, mDimension.width, mDimension.height));

        this.setBackground(mCouleurFond);

        // initialisation de la matrice des couleurs
        mCouleurs = new Color[mDimension.width * mDimension.height];
        synchronized (mMutexCouleurs) {
            for (i = 0; i != mDimension.width; i++) {
                for (j = 0; j != mDimension.height; j++) {
                    mCouleurs[i * mDimension.width + j] = new Color(mCouleurFond.getRed(), mCouleurFond.getGreen(), mCouleurFond.getBlue());
                }
            }
        }

    }

    /******************************************************************************
     * Titre : Color getCouleur Description : Cette fonction renvoie la couleur
     * d'une case
     ******************************************************************************/
    public Color getCouleur(int x, int y) {

        return mCouleurs[x * mDimension.width + y];

    }

    /******************************************************************************
     * Titre : Color getDimension Description : Cette fonction renvoie la
     * dimension de la peinture
     ******************************************************************************/
    public Dimension getDimension() {
        return mDimension;
    }

    /******************************************************************************
     * Titre : Color getHauteur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getHauteur() {
        return mDimension.height;
    }

    /******************************************************************************
     * Titre : Color getLargeur Description : Cette fonction renvoie la hauteur de
     * la peinture
     ******************************************************************************/
    public int getLargeur() {
        return mDimension.width;
    }

    /******************************************************************************
     * Titre : void init() Description : Initialise le fond a la couleur blanche
     * et initialise le tableau des couleurs avec la couleur blanche
     ******************************************************************************/
    public void init() {
        int i, j;


        for (i = 0; i != mDimension.width; i++) {
            for (j = 0; j != mDimension.height; j++) {
                mCouleurs[i * mDimension.width + j] = new Color(mCouleurFond.getRed(), mCouleurFond.getGreen(), mCouleurFond.getBlue());
            }
        }


        mSuspendu = false;

    }

    /****************************************************************************/
    public void mouseClicked(MouseEvent pMouseEvent) {
        pMouseEvent.consume();
        if (pMouseEvent.getButton() == MouseEvent.BUTTON1) {
            // double clic sur le bouton gauche = effacer et recommencer
            if (pMouseEvent.getClickCount() == 2) {
                init();
            }
            // simple clic = suspendre les calculs et l'affichage
            mApplis.pause();
        } else {
            // bouton du milieu (roulette) = suspendre l'affichage mais
            // continuer les calculs
            if (pMouseEvent.getButton() == MouseEvent.BUTTON2) {
                suspendre();
            } else {
                // clic bouton droit = effacer et recommencer
                // case pMouseEvent.BUTTON3:
                init();
            }
        }
    }

    /****************************************************************************/
    public void mouseEntered(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mouseExited(MouseEvent pMouseEvent) {
    }

    /****************************************************************************/
    public void mousePressed(MouseEvent pMouseEvent) {

    }

    /****************************************************************************/
    public void mouseReleased(MouseEvent pMouseEvent) {
    }

    /******************************************************************************
     * Titre : void paint(Graphics g) Description : Surcharge de la fonction qui
     * est appelé lorsque le composant doit être redessiné
     ******************************************************************************/
    @Override
    public void paint(Graphics pGraphics) {
        int i, j;

        for (i = 0; i < mDimension.width; i++) {
            for (j = 0; j < mDimension.height; j++) {
                pGraphics.setColor(mCouleurs[i * mDimension.width + j]);
                pGraphics.fillRect(i, j, 1, 1);
            }
        }

    }

    /******************************************************************************
     * Titre : void colorer_case(int x, int y, Color c) Description : Cette
     * fonction va colorer le pixel correspondant et mettre a jour le tabmleau des
     * couleurs
     ******************************************************************************/
    public void setCouleur(int x, int y, Color c, int pTaille) {
        bs = getBufferStrategy();
        if(bs == null) { //True if our canvas has no buffer strategy (should only happen once when we first start the game)
            createBufferStrategy(1); //Create a buffer strategy using two buffers (double buffer the canvas)
            return ;
        }

        mGraphics = bs.getDrawGraphics();


        if (!mSuspendu) {
            // on colorie la case sur laquelle se trouve la fourmi
            mGraphics.setColor(c);
            mGraphics.fillRect(x, y, 1, 1);
        }

        mCouleurs[x * mDimension.width + y] = c;

        // on fait diffuser la couleur :
        switch (pTaille) {
            case 0:
                // on ne fait rien = pas de diffusion
                break;
            case 1:
                // produit de convolution discrete sur 9 cases
                convolution(x, y, 3);
                break;
            case 2:
                // produit de convolution discrete sur 25 cases
                convolution(x, y, 5);
                break;
            case 3:
                // produit de convolution discrete sur 49 cases
                convolution(x, y, 7);
                break;
        }// end switch

    }

    /******************************************************************************
     * Titre : setSupendu Description : Cette fonction change l'état de suspension
     ******************************************************************************/

    public void suspendre() {
        mSuspendu = !mSuspendu;
        if (!mSuspendu) {
            repaint();
        }
    }

    private void convolution(int x, int y, int dimension) {
        // produit de convolution discrete sur 9 cases
        int i, j, k, l, m, n;
        int R, G, B;
        Color lColor;

        for (i = 0; i < dimension; i++) {
            for (j = 0; j < dimension; j++) {
                R = G = B = 0;

                for (k = 0; k < dimension; k++) {
                    for (l = 0; l < dimension; l++) {
                        m = (x + i + k - (dimension - 1) + mDimension.width) % mDimension.width;
                        n = (y + j + l - (dimension - 1) + mDimension.height) % mDimension.height;
                        if (dimension == 3) {
                            R += CPainting.mMatriceConv9[k][l] * mCouleurs[m * mDimension.width + n].getRed();
                            G += CPainting.mMatriceConv9[k][l] * mCouleurs[m * mDimension.width + n].getGreen();
                            B += CPainting.mMatriceConv9[k][l] * mCouleurs[m * mDimension.width + n].getBlue();
                        } else if (dimension == 5) {
                            R += CPainting.mMatriceConv25[k][l] * mCouleurs[m * mDimension.width + n].getRed();
                            G += CPainting.mMatriceConv25[k][l] * mCouleurs[m * mDimension.width + n].getGreen();
                            B += CPainting.mMatriceConv25[k][l] * mCouleurs[m * mDimension.width + n].getBlue();
                        } else {
                            R += CPainting.mMatriceConv49[k][l] * mCouleurs[m * mDimension.width + n].getRed();
                            G += CPainting.mMatriceConv49[k][l] * mCouleurs[m * mDimension.width + n].getGreen();
                            B += CPainting.mMatriceConv49[k][l] * mCouleurs[m * mDimension.width + n].getBlue();
                        }

                    }
                }
                switch (dimension) {
                    case 3:
                        R = R / 16;
                        G = G / 16;
                        B = B / 16;
                        break;
                    case 5:
                        R = R / 44;
                        G = G / 44;
                        B = B / 44;
                        break;
                    case 7:
                        R = R / 128;
                        G = G / 128;
                        B = B / 128;
                        break;
                }
                lColor = new Color(R, G, B);

                mGraphics.setColor(lColor);

                m = (x + i - (dimension - 1) / 2 + mDimension.width) % mDimension.width;
                n = (y + j - (dimension - 1) / 2 + mDimension.height) % mDimension.height;
                mCouleurs[m * mDimension.width + n] = lColor;
                if (!mSuspendu) {
                    mGraphics.fillRect(m, n, 1, 1);
                    mGraphics.dispose(); //Dispose of our graphics object because it is no longer needed, and unnecessarily taking up memory
                    bs.show(); //Show the buffer strategy, flip it if necessary (make back buffer the visible buffer and vice versa)
                }
            }
        }

    }

}
