package lk.devfactory.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;

import com.scitools.understand.Database;
import com.scitools.understand.Entity;
import com.scitools.understand.Reference;
import com.scitools.understand.Understand;
import com.scitools.understand.UnderstandException;

public class SubmitExternalProcessTest {
	public static String projPath = "/Users/lahiru/tmp";
	public static String udbProjectName = "/und_project.udb";

	public static void main(String[] args) {
		
		System.out.println("Prepare the udb file.");
		buildUdb("exam-conductor-master");
		System.out.println("###############");
		lineAndColomNumbersMethodsVariables("exam-conductor-master");
		System.out.println("***************");
		unusedPrivateFunctions("exam-conductor-master");
		System.out.println("@@@@@@@@@@@@@@@");
		unusedPrivateVariables("exam-conductor-master");
		System.out.println("&&&&&&&&&&&&&&&");
		unusedParameters("exam-conductor-master");
	}

	private static void buildUdb(String repoName) {
		String absPath = projPath + File.separator + repoName;
		String cmd = "und -db " + absPath + udbProjectName + " create -languages Java " + "add " + absPath
				+ "/src settings -javaVersion Java8 analyze";
		System.out.println(cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			System.out.println("Waiting for udb build ...");
			p.waitFor();
			System.out.println("Udb file done.");
		} catch (IOException | InterruptedException e) {
			System.out.println("Failed execute udb command:" + e.getMessage());
			System.exit(0);
		}
	}

	private static void lineAndColomNumbersMethodsVariables(String repoName) {
		try {
			// Open the Understand Database
			Database db = Understand.open(projPath + File.separator + repoName + udbProjectName);
			System.out.println("Project file :" + projPath + File.separator + repoName + udbProjectName);
			// Get a list of all functions and methods
			Entity[] clazz = db.ents("class ~unknown ~unresolved");
			for (Entity e : clazz) {
				System.out.println(e.simplename() + " : " + e.kind());

				// Get a list of all variables of the class
				Reference[] varRefs = e.refs("define", "variable", true);
				for (Reference vRef : varRefs) {
					System.out.println(e.name() + " => " + vRef.ent().name() + " [" + vRef.line() + ":" + vRef.column()
							+ "] > " + vRef.ent().kind().name());
				}

				// Get a list of all functions and methods of class
				Reference[] methodRefs = e.refs("define", "method", true);
				for (Reference mRef : methodRefs) {
					System.out.println(e.name() + " => " + mRef.ent().name() + " [" + mRef.line() + ":" + mRef.column()
							+ "] > " + mRef.ent().kind().name());

					// Get a list of all method variables
					Reference[] localVarRefs = mRef.ent().refs("define", "Variable Parameter", true);
					for (Reference lVRef : localVarRefs) {
						System.out.println(mRef.ent().name() + " =>* " + lVRef.ent().name() + " [" + lVRef.line() + ":"
								+ lVRef.column() + "] > " + lVRef.ent().kind().name());
					}
				}
			}
		} catch (UnderstandException e) {
			System.out.println("Failed opening Database:" + e.getMessage());
			System.exit(0);
		}
	}

	private static void unusedPrivateFunctions(String repoName) {
		try {
			// Open the Understand Database
			Database db = Understand.open(projPath + File.separator + repoName + udbProjectName);
			// Get a list of all functions and methods
			Entity[] funcs = db.ents("function private ~unknown ~unresolved,method private ~unknown ~unresolved");
			for (Entity e : funcs) {
				System.out.println(e.name() + " : " + e.kind());

				// Get a list of all functions and methods has invocations
				Reference[] callRefs = e.refs("callby", null, true);
				for (Reference pRef : callRefs) {
					System.out.println(
							e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
				}
			}
		} catch (UnderstandException e) {
			System.out.println("Failed opening Database:" + e.getMessage());
			System.exit(0);
		}
	}

	private static void unusedPrivateVariables(String repoName) {
		try {
			// Open the Understand Database
			Database db = Understand.open(projPath + File.separator + repoName + udbProjectName);
			// Get a list of all variables
			Entity[] vars = db.ents("variable ~unknown");

			for (Entity e : vars) {
				System.out.println(e.name() + " : " + e.kind());

				// Get a list of all used variables
				Reference[] useByRefs = e.refs("Useby", null, true);
				for (Reference pRef : useByRefs) {
					System.out.println(
							e.name() + " => " + pRef.ent().name() + " [" + pRef.line() + ":" + pRef.column() + "]");
				}
			}
		} catch (UnderstandException e) {
			System.out.println("Failed opening Database:" + e.getMessage());
			System.exit(0);
		}
	}

	private static void unusedParameters(String repoName) {
		try {
			// Open the Understand Database
			Database db = Understand.open(projPath + File.separator + repoName + udbProjectName);
			// Get a list of all functions and methods
			Entity[] funcs = db.ents("function ~unknown ~unresolved,method ~unknown ~unresolved");

			for (Entity e : funcs) {
				System.out.println(e.name() + " : " + e.kind());

				// Get a list of all params of functions and methods
				Reference[] paramterRefs = e.refs("define", "parameter", true);
				for (Reference pRef : paramterRefs) {
					e = pRef.ent();
					System.out.println("Param => " + e.name());

					// Get a list of all used params
					Reference[] useByRefs = e.refs("Useby", null, true);
					for (Reference uRef : useByRefs) {
						System.out.println(e.name() + " =>* " + uRef.ent().name() + " [" + uRef.line() + ":"
								+ uRef.column() + "]");
					}
				}
			}
		} catch (UnderstandException e) {
			System.out.println("Failed opening Database:" + e.getMessage());
			System.exit(0);
		}
	}
}