package com.proyectosfisi.game.contrawithbots;

/**
 * Created by MARISSA on 14/06/2015.
 */
public class Main {

    public static void main(String args[])
    {
        double[] d2 = {0.5,0.5,0.5,0.0};

        RNBot red = new RNBot();
        System.out.println("p:");
        long[] array = red.compute(d2);
        System.out.println(array[0]+","+array[1]+","+array[2]+","+array[3]+","+array[4]+","+array[5]);
    }
}
