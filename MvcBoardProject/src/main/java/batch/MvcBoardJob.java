package batch;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import domain.MvcBoard;
import repository.MvcBoardDAO;

public class MvcBoardJob implements Job {
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		MvcBoard dto = MvcBoardDAO.getInstance().selectMvcBoardBestHit();
		System.out.println("=====최대 조회수 게시글=====");
		System.out.println("제목: " + dto.getTitle());
		System.out.println("내용: " + dto.getContent());
		System.out.println("조회수: " + dto.getHit());
	}
}
