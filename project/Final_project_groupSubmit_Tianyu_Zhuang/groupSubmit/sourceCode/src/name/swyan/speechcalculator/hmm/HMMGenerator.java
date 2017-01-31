package name.swyan.speechcalculator.hmm;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import name.swyan.speechcalculator.mfcc.Extract;
import name.swyan.speechcalculator.data.FormatControlConf;
import name.swyan.speechcalculator.data.PreProcess;
import name.swyan.speechcalculator.data.WaveData;
import name.swyan.speechcalculator.mfcc.FeatureVector;
import name.swyan.speechcalculator.hmm.HiddenMarkov;
import name.swyan.speechcalculator.vq.Codebook;
import name.swyan.speechcalculator.vq.Points;
import name.swyan.speechcalculator.data.DataBase;
import name.swyan.speechcalculator.data.ObjectIODataBase;
import name.swyan.speechcalculator.data.FilesManager;
import name.swyan.speechcalculator.util.ArrayWriter;

public class HMMGenerator {

	FilesManager filesList;
	FormatControlConf fc = new FormatControlConf();
	int samplingRate = (int) fc.getRate();
	int samplePerFrame = 512;
	int FEATUREDIMENSION = 39;
	String[] words;
	String[] users;
	File[][] wavFiles;
	Extract fExt;
	WaveData wd;
	PreProcess prp;
	Codebook cb;
	List<double[]> allFeaturesList = new ArrayList<double[]>();
	HiddenMarkov mkv;
	DataBase db;
	private HiddenMarkov hmmModels[];

	public HMMGenerator() {
		wd = new WaveData();
	}

	public void generateCodebook() {
		filesList = new FilesManager("train");
		int totalFrames = 0;
		wavFiles = filesList.readWaveFilesList();
		/*for (int i = 0; i < wavFiles.length; i++) {
			for (int j = 0; j < wavFiles[i].length; j++) {
				System.out.println("Currently :::" + wavFiles[i][j].getAbsoluteFile());
				FeatureVector feature = extractFeatureFromFile(wavFiles[i][j]);
				for (int k = 0; k < feature.getNoOfFrames(); k++) {
					allFeaturesList.add(feature.getFeatureVector()[k]);
					totalFrames++;//
				}
			}
		}*/
		for (int i = 0; i < wavFiles.length; i++) {
			for (int j = 1; j < wavFiles[i].length; j++) {
				System.out.println("Currently :::" + wavFiles[i][j].getAbsoluteFile());
				FeatureVector feature = extractFeatureFromFile(wavFiles[i][j]);
				//System.out.println("");
				for (int k = 0; k < feature.getNoOfFrames(); k++) {
					allFeaturesList.add(feature.getFeatureVector()[k]);
					totalFrames++;//
				}
			}
		}
		System.out.println("total frames  " + totalFrames + "  allFeaturesList.size   " + allFeaturesList.size());
		// make a single 2d array of all features
		double allFeatures[][] = new double[totalFrames][FEATUREDIMENSION];
		for (int i = 0; i < totalFrames; i++) {
			double[] tmp = allFeaturesList.get(i);
			allFeatures[i] = tmp;
		}
		Points pts[] = new Points[totalFrames];
		for (int j = 0; j < totalFrames; j++) {
			pts[j] = new Points(allFeatures[j]);
		}
		System.out.println("Generating Codebook........");
		System.out.println(pts);
		Codebook cbk = new Codebook(pts);
		System.out.println(pts);
		cbk.saveToFile();
		System.out.println("Codebook Generation Completed");
		// hmmTrain();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void trainHMM() {
		//System.out.println("inside hmm train");
		filesList = new FilesManager("train");
		cb = new Codebook();
		// for each training word
		int quantized[][];
		// extract features
		wavFiles = filesList.readWaveFilesList();
		words = filesList.readWordWavFolder();
		/*for (int i = 0; i < wavFiles.length; i++) {
			// for each training samples
			quantized = new int[wavFiles[i].length][];// training sequence
			String currentWord = words[i];
			System.out.println("Current Word :::" + currentWord);
			for (int j = 0; j < wavFiles[i].length; j++) {
				System.out.println("Currently :::" + wavFiles[i][j].getAbsoluteFile());
				FeatureVector feature = extractFeatureFromFile(wavFiles[i][j]);
				// get Points object from feature vector
				Points[] pts = getPointsFromFeatureVector(feature);
				quantized[j] = cb.quantize(pts);
				// ArrayWriter.printIntArrayToConole(quantized[j]);
			}
			mkv = new HiddenMarkov(6, 256);
			// TODO: value, MAKE CONSTANTS

			// do training
			System.out.println("Training.......");
			mkv.setTrainSeq(quantized);
			mkv.train();
			mkv.save(currentWord);
			System.out.println("Word  " + currentWord + " is trained");
		}*/
		for (int i = 0; i < wavFiles.length; i++) {
			// for each training samples
			quantized = new int[wavFiles[i].length-1][];// training sequence
			//String currentWord = words[i];
			String currentWord = words[i+1];
			System.out.println("Current Word :::" + currentWord);
			for (int j = 1; j < wavFiles[i].length; j++) {
				System.out.println("Currently :::" + wavFiles[i][j].getAbsoluteFile());
				FeatureVector feature = extractFeatureFromFile(wavFiles[i][j]);
				// get Points object from feature vector
				Points[] pts = getPointsFromFeatureVector(feature);
				quantized[j-1] = cb.quantize(pts);
				// ArrayWriter.printIntArrayToConole(quantized[j]);
			}
			mkv = new HiddenMarkov(6, 256);
			// TODO: value, MAKE CONSTANTS

			// do training
			System.out.println("Training.......");
			mkv.setTrainSeq(quantized);
			mkv.train();
			mkv.save(currentWord);
			System.out.println("Word  " + currentWord + " is trained");
		}
		System.out.println("HMM Train Completed");
	}

