package name.swyan.speechcalculator.data;

public interface DataBase {
	public void setType(String type);

	public String[] readRegistered();

	public Model readModel(String name);

	public void saveModel(Model m, String name);
}
