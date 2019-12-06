package com.didispace.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.errors.CorruptObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.MutableObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.patch.HunkHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.gitective.core.BlobUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: jgit工具类
 * @Author: xiaobing
 * @system name: 工作流引擎
 * @copyright：长安新生（深圳）金融投资有限公司
 * @Date: Created in 2018/9/26 10:35
 */
public class GitUtils {

	private static final Logger logger = LoggerFactory.getLogger(GitUtil.class);

	/**
	 * @Author: xiaobing
	 * @Description: 克隆项目
	 * @param username
	 *            git用户名
	 * @param password
	 *            git密码
	 * @param remotePath
	 *            git远程库路径
	 * @param branch
	 *            git分支
	 * @param localPath
	 *            下载已有仓库到本地路径
	 * @Date: 2018/9/25
	 */
	public static void gitClone(String username, String password, String remotePath, String branch, String localPath)
			throws IOException, GitAPIException {
		// 设置远程服务器上的用户名和密码
		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
				username, password);

		// 克隆代码库命令
		CloneCommand cloneCommand = Git.cloneRepository();
		// 设置远程URI
		Git git = cloneCommand.setURI(remotePath)
				// 设置clone下来的分支
				.setBranch(branch)
				// 设置下载存放路径
				.setDirectory(new File(localPath))
				// 设置权限验证
				.setCredentialsProvider(usernamePasswordCredentialsProvider).call();
		System.out.println("下载完成！");
		System.out.print(git.tag());
	}

	/**
	 * @Author: xiaobing
	 * @Description: fetch功能
	 * @Date: 2018/10/14
	 */
	public static String fetchBranch(String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			git.fetch().call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @Author: xiaobing
	 * @Description: 获取本地所有分支名
	 * @Date: 2018/10/14
	 */
	public static List<String> getLocalBranchNames(String localPath) throws IOException {
		List<String> result = new LinkedList<String>();
		Git git = Git.open(new File(localPath));
		Map<String, Ref> map = git.getRepository().getAllRefs();
		Set<String> keys = map.keySet();
		for (String str : keys) {
			if (str.indexOf("refs/heads") > -1) {
				String el = str.substring(str.lastIndexOf("/") + 1, str.length());
				result.add(el);
			}
		}
		return result;
	}

	/**
	 * @Author: xiaobing
	 * @Description: 切换分支
	 * @Date: 2018/10/14 首先判断本地是否已有此分支
	 * @param localPath
	 *            主仓
	 * @return
	 */
	public static String switchBranch(String localPath, String branch) {
		try {
			Git git = Git.open(new File(localPath));
			String newBranch = branch.substring(branch.lastIndexOf("/") + 1, branch.length());
			CheckoutCommand checkoutCommand = git.checkout();
			List<String> list = getLocalBranchNames(localPath);
			// 如果本地分支
			if (!list.contains(newBranch)) {
				git.branchCreate().setName(branch).call();
				/*
				 * checkoutCommand.setStartPoint(branch); checkoutCommand.setCreateBranch(true);
				 */
			}
			checkoutCommand.setName(newBranch);
			checkoutCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @Author: xiaobing
	 * @Description: 切换子仓的分支
	 * @Date: 2018/10/14
	 * @param localPath
	 *            主仓
	 * @param sub
	 *            子仓
	 * @param branch
	 *            分支名
	 */
	public static String switchSubhBranch(String localPath, String sub, String branch) {
		try {
			Git git = Git.open(new File(localPath + "\\.git\\modules" + sub));
			String newBranch = branch.substring(branch.lastIndexOf("/") + 1, branch.length());
			CheckoutCommand checkoutCommand = git.checkout();
			List<String> list = getLocalBranchNames(localPath + "\\.git\\modules" + sub);
			// 如果本地分支
			if (!list.contains(newBranch)) {
				checkoutCommand.setStartPoint(branch);
				checkoutCommand.setCreateBranch(true);
				checkoutCommand.setForce(true);
			}
			checkoutCommand.setName(newBranch);
			checkoutCommand.call();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @Author: xiaobing
	 * @Description: push到远程仓库
	 * @Date: 2018/10/14
	 */
	public static String pushRepository(String username, String password, String localPath) {
		try {
			Git git = Git.open(new File(localPath));
			PushCommand pushCommand = git.push();
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
			pushCommand.setCredentialsProvider(credentialsProvider);
			pushCommand.setForce(true).setPushAll();
			Iterable<PushResult> iterable = pushCommand.call();
			for (PushResult pushResult : iterable) {
				System.out.println(pushResult.toString());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @Author: xiaobing
	 * @Description: 远程提交子仓
	 * @Date: 2018/10/14
	 */
	public static String pushSubRepository(String username, String password, String localPath, String sub) {
		try {
			String newPath = localPath + "\\.git\\modules";
			Git git = Git.open(new File(newPath + sub));
			PushCommand pushCommand = git.push();
			CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(username, password);
			pushCommand.setCredentialsProvider(credentialsProvider);
			pushCommand.setForce(true).setPushAll();
			Iterable<PushResult> iterable = pushCommand.call();
			for (PushResult pushResult : iterable) {
				System.out.println(pushResult.toString());
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	/**
	 * @Author: xiaobing
	 * @Description: 拉取远程仓库内容至本地
	 * @param username
	 *            git用户名
	 * @param password
	 *            git密码
	 * @param localPath
	 *            下载已有仓库到本地路径
	 * @param branch
	 *            git分支
	 * @Date: 2018/9/25
	 */
	public static void gitPull(String username, String password, String localPath, String branch)
			throws IOException, GitAPIException {

		UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
				username, password);
		// git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		git.pull().setRemoteBranchName(branch).setCredentialsProvider(usernamePasswordCredentialsProvider).call();
		System.out.println("pull完成");
	}

	/**
	 * 拿到当前本地分支名
	 * 
	 * @param localPath
	 *            主仓
	 * @return
	 * @throws IOException
	 */
	public static String getCurrentBranch(String localPath) throws IOException {
		Git git = Git.open(new File(localPath));
		return git.getRepository().getBranch();
	}

	/**
	 * 拿到当前远程分支名
	 * 
	 * @param localPath
	 *            主仓
	 * @return
	 * @throws IOException
	 */
	public static String getCurrentRemoteBranch(String localPath) throws IOException {
		Git git = Git.open(new File(localPath));
		StoredConfig storedConfig = git.getRepository().getConfig();
		String currentRemote = storedConfig.getString("branch", getCurrentBranch(localPath), "remote");
		return currentRemote;
	}

	/**
	 * @Author: xiaobing
	 * @Description: 获取所有远程分支
	 * @Date: 2018/10/14
	 */
	public static List<String> gitAllBranch(String localPath) throws IOException, GitAPIException {
		List<String> result = new LinkedList<String>();
		// 获取名称
		Git git = Git.open(new File(localPath));
		StoredConfig storedConfig = git.getRepository().getConfig();
		String currentRemote = storedConfig.getString("branch", getCurrentBranch(localPath), "remote");
		// 根据名称获取所有分支名称
		Map<String, Ref> map = git.getRepository().getAllRefs();
		Set<String> keys = map.keySet();
		String index = "refs/remotes/" + currentRemote;
		for (String str : keys) {
			if (str.indexOf(index) > -1) {
				String el = str.substring(str.lastIndexOf("/") + 1, str.length());
				result.add(str);
				// System.out.println(el);
			}
		}
		return result;
	}

	public static void main(String[] args) throws IOException, GitAPIException {
		gitAllBranch("D:\\projectcaxs-cfs");
	}

	/**
	 * @Author: xiaobing
	 * @Description: 进行两版本之间文件对比
	 * @param commitList
	 *            commit对象，只能存在两个
	 * @param localPath
	 *            下载已有仓库到本地路径
	 * @Date: 2018/9/26
	 */
	public static Map<String, Object> gitdiff(List<RevCommit> commitList, String localPath)
			throws IOException, GitAPIException {
		Map<String, Object> map = new HashMap<>();
		Git git = new Git(new FileRepository(localPath + "/.git"));
		Repository repository = git.getRepository();
		AbstractTreeIterator newTree = prepareTreeParser(commitList.get(0), repository);
		AbstractTreeIterator oldTree = prepareTreeParser(commitList.get(1), repository);
		List<DiffEntry> diff = git.diff().setOldTree(oldTree).setNewTree(newTree).setShowNameAndStatusOnly(true).call();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DiffFormatter df = new DiffFormatter(out);
		// 设置比较器为忽略空白字符对比（Ignores all whitespace）
		df.setDiffComparator(RawTextComparator.WS_IGNORE_ALL);
		df.setRepository(git.getRepository());
		// 每一个diffEntry都是第个文件版本之间的变动差异
		int addSize = 0;
		int subSize = 0;
		for (DiffEntry diffEntry : diff) {
			// 打印文件差异具体内容
			df.format(diffEntry);
			String diffText = out.toString("UTF-8");

			// 获取文件差异位置，从而统计差异的行数，如增加行数，减少行数
			FileHeader fileHeader = df.toFileHeader(diffEntry);
			List<HunkHeader> hunks = (List<HunkHeader>) fileHeader.getHunks();

			for (HunkHeader hunkHeader : hunks) {
				EditList editList = hunkHeader.toEditList();
				for (Edit edit : editList) {
					subSize += edit.getEndA() - edit.getBeginA();
					addSize += edit.getEndB() - edit.getBeginB();

				}
			}
			out.reset();
		}
		map.put("addSize", addSize);
		map.put("subSize", subSize);
		return map;
	}

	private static AbstractTreeIterator prepareTreeParser(RevCommit commit, Repository repository) {
		try (RevWalk walk = new RevWalk(repository)) {
			RevTree tree = walk.parseTree(commit.getTree().getId());

			CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
			try (ObjectReader oldReader = repository.newObjectReader()) {
				oldTreeParser.reset(oldReader, tree.getId());
			}

			walk.dispose();

			return oldTreeParser;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * @Author: xiaobing
	 * @Description: 获取指定版本行数
	 * @Date: 2018/9/26
	 */
	public static int getAllFileLines(RevCommit commit, String localPath) throws IOException, GitAPIException {
		Git git = new Git(new FileRepository(localPath + "/.git"));
		Repository repository = git.getRepository();
		TreeWalk treeWalk = new TreeWalk(repository);
		int size = 0;
		try {
			treeWalk.addTree(commit.getTree());
			treeWalk.setRecursive(true);
			MutableObjectId id = new MutableObjectId();
			while (treeWalk.next()) {
				treeWalk.getObjectId(id, 0);
				int lines = countAddLine(BlobUtils.getContent(repository, id.toObjectId()));
				size += lines;
			}
		} catch (MissingObjectException e) {
			logger.error("error:" + e);
		} catch (IncorrectObjectTypeException e) {
			logger.error("error:" + e);
		} catch (CorruptObjectException e) {
			logger.error("error:" + e);
		} catch (IOException e) {
			logger.error("error:" + e);
		}
		return size;
	}

	/**
	 * 统计非空白行数
	 * 
	 * @param content
	 * @return
	 */
	public static int countAddLine(String content) {
		char[] chars = content.toCharArray();
		int sum = 0;
		boolean notSpace = false;
		for (char ch : chars) {
			if (ch == '\n' && notSpace) {
				sum++;
				notSpace = false;
			} else if (ch > ' ') {
				notSpace = true;
			}
		}
		// 最后一行没有换行时，如果有非空白字符，则+1
		if (notSpace) {
			sum++;
		}
		return sum;
	}

}
