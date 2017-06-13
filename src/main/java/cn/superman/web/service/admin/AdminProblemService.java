package cn.superman.web.service.admin;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import cn.superman.constant.ConstantParameter;
import cn.superman.util.BeanMapperUtil;
import cn.superman.util.EncryptUtility;
import cn.superman.util.Log4JUtil;
import cn.superman.util.UUIDUtil;
import cn.superman.web.dao.ProblemDao;
import cn.superman.web.dao.base.BaseDao;
import cn.superman.web.dto.NewProblemDTO;
import cn.superman.web.dto.ProblemStandardFileDTO;
import cn.superman.web.dto.UpdateProblemDTO;
import cn.superman.web.dto.UploadProblemStandardFileDTO;
import cn.superman.web.exception.ServiceLogicException;
import cn.superman.web.po.Problem;
import cn.superman.web.service.page.PageService;

@Service
public class AdminProblemService extends PageService<Problem, Problem> {
	@Autowired
	private ProblemDao problemDao;

	public void addProblem(NewProblemDTO newProblemDTO) {
		Problem problem = BeanMapperUtil.map(newProblemDTO, Problem.class);
		try {
			// 创建该问题的目录等……
			// 获取存放所有题目的根目录
			String rootPath = ConstantParameter.PROBLEM_ROOT_PATH;
			// 生成存放该题目根目录的名称
			String dirName = UUIDUtil.getUUID();
			// 存放标准输入的文件夹名字
			String inDirName = ConstantParameter.PROBLEM_IN_DIR_NAME;
			// 存放标准输出的文件夹
			String outDirName = ConstantParameter.PROBLEM_OUT_DIR_NAME;

			File inDir = new File(rootPath + File.separator + dirName
					+ File.separator + inDirName);
			inDir.mkdirs();

			File outDir = new File(rootPath + File.separator + dirName
					+ File.separator + outDirName);
			outDir.mkdirs();

			problem.setInputFileRootPath(inDir.getAbsolutePath());
			problem.setOutputFileRootPath(outDir.getAbsolutePath());
			problem.setProblemVersion(1);
			problem.setTotalRightCount(0);
			problem.setTotalSubmitCount(0);
			problemDao.add(problem);
		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("添加题目失败");
		}

	}

	public void deleteProblem(BigInteger problemId) {
		Problem problem = problemDao.findById(problemId);
		if (problem == null) {
			throw new ServiceLogicException("题目编号不对，无法找到该题目");
		}
		try {
			problemDao.deleteById(problem.getProblemId());
		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("数据库操作失败，请联系管理人员");
		}

		String inputFileRootPath = problem.getInputFileRootPath();
		String outputFileRootPath = problem.getOutputFileRootPath();
		try {
			FileUtils.deleteDirectory(new File(inputFileRootPath));
			FileUtils.deleteDirectory(new File(outputFileRootPath));
		} catch (IOException e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("题目删除成功，但是题目的测试用例部分删除失败，请联系管理人员");
		}
	}

	public void updateProblem(UpdateProblemDTO dto) {
		try {
			Problem problem = BeanMapperUtil.map(dto, Problem.class);
			if (problem.getProblemVersion() != null) {
				problem.setProblemVersion(problem.getProblemVersion() + 1);
			}
			problemDao.update(problem);
		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("数据库内容更新失败");
		}
	}

	public void uploadProblemStandardFile(UploadProblemStandardFileDTO dto) {
		try {
			// 查出这个问题的内容的存放根目录先
			Problem problem = problemDao.findById(dto.getProblemId());
			// 输入和输出文件，采用同一个名字
			String filePrefixName = System.currentTimeMillis() + "";

			File inputFile = new File(problem.getInputFileRootPath()
					+ File.separator + filePrefixName + ".txt");

			FileUtils.copyInputStreamToFile(dto.getStandardInputFileStream(),
					inputFile);

			File outputFile = new File(problem.getOutputFileRootPath()
					+ File.separator + filePrefixName + ".txt");

			FileUtils.copyInputStreamToFile(dto.getStandardOutputFileStream(),
					outputFile);

		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("上传失败");
		}

	}