	public String hmmGetWordFromFile(File speechFile) {
		// extract features
		FeatureVector feature = extractFeatureFromFile(speechFile);
		return hmmGetWordWithFeature(feature);
	}

	public String hmmGetWordFromFileByteArray(byte[] byteArray) {
		// extract features
		FeatureVector feature = extractFeatureFromFileByteArray(byteArray);
		return hmmGetWordWithFeature(feature);
	}

	public String hmmGetWordFromAmplitureArray(float[] byteArray) {
		// extract features
		FeatureVector feature = extractFeatureFromExtractedAmplitureByteArray(byteArray);
		return hmmGetWordWithFeature(feature);
	}

	public String hmmGetWordWithFeature(FeatureVector feature) {
		Points[] pts = getPointsFromFeatureVector(feature);
		cb = new Codebook();
		// quantize using Codebook
		int quantized[] = cb.quantize(pts);

		// read registered/trained words
		db = new ObjectIODataBase();
		db.setType("hmm");
		words = db.readRegistered();
		db = null;
		System.out.println("registred words ::: count : " + words.length);
		ArrayWriter.printStringArrayToConole(words);
		hmmModels = new HiddenMarkov[words.length];

		// read hmmModels
		for (int i = 0; i < words.length; i++) {
			hmmModels[i] = new HiddenMarkov(words[i]);
		}
		// find the likelihood by viterbi decoding of quantized sequence
		double likelihoods[] = new double[words.length];
		for (int j = 0; j < words.length; j++) {
			likelihoods[j] = hmmModels[j].viterbi(quantized);
			System.out.println("Likelihood with " + words[j] + " is " + likelihoods[j]);
		}
		// find the largest likelihood
		double highest = Double.NEGATIVE_INFINITY;
		int wordIndex = -1;
		for (int j = 0; j < words.length; j++) {
			if (likelihoods[j] > highest) {
				highest = likelihoods[j];
				wordIndex = j;
			}
		}
		System.out.println("Best matched word " + words[wordIndex]);
		return words[wordIndex];
	}

	/**
	 * 
	 * @param byteArray
	 * @return
	 * @throws Exception
	 */
	public FeatureVector extractFeatureFromFileByteArray(byte[] byteArray) {
		float[] arrAmp;
		arrAmp = wd.extractAmplitudeFromFileByteArray(byteArray);
		return extractFeatureFromExtractedAmplitureByteArray(arrAmp);
	}

	/**
	 * 
	 * @param byteArray
	 * @return
	 * @throws Exception
	 */
	public FeatureVector extractFeatureFromExtractedAmplitureByteArray(float[] arrAmp) {
		prp = new PreProcess(arrAmp, samplePerFrame, samplingRate);
		fExt = new Extract(prp.framedSignal, samplingRate, samplePerFrame);
		fExt.makeMfccFeatureVector();
		return fExt.getFeatureVector();
	}

	/**
	 * 
	 * @param speechFile
	 * @return
	 * @throws Exception
	 */
	private FeatureVector extractFeatureFromFile(File speechFile) {
		float[] arrAmp;
		arrAmp = wd.extractAmplitudeFromFile(speechFile);
		return extractFeatureFromExtractedAmplitureByteArray(arrAmp);
	}

	/**
	 * 
	 * @param features
	 * @return
	 */
	private Points[] getPointsFromFeatureVector(FeatureVector features) {
		// get Points object from all feature vector
		Points pts[] = new Points[features.getFeatureVector().length];
		for (int j = 0; j < features.getFeatureVector().length; j++) {
			pts[j] = new Points(features.getFeatureVector()[j]);
		}
		return pts;
	}

	/**
	 * 
	 * @param word
	 * @return
	 */
	public boolean checkWord(String word) {
		db = new ObjectIODataBase();
		db.setType("hmm");
		words = db.readRegistered();
		for (int i = 0; i < words.length; i++) {
			if (words[i].equalsIgnoreCase(word)) { return true;// word found
			}
		}
		return false;// word not found
	}

	/**
	 * 
	 * @return
	 */
	public boolean checkSelectedPath() {
		return true;
	}
}
