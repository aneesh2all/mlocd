package classifiers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import utils.FileWorker;
import weka.classifiers.bayes.*;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.C45Loader;

public class NaiveWeka extends classifiers.Classifier{
weka.classifiers.Classifier learner = null;
	
	public void setLoction(String location){
		this.model = location;
	}
	
	public void build(String filename) throws Exception {

		if(this.model == null)
			this.model = filename.split("\\.")[0];

		File file = new File(filename);
		C45Loader loader = new C45Loader();
		loader.setSource(file);
		Instances dataset = loader.getDataSet();
//		 Instances dataset = arf.getStructure();
//		int num = dataset.numInstances();
//		for (int i = 0; i < num - 1; i++)
//			System.out.println(dataset.instance(i));

		int ci = dataset.numAttributes();
		dataset.setClassIndex(ci - 1);
//		 System.out.println(dataset.classAttribute());
		this.learner = new NaiveBayes();
		this.learner.buildClassifier(dataset);
	}


	public String classifyInstance(String instance) throws Exception{
		File file = null;
		String label = null;
		String tmpdata = null;
		int i = 1;
		do {
			tmpdata = this.model + "naiveTmp" + Integer.toString(i++);
			file = new File(tmpdata + ".data");
		} while (file.exists());
		PrintWriter out = new PrintWriter(new FileOutputStream(tmpdata
				+ ".data"));
		out.println(instance);
		out.close();
		FileWorker.cp(this.model + ".names", tmpdata + ".names");
	    file = new File(tmpdata);
		C45Loader loader = new C45Loader();
		loader.setSource(file);
        Instances dataset = loader.getDataSet();
		double d = learner.classifyInstance(dataset.instance(0));
		int index = (int) d;
		Attribute class_attribute = dataset.classAttribute();
		label = class_attribute.value(index);
		FileWorker.remove(tmpdata+".data");
		FileWorker.remove(tmpdata + ".names");
		return label;
	}

	public String[] classifyData(String filename) throws Exception{
		 File file = new File(filename);
		C45Loader loader = new C45Loader();
		loader.setSource(file);
	    Instances dataset = loader.getDataSet();
        int num = dataset.numInstances();
        Attribute class_attribute = dataset.classAttribute();
        String[] labels = new String[num];
	    for(int i = 0; i < num; ++i){
        	 double d   = learner.classifyInstance(dataset.instance(i));
        	 int index = (int) d;
        	 String label = class_attribute.value(index);
        	 labels[i] = label;
	    }
		return labels;	
	}
	
	public NaiveWeka copy(){
		NaiveWeka copy = new NaiveWeka();
		copy.learner = this.learner;
		copy.model = this.model;
		return copy;
	}
}
