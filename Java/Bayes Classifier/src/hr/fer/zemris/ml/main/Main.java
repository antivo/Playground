package hr.fer.zemris.ml.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.ObjectInputStream.GetField;
import java.util.List;

import hr.fer.zemris.ml.data.Data;
import hr.fer.zemris.ml.data.builder.DataBuilder;
import hr.fer.zemris.ml.model.DiagonalCovarianceMatrixModel;
import hr.fer.zemris.ml.model.GeneralModel;
import hr.fer.zemris.ml.model.IsotropicCovarianceMatrixModel;
import hr.fer.zemris.ml.model.SharedCovarianceMatrixModel;
import Jama.Matrix;

public class Main {
	private static String opcenitiDat = "../output/opceniti.dat"; 
	private static String dijeljenaDat = "../output/dijeljena.dat";
	private static String dijagonalnaDat = "../output/dijagonalna.dat";
	private static String izotropnaDat = "../output/izotropna.dat";
	
	private static String greske = "../output/greske.dat";
	
	public static void main(String[] args) throws IOException {
		Data train = DataBuilder.fromFile(args[0].split("=")[1]);
		Data test = DataBuilder.fromFile(args[1].split("=")[1]);
		
		GeneralModel gm = new GeneralModel(train, test);
		SharedCovarianceMatrixModel sm = new SharedCovarianceMatrixModel(gm);
		DiagonalCovarianceMatrixModel dm = new DiagonalCovarianceMatrixModel(sm);
		IsotropicCovarianceMatrixModel im = new IsotropicCovarianceMatrixModel(dm);
	
		
		PrintWriter out = new PrintWriter(opcenitiDat);
		out.println(gm.getAposteriori());
		out.flush();
		out.close();
		
		out = new PrintWriter(dijeljenaDat);
		out.println(sm.getAposteriori());
		out.flush();
		out.close();
		
		out = new PrintWriter(dijagonalnaDat);
		out.println(dm.getAposteriori());
		out.flush();
		out.close();
		
		out = new PrintWriter(izotropnaDat);
		out.println(im.getAposteriori());
		out.flush();
		out.close();
		
		StringBuilder sb = new StringBuilder("");
		sb.append("opceniti ").append("\t ").append(String.format("%.2f", gm.getLearningError())).append(" \t ").append(String.format("%.2f", gm.getGeneralizationError())).append("\n");
		sb.append("dijeljena ").append("\t ").append(String.format("%.2f", sm.getLearningError())).append(" \t ").append(String.format("%.2f", sm.getGeneralizationError())).append("\n");
		sb.append("dijagonalna ").append("\t ").append(String.format("%.2f", dm.getLearningError())).append(" \t ").append(String.format("%.2f", dm.getGeneralizationError())).append("\n");
		sb.append("izotropna ").append("\t ").append(String.format("%.2f", im.getLearningError())).append(" \t ").append(String.format("%.2f", im.getGeneralizationError()));
		
		out = new PrintWriter(greske);
		out.println(sb.toString().replace(',', '.'));
		out.flush();
		out.close();
	}

}
