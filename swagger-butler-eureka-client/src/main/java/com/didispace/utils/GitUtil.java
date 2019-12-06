package com.didispace.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import com.alibaba.fastjson.JSONObject;

public class GitUtil {
	private static final String git_username = "937557708@qq.com";
	private static final String git_password = "tjr07241711";
	private static String branch_name = "master";
	public static String localRepoPath = "D:\\Git_Project\\elk";
	// public static String localRepoPath+"/.git" = "D:/repo/.git";
	public static String remoteRepoURI = "https://github.com/a937557708/elk.git";
	public static String localCodeDir = "D:\\Git_Project\\elk";

	/**
	 * 新建一个分支并同步到远程仓库
	 * 
	 * @param branchName
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static String newBranch(String branchName) {
		String newBranchIndex = "refs/heads/" + branchName;
		String gitPathURI = "";
		Git git = null;
		try {

			// 检查新建的分支是否已经存在，如果存在则将已存在的分支强制删除并新建一个分支
			List<Ref> refs = git.branchList().call();
			for (Ref ref : refs) {
				if (ref.getName().equals(newBranchIndex)) {
					System.out.println("Removing branch before");
					git.branchDelete().setBranchNames(branchName).setForce(true).call();
					break;
				}
			}
			// 新建分支
			Ref ref = git.branchCreate().setName(branchName).call();
			// 推送到远程
			git.push().add(ref).call();
			gitPathURI = remoteRepoURI + " " + "feature/" + branchName;
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return gitPathURI;
	}

	/**
	 * 递归遍历文件目录,获取所有文件路径
	 * 
	 * @param filePath
	 * @return 2012-1-4
	 */
	public static List<String> getAllFilePathFromFolder(String filePath) {
		ArrayList<String> filePaths = new ArrayList<String>();
		File file = new File(filePath);
		try {
			File[] tempFile = file.listFiles();
			for (int i = 0; i < tempFile.length; i++) {
				String tempFileName = tempFile[i].getName();
				String path = tempFile[i].getAbsolutePath();
				if (tempFileName.endsWith(".git")) {
					continue;
				}
				if (tempFile[i].isFile()) {
					Long len = tempFile[i].length();
					if (len <= 1027 * 1027 * 100) {
						filePaths.add(path);
					}
				} else {
					List<String> tempFilePaths = getAllFilePathFromFolder(path);
					if (tempFilePaths.size() > 0) {
						for (String tempPath : tempFilePaths) {
							filePaths.add(tempPath);
						}
					}
				}
			}
		} catch (Exception e) {
			// fileNames.add("尚无文件到达！");
			// e.printStackTrace();
			// log4j.info("Can not find files!"+e.getMessage());
		}
		return filePaths;
	}

