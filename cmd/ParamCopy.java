package cmd;
//ParamPasta by ViveTheJoestar
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class ParamCopy {
	public static String copyDatFilesToCostumeDirs(List<File> list) throws IOException {
		String copiedFiles = null;
		for (File dat: list) {
			Path parentPath = dat.toPath().getParent(); 
			String datName = dat.getName();
			File rootDir = parentPath.getParent().toFile();
			File parentDir = parentPath.toFile();
			File[] dirs = rootDir.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					String nameLower = name.toLowerCase();
					if (dir.isDirectory()) {
						String[] nameSplit = nameLower.split("_");
						if (nameSplit.length >= 2) {
							int last = nameSplit.length - 1;
							if (nameSplit[last].matches("\\dp")) return true;
							else if (nameSplit[last].equals("dmg") && nameSplit[last - 1].matches("\\dp")) return true;
						}
					}
					return false;
				}
			});
			String[] parentNameSplit = parentDir.getName().toLowerCase().split("_");
			String parentCharaName = "";
			int end = parentNameSplit.length - 1;
			if (parentNameSplit[end].equals("dmg")) end--;
			if (parentNameSplit.length > 0) {
				for (int strCnt = 0; strCnt < end; strCnt++)
					parentCharaName += parentNameSplit[strCnt];
			}
			for (File dir: dirs) {
				System.out.println(dir + " && " + parentDir);
				String[] dirNameSplit = dir.getName().toLowerCase().split("_");
				String dirCharaName = "";
				end = dirNameSplit.length - 1;
				if (dirNameSplit[end].equals("dmg")) end--;
				for (int strCnt = 0; strCnt < end; strCnt++)
					dirCharaName += dirNameSplit[strCnt];
				boolean charaNamesMatch = dirCharaName.equals(parentCharaName) && !dir.equals(parentDir);
				if (parentDir.isDirectory() && dir.isDirectory() && charaNamesMatch) {
					Files.copy(dat.toPath(), dir.toPath().resolve(datName), StandardCopyOption.REPLACE_EXISTING);
					copiedFiles += "Copied " + datName + " to " + dir.getAbsolutePath() + "\n";
				}
			}
		}
		return copiedFiles;
	}
}