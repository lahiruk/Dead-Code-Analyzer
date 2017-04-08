package lk.devfactory;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

//TODO externalise the property values
public class SystemPropertyTestSupport {
	static {
		Path currentRelativePath = Paths.get("");
		String currentDirectory = currentRelativePath.toAbsolutePath().toString();
		System.setProperty("tmpPath",currentDirectory+File.separator+"build"+File.separator+"resources"+File.separator+"test");
		System.setProperty("distribution","MacOS");
	}
}