	public static void commitFiles() throws IOException, GitAPIException {
		String filePath = "";
		UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(git_username,
				git_password);
		Git git = Git.open(new File(localRepoPath + "/.git"));
		git.pull();
		List<String> list = new ArrayList<>();
		String[] strArr = { "kibana-7.1.1-linux-x86_64\\node_modules" };
		for (String str : strArr) {
			List<String> list1 = getAllFilePathFromFolder(localRepoPath + "\\" + str);
			list.addAll(list1);
		}

		System.out.println("commitFiles start " + localRepoPath);
		for (String path : list) {
			path = path.replace(localRepoPath + "\\", "").replace("\\", "/");

			// 创建用户文件的过程
			// File myfile = new File(filePath);
			// myfile.createNewFile();

			DirCache dirCache = git.add().addFilepattern(path).call();

			System.out.println("add start " + path);
			System.out.println("add " + path);
			// 提交
			git.commit().setMessage("Added pets:" + path).call();

			System.out.println("commit " + path);
			// 推送到远程
			try {
				git.push().setCredentialsProvider(provider).call();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("push " + path + "----" + new SimpleDateFormat("yyyy-MM-ddHH:mm:ss").format(new Date()));
		}
		System.out.println("commitFiles end: " + localRepoPath);
	}

	public static String cloneRepository(String branch) {
		try {
			System.out.println("开始下载......");
			// 设置远程服务器上的用户名和密码
			UsernamePasswordCredentialsProvider provider = new UsernamePasswordCredentialsProvider(git_username,
					git_password);
			CloneCommand cc = Git.cloneRepository().setURI(remoteRepoURI);
			cc.setBranch(branch);
			cc.setDirectory(new File(localRepoPath)).setCredentialsProvider(provider).call();
			// File file = new File(localRepoPath + "/.git");
			// Git git = Git.open(file);

			System.out.println("下载完成......");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	public static boolean pullBranchToLocal(String cloneURL) {
		boolean resultFlag = false;
		// String[] splitURL = cloneURL.split(" ");
		// String branchName = splitURL[1];
		String fileDir = localCodeDir;
		// 检查目标文件夹是否存在
		File file = new File(fileDir);
		// if (file.exists()) {
		// deleteFolder(file);
		// }

		Git git;
		try {
			git = Git.open(new File(localRepoPath + "/.git"));

			git.cloneRepository().setURI(cloneURL).setDirectory(file).call();
			resultFlag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultFlag;
	}

	/**
	 * 拉取远程代码
	 * 
	 * @param remoteBranchName
	 * @return 远程分支名
	 */

	public static boolean pull() {
		return pull(branch_name);
	}

	public static boolean pull(String remoteBranchName) {

		boolean pullFlag = true;
		try (Git git = Git.open(new File(localRepoPath + "/.git"));) {
			// UsernamePasswordCredentialsProvider provider =new
			// UsernamePasswordCredentialsProvider(GIT_USERNAME,GIT_PASSWORD);
			git.pull().setRemoteBranchName(remoteBranchName)
					// .setCredentialsProvider(provider)
					.call();
		} catch (Exception e) {
			e.printStackTrace();
			pullFlag = false;
		}
		return pullFlag;
	}

	/**
	 * 创建本地新仓库
	 * 
	 * @param repoPath
	 *            仓库地址 D:/workspace/TestGitRepository
	 * @return
	 * @throws IOException
	 */
	public static Repository createNewRepository(String repoPath) throws IOException {
		File localPath = new File(repoPath);
		// create the directory
		Repository repository = FileRepositoryBuilder.create(new File(localPath, ".git"));
		repository.create();
		return repository;
	}

	public static void deleteFolder(File file) {
		if (file.isFile() || file.list().length == 0) {
			file.delete();
		} else {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				deleteFolder(files[i]);
				files[i].delete();
			}
		}
	}

	public static void init(JSONObject json) {
		if (json != null) {
			if (json.containsKey("remoteRepoURI")) {
				remoteRepoURI = json.getString("remoteRepoURI");
			}
			if (json.containsKey("localRepoPath")) {
				localRepoPath = json.getString("localRepoPath");
			}
			if (json.containsKey("localCodeDir")) {
				localCodeDir = json.getString("localCodeDir");
			}
			if (json.containsKey("branch_name")) {
				branch_name = json.getString("branch_name");
			}

		}
		File file = new File(localRepoPath + "/.git");
		if (file.exists()) {
			pull();
			// cloneRepository(remoteRepoURI, localRepoPath);
			// pullBranchToLocal(remoteRepoURI);
		} else {
			File file1 = new File(localRepoPath);
			if (file1.exists()) {
				System.out.println(
						"Destination path “" + localRepoPath + "” already exists and is not an empty directory");
			} else {
				cloneRepository(branch_name);
			}
		}

	}

	static void setupRepo() throws GitAPIException {
		// 建立与远程仓库的联系，仅需要执行一次
		Git git = Git.cloneRepository().setURI(remoteRepoURI).setDirectory(new File(localRepoPath)).call();
	}

	public static void main(String[] args) {
		// GitUtil.init(null);

		try {
			GitUtil.commitFiles();
		} catch (IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("aaa");
	}
}
