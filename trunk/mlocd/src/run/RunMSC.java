package run;
import meta.MSC;
import classifiers.NaiveWeka;
import utils.Evaluation;

import data.generator.Sea;

public class RunMSC {
	
	public static void main(String[] args)throws Exception{
		String base = "lab\\";
		String stadata = "sea";
		Sea sea = new Sea(base+stadata);
		String[] datasets = new String[2];
		
		for(int i = 0; i < datasets.length; ++i){
			String dataset = base + stadata + (i+1) +".data";
			sea.setLocation(dataset);
			sea.generateData(100);
			sea.makeNamesFile();
			datasets[i] = dataset;
		}
		
		String testset = datasets[datasets.length-1];
	
		NaiveWeka learner = new NaiveWeka();
//		learner.build(datasets[1]);
		MSC classifier = new MSC(learner);
		classifier.setLocation(base);
		classifier.build(datasets);
		
		String[] labels = classifier.classifyData(testset);
		double accuracy = Evaluation.accuracy(testset, labels);
		System.out.println("The accuracy of msc is "+accuracy);
		System.out.println("System End");
	}
	
}