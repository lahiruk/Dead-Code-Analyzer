package lk.devfactory.utility;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.repository.impl.ExtractProject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ExtractProjectTest extends SystemPropertyTestSupport{
	static final String TMP_PATH = "/Users/lahiru/tmp";
	private static final String FOLDER_TO_ZIP = TMP_PATH+ File.separator +"exam-conductor-master";
	static final String repoId = "123";
	private static final String ZIP_FILE_NAME = TMP_PATH+ File.separator +repoId+SystemConst.DOWNLOAD_PROJECT_SUFFIX;

	File oldDir;
	File newDir;
	File zipFile;

	@Autowired()
    ExtractProject ep;

	@Before
	public void before(){
		oldDir = new File(FOLDER_TO_ZIP);
		try {
			oldDir.mkdir();
			pack(FOLDER_TO_ZIP, ZIP_FILE_NAME);
			zipFile = new File(ZIP_FILE_NAME);
			oldDir.delete();
		} catch (IOException e) {
			fail("Unable to create temp zip file for unzip folder "+e.getMessage());
		}
	}
	
	@After
	public void after() {
		newDir.delete(); //Clean up
		zipFile.delete();
	}
	
	@Test
	public void extract(){
		assertFalse("Directory with extracting directory should not exist", oldDir.exists());
		ep.extract(repoId);
		newDir = new File(FOLDER_TO_ZIP);
		assertTrue("Folder extract not successful", newDir.exists());
	}
	
	public void pack(String sourceDirPath, String zipFilePath) throws IOException {
        ZipOutputStream zip = null;
        FileOutputStream fW = null;
        fW = new FileOutputStream(zipFilePath);
        zip = new ZipOutputStream(fW);
        addFolderToZip("", sourceDirPath, zip);
        zip.close();
        fW.close();
	}
	
    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.list().length == 0) {
            addFileToZip(path , srcFolder, zip, true);
        }
        else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                } 
                else {
                     addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }
    
    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        File folder = new File(srcFile);
        if (flag) {
            zip.putNextEntry(new ZipEntry(path + "/" +folder.getName() + "/"));
        }
        else {
            if (folder.isDirectory()) {
                addFolderToZip(path, srcFile, zip);
            }
            else {
                byte[] buf = new byte[1024];
                int len;
                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                while ((len = in.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
                in.close();
            }
        }
    }
}