	public List<ProblemStandardFileDTO> getProblemStandardFilesById(
			Integer problemId) {
		List<ProblemStandardFileDTO> dtos = new ArrayList<ProblemStandardFileDTO>();
		ProblemStandardFileDTO tempDto = null;
		// 查出这个问题的内容的存放根目录先
		Problem problem = problemDao.findById(problemId);
		File inputDir = new File(problem.getInputFileRootPath());
		File outputDir = new File(problem.getOutputFileRootPath());

		if (!inputDir.exists() || !outputDir.exists()) {
			return dtos;
		}

		File[] inputFiles = inputDir.listFiles();
		File[] outputFiles = outputDir.listFiles();
		if (inputFiles.length != outputFiles.length) {
			throw new ServiceLogicException("输入文件和输出文件数量，不一致！请直接 进入相应文件夹中，进行检查");
		}

		// 虽然名字一样，但是在数组中的位置不一定是一样的，所以为了保险,用个map来维护映射关系
		Map<String, File> tempMap = new HashMap<String, File>();
		for (int i = 0; i < outputFiles.length; i++) {
			tempMap.put(outputFiles[i].getName(), outputFiles[i]);
		}

		for (int i = 0; i < inputFiles.length; i++) {
			tempDto = new ProblemStandardFileDTO();

			try {

				// 加密路径
				tempDto.setInputFilePath(Base64Utils
						.encodeToUrlSafeString(EncryptUtility
								.AESEncoding(
										ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
										inputFiles[i].getAbsolutePath())
								.getBytes()));
				tempDto.setInputFilePageShowName("input" + (i + 1));
				// 加密路径
				tempDto.setOutputFilePath(Base64Utils
						.encodeToUrlSafeString(EncryptUtility
								.AESEncoding(
										ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
										tempMap.get(inputFiles[i].getName())
												.getAbsolutePath()).getBytes()));
				tempDto.setOutputFilePageShowName("output" + (i + 1));
				dtos.add(tempDto);
			} catch (Exception e) {
				Log4JUtil.logError(e);
			}

		}

		return dtos;
	}

	public void deleteProblemStandardFile(String inputFilePath,
			String outputFilePath) {
		// 先解密路径
		try {
			String realInputFilePath = EncryptUtility.AESDencoding(
					ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
					new String(Base64Utils
							.decodeFromUrlSafeString(inputFilePath)));
			String realOutputFilePath = EncryptUtility.AESDencoding(
					ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
					new String(Base64Utils
							.decodeFromUrlSafeString(outputFilePath)));

			File inputFile = new File(realInputFilePath);
			int index = 0;

			if (inputFile.exists()) {
				if (inputFile.delete()) {
					index++;
				}
			}

			File outputFile = new File(realOutputFilePath);
			if (outputFile.exists()) {
				if (outputFile.delete()) {
					index++;
				}
			}

			if (index != 2) {
				throw new ServiceLogicException("有部分删除未成功");
			}
		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new ServiceLogicException("删除失败");
		}
	}

	public File decodeToFileByFilePath(String filePath) {
		try {

			String path = EncryptUtility.AESDencoding(
					ConstantParameter.PROBLEM_STANDARD_FILE_PATH_SEED,
					new String(Base64Utils.decodeFromUrlSafeString(filePath)));

			File file = new File(path);
			if (!file.exists()) {
				throw new RuntimeException("无法获取来文件");
			}

			return file;
		} catch (Exception e) {
			Log4JUtil.logError(e);
			throw new RuntimeException("无法获取来文件");
		}
	}

	@Override
	public BaseDao<Problem, Problem> getUseDao() {
		return problemDao;
	}
}
