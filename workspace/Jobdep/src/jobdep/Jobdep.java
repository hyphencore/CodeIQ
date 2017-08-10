package jobdep;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/*
 * 1つのジョブ依存を管理する
 */
class Job {
	String jobName; // ジョブ名
	ArrayList<String> jobDeps;	// 依存関係のあるジョブリスト

	Job(String name) {
		jobName = name;
		jobDeps = new ArrayList<String>();
	}

	// このジョブに依存ジョブを追加する
	void add(String jdName) {
		int index;

		// 依存ジョブがない場合だけ追加
		index = jobDeps.indexOf(jdName);
		if (index < 0) {
			jobDeps.add(jdName);
		}
	}

	// このジョブから依存ジョブを消去
	void delete(String jdName) {
		int index;

		index = jobDeps.indexOf(jdName);
		if (index >= 0) {
			jobDeps.remove(index);
		}
	}
}

class Jobdep {
	public static void exec(ArrayList<Job> jobs) {
		ArrayList<String> execList = new ArrayList<String>();	// 実行リスト
		int index;
		boolean execok;
		String jobName, jobDepsName;

		while (jobs.size() > 0) { // ジョブがある限り続ける
			execList.clear(); // 実行リストの初期化

			// 依存先JOBの処理
			for (int i=0 ; i<jobs.size() ; i++) {
				if (jobs.get(i).jobDeps.size() > 0) { // 依存ジョブがある場合のジョブ
					for (int j=0 ; j<jobs.get(i).jobDeps.size() ; j++) {
						jobDepsName = jobs.get(i).jobDeps.get(j);
						// その依存ジョブが依存ジョブリストにない場合は、依存関係無し（または依存先が実行済み）のため実行
						execok = true;
						for (int k=0 ; k<jobs.size() ; k++) {
							jobName = jobs.get(k).jobName;
							if (jobDepsName.compareTo(jobName) == 0) { // 存在していた
								execok = false;
							}
						}
						if (execok) {
							index = execList.indexOf(jobDepsName);
							if (index < 0) { // 実行リストに存在しないときだけ追加
								execList.add(jobs.get(i).jobDeps.get(j));
							}
						}
					}
				}
			}

			// JOBの処理
			for (int i=0 ; i<jobs.size() ; i++) {
				jobName = jobs.get(i).jobName;
				// 依存先がない場合は実行リストに追加する
				if (jobs.get(i).jobDeps.size() <= 0) { // 依存ジョブがなくなったジョブ
					index = execList.indexOf(jobName);
					if (index < 0) { // 実行リストに存在しないときだけ追加
						execList.add(jobName);
					}
				}
			}

			// 出力
			Collections.sort(execList);
			for (int i=0 ; i<execList.size() ; i++) {
				if (i == 0) System.out.print(execList.get(i));
				else System.out.print(" " + execList.get(i));
			}
			System.out.println("");

			// 実行したリストをもとに、依存関係を消していく
			for (int i=0 ; i<execList.size() ; i++) {
				for (int j=jobs.size()-1 ; j>=0 ; j--) {
					index = jobs.get(j).jobDeps.indexOf(execList.get(i));
					if (index >= 0) jobs.get(j).jobDeps.remove(index);	// 実行したジョブが依存にあれば消す
					if (jobs.get(j).jobName.compareTo(execList.get(i)) == 0) jobs.remove(j); // ジョブを消す
				}
			}
		}
	}

	public static void parse(ArrayList<String> lines, ArrayList<Job> jobdeps) {
		String[] ret, ret2;
		Job job;

		for (int i=0 ; i<lines.size() ; i++) {
			ret = lines.get(i).split(":");	// : で分離
			job = new Job(ret[0].trim()); // 新規ジョブ
			jobdeps.add(job);
			if (ret.length > 1) { // 依存関係がある
				ret2 = ret[1].trim().split(" "); // 依存ジョブの抽出
				for (int j=0 ; j<ret2.length ; j++) {
					job.jobDeps.add(ret2[j].trim());
				}
			}
		}
	}

	public static void main(String[] args) {
		Scanner cin = new Scanner(System.in);
		ArrayList<Job> jobs = new ArrayList<Job>();
		ArrayList<String> lines = new ArrayList<String>();

		while (cin.hasNext()) {
			lines.add(cin.nextLine());
		}
		cin.close();

		parse(lines, jobs);

		exec(jobs);
	}

}
