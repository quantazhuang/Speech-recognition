package name.swyan.speechcalculator.data;

import java.io.File;


/**
 * various operations relating to reading train/testing wav folders<br>
 * works according to the filePath supplied in constructor arguement
 * 
 */
public class FilesManager {

	protected String[] folderNames;
	protected File[][] waveFiles;
	protected File wavPath;

	/**
	 * constructor, sets the wavFile path according to the args supplied
	 * 
	 * @param hmmOrGmm
	 * @param testOrTrain
	 */
	public FilesManager(String s) {
		if (s.equalsIgnoreCase("train")) {
			setWavPath(new File("samples"));
		}
	}

	private void readFolder() {
//		System.out.println(getWavPath().getAbsolutePath());
		folderNames = new String[getWavPath().list().length];
		folderNames = getWavPath().list();// must return only folders
	}

	public String[] readWordWavFolder() {
		readFolder();
		return folderNames;
	}

	public File[][] readWaveFilesList() {
		readFolder();
		/*waveFiles = new File[folderNames.length][];
		for (int i = 0; i < folderNames.length; i++) {
			System.out.println(folderNames[i]);
			File wordDir = new File(getWavPath() + "\\" + folderNames[i]+"\\");
			waveFiles[i] = wordDir.listFiles();
		}*/
		waveFiles = new File[folderNames.length-1][];
		for(int i = 0; i < folderNames.length-1; i++) {
			System.out.println(folderNames[i+1]);
			File wordDir = new File(getWavPath() + "/" + folderNames[i+1] + "/");
			//File fileDir = wordDir.listFiles();
			waveFiles[i] = wordDir.listFiles();
		}
		System.out.println("++++++Folder's Content+++++");
		for (int i = 0; i < waveFiles.length; i++) {
			for (int j = 0; j < waveFiles[i].length; j++) {
				System.out.print(waveFiles[i][j].getName() + "\t\t");
			}
			System.out.println();
		}
		return waveFiles;

	}

	public File getWavPath() {
		return wavPath;
	}

	public void setWavPath(File wavPath) {
		this.wavPath = wavPath;
		System.out.println("Current wav file Path   :" + this.wavPath.getName());
	}
}
