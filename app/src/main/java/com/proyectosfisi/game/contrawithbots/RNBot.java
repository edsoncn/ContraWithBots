package com.proyectosfisi.game.contrawithbots;

import org.encog.engine.network.activation.ActivationSigmoid;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.data.NeuralDataSet;
import org.encog.neural.data.basic.BasicNeuralDataSet;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.encog.neural.networks.training.Train;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;

/**
 * Created by MARISSA on 13/06/2015.
 */
public class RNBot {

    private int numEntradas;
    private int numSalidas;

    private double ENTRADAS[][];
    private double SALIDAS[][];

    private BasicNetwork network;

    public RNBot(){

        numEntradas = 4;
        numSalidas = 6;

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, numEntradas));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, 5));
        network.addLayer(new BasicLayer(new ActivationSigmoid(), true, numSalidas));
        network.getStructure().finalizeStructure();
        network.reset();

        //cargar fff
        cargarEntradasYSalidas();

        NeuralDataSet trainingSet = new BasicNeuralDataSet(ENTRADAS, SALIDAS);

        Train train = new ResilientPropagation(network, trainingSet);

        int epoch = 1;

        do {

            train.iteration();
            System.out.println("Epoch #" + epoch + " Error:" + train.getError());
            epoch++;

        } while(train.getError() > 0.001 && epoch < 2000);

    }

    public long[] compute(double[] array){
        long[] arrayResult = new long[6];
        MLData input = new BasicMLData(array);
        MLData result = network.compute(input);
        arrayResult[0] = Math.round(result.getData(0));
        arrayResult[1] = Math.round(result.getData(1));
        arrayResult[2] = Math.round(result.getData(2));
        arrayResult[3] = Math.round(result.getData(3));
        arrayResult[4] = Math.round(result.getData(4));
        arrayResult[5] = Math.round(result.getData(5));
        return arrayResult;
    }

    private void cargarEntradasYSalidas(){

        ENTRADAS = new double[][] {
                {	0	,	0	,	0	,	0	}	,
                {	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0.5	,	0	}	,
                {	0	,	0	,	0.5	,	1	}	,
                {	0	,	0	,	1	,	0	}	,
                {	0	,	0	,	1	,	1	}	,
                {	0	,	0.5	,	0	,	0	}	,
                {	0	,	0.5	,	0	,	1	}	,
                {	0	,	0.5	,	0.5	,	0	}	,
                {	0	,	0.5	,	0.5	,	1	}	,
                {	0	,	0.5	,	1	,	0	}	,
                {	0	,	0.5	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	}	,
                {	0	,	1	,	0	,	1	}	,
                {	0	,	1	,	0.5	,	0	}	,
                {	0	,	1	,	0.5	,	1	}	,
                {	0	,	1	,	1	,	0	}	,
                {	0	,	1	,	1	,	1	}	,
                {	0.5	,	0	,	0	,	0	}	,
                {	0.5	,	0	,	0	,	1	}	,
                {	0.5	,	0	,	0.5	,	0	}	,
                {	0.5	,	0	,	0.5	,	1	}	,
                {	0.5	,	0	,	1	,	0	}	,
                {	0.5	,	0	,	1	,	1	}	,
                {	0.5	,	0.5	,	0	,	0	}	,
                {	0.5	,	0.5	,	0	,	1	}	,
                {	0.5	,	0.5	,	0.5	,	0	}	,
                {	0.5	,	0.5	,	0.5	,	1	}	,
                {	0.5	,	0.5	,	1	,	0	}	,
                {	0.5	,	0.5	,	1	,	1	}	,
                {	0.5	,	1	,	0	,	0	}	,
                {	0.5	,	1	,	0	,	1	}	,
                {	0.5	,	1	,	0.5	,	0	}	,
                {	0.5	,	1	,	0.5	,	1	}	,
                {	0.5	,	1	,	1	,	0	}	,
                {	0.5	,	1	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	}	,
                {	1	,	0	,	0	,	1	}	,
                {	1	,	0	,	0.5	,	0	}	,
                {	1	,	0	,	0.5	,	1	}	,
                {	1	,	0	,	1	,	0	}	,
                {	1	,	0	,	1	,	1	}	,
                {	1	,	0.5	,	0	,	0	}	,
                {	1	,	0.5	,	0	,	1	}	,
                {	1	,	0.5	,	0.5	,	0	}	,
                {	1	,	0.5	,	0.5	,	1	}	,
                {	1	,	0.5	,	1	,	0	}	,
                {	1	,	0.5	,	1	,	1	}	,
                {	1	,	1	,	0	,	0	}	,
                {	1	,	1	,	0	,	1	}	,
                {	1	,	1	,	0.5	,	0	}	,
                {	1	,	1	,	0.5	,	1	}	,
                {	1	,	1	,	1	,	0	}	,
                {	1	,	1	,	1	,	1	}
        };

        SALIDAS = new double[][] {
                {	1	,	0	,	0	,	0	,	0	,	0	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	0	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	0	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	0	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	1	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	1	,	0	,	0	,	1	}	,
                {	0	,	0	,	1	,	0	,	1	,	1	}	,
                {	0	,	0	,	1	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	1	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	1	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	1	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	0	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}	,
                {	0	,	1	,	0	,	0	,	0	,	1	}	,
                {	0	,	0	,	0	,	0	,	1	,	1	}
        };
    }
}
