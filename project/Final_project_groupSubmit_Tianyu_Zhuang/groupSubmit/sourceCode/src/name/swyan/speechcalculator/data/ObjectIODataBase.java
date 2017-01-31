package name.swyan.speechcalculator.data;

import java.io.File;

import name.swyan.speechcalculator.vq.CodeBookDictionary;
import name.swyan.speechcalculator.hmm.HMMModel;

public class ObjectIODataBase implements DataBase {

    /**
     * type of current model,,gmm,hmm,cbk, which is extension ofsaved file
     */
    String type;
    /**
     *
     */
    String[] modelFiles;
    /**
     *
     */
    String[] userNames;
    String CURRENTFOLDER;
    /**
     * the file name to same codebook, adds .cbk extension automatically
     */
    String CODEBOOKFILENAME = "codebook";
    String currentModelType;

    public ObjectIODataBase() {
    }

    /**
     *
     * @param type type of the model, valid entry are either gmm, hmm, or cbk
     */
    public void setType(String type) {
        this.type = type;
        if (this.type.equalsIgnoreCase("hmm")) {
            //CURRENTFOLDER = "models\\HMM";
            CURRENTFOLDER = "models/HMM";
        }
        if (this.type.equalsIgnoreCase("cbk")) {
            //CURRENTFOLDER = "models\\codeBook";
            CURRENTFOLDER = "models/codeBook";
        }
    }

    /**
     *
     */
    @Override
    public Model readModel(String name) {
        Model model = null;
        if (type.equalsIgnoreCase("hmm")) {
            ObjectIO<HMMModel> oio = new ObjectIO<HMMModel>();
            model = new HMMModel();
            //model = oio.readModel( CURRENTFOLDER + "\\" + name + "." + type);
            model = oio.readModel(CURRENTFOLDER + "/" + name + "." + type);
//            System.out.println("Type " + type);
//            System.out.println("Read ::::: " + DBROOTFOLDER + "\\" + CURRENTFOLDER + "\\" + name + "." + type);
            // System.out.println(model);
        }
        if (type.equalsIgnoreCase("cbk")) {
            ObjectIO<CodeBookDictionary> oio = new ObjectIO<CodeBookDictionary>();
            model = new CodeBookDictionary();
            //model = oio.readModel( CURRENTFOLDER + "\\" + CODEBOOKFILENAME + "." + type);
            model = oio.readModel(CURRENTFOLDER + "/" + CODEBOOKFILENAME + "." + type);
//            System.out.println("Read ::::: " + DBROOTFOLDER + "\\" + CURRENTFOLDER + "\\" + CODEBOOKFILENAME + "." + type);
        }
        return model;
    }

    /**
     *
     */
    @Override
    public String[] readRegistered() {

        modelFiles = readRegisteredWithExtension();
        System.out.println("modelFiles length (Oiodb) :" + modelFiles.length);
        return removeExtension(modelFiles);
    }

    /**
     *
     */
    @Override
    public void saveModel(Model model, String name) {

        if (type.equalsIgnoreCase("hmm")) {
            ObjectIO<HMMModel> oio = new ObjectIO<HMMModel>();
            oio.setModel((HMMModel) model);
            /*oio.saveModel(CURRENTFOLDER + "\\" + name
                    + "." + type);*/
            oio.saveModel(CURRENTFOLDER + "/" + name
                    + "." + type);
        }
        if (type.equalsIgnoreCase("cbk")) {
            ObjectIO<CodeBookDictionary> oio = new ObjectIO<CodeBookDictionary>();
            oio.setModel((CodeBookDictionary) model);
            /*oio.saveModel( CURRENTFOLDER + "\\"
                    + CODEBOOKFILENAME + "." + type);*/
            oio.saveModel( CURRENTFOLDER + "/"
                    + CODEBOOKFILENAME + "." + type);
        }

    }

    private String[] readRegisteredWithExtension() {
        File modelPath = new File( CURRENTFOLDER);
        //modelFiles = new String[modelPath.list().length];
        modelFiles = new String[modelPath.list().length-1];
        String[] mFwD = modelPath.list();
        for(int i = 0; i < modelPath.list().length-1; i++) {
            modelFiles[i] = mFwD[i+1];
        }
        //modelFiles = modelPath.list();// must return only folders
        return modelFiles;
    }

    private String[] removeExtension(String[] modelFiles) {
        // remove the ext i.e., type
        String[] noExtension = new String[modelFiles.length];
        for (int i = 0; i < modelFiles.length; i++) {
            noExtension[i] = modelFiles[i].substring(0,
                    modelFiles[i].length() - 4);// TODO:check the lengths
        }
        return noExtension;
    }
}
