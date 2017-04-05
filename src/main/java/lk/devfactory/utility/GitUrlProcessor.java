package lk.devfactory.utility;

import org.apache.commons.lang3.StringUtils;

//TODO Add javadocs
public class GitUrlProcessor {
	
	public static String convertToZipDownloadUrl(String gitUrl) {
		// Match to https://codeload.github.com/lahiruk/exam-conductor/zip/master
		gitUrl = StringUtils.replace(gitUrl, "github", "codeload.github"); 
		return gitUrl+"/zip/master";
	}
	
	public static String getProjectName(String gitUrl) {
		String[] components = gitUrl.split("/");
		
		return components[4]; /*https://github.com/lahiruk/dead-code-analyzer comes in 
								{"https:","","github.com","lahiruk","dead-code-analyzer"} */
	}

	
}
