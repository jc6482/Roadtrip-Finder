package template.main;

/* comment */
public class Main {
	public static void main(String[] args) {
		System.setProperty("environment", "dev");
		Application.run(args);
	}
}
