package org.gb.sample.spark.income;

import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class TopIncomesMain {

	public static void main(String[] args) {
		System.out.println("Inside TopIncomesMain.main(), without SparkContext created.");
		SparkConf conf = new SparkConf().setAppName("Person profiles with top incomes");
		JavaSparkContext sc = new JavaSparkContext(conf);
/*		String nameVsIncomeFile = args[0];
		String personProfileFile = args[1];
		JavaRDD<String> nameVsIncomeLines = sc.textFile(nameVsIncomeFile);
		JavaRDD<String> personProfileLines = sc.textFile(personProfileFile);
		IncomeAnalyzer incomeAnalyzer = new BroadcastHashJoinIncomeAnalyzer(personProfileLines);
		Map<Integer, List<PersonProfile>> topIncomesWithPersonProfiles = incomeAnalyzer.getTopIncomePersonProfiles(nameVsIncomeLines, 10);
		topIncomesWithPersonProfiles.forEach(TopIncomesMain::printIncomeWithPersonProfiles);
		incomeAnalyzer.cleanup();*/
		sc.close();
	}

	private static void printIncomeWithPersonProfiles(Integer income, List<PersonProfile> personProfiles) {
		personProfiles.forEach(profile -> {
			String fullName = String.join(" ", profile.getFirstName(), profile.getLastName());
			String incomeWithPersProfileAsString = String.join(", ", fullName, profile.getCompanyName(),
					profile.getEmailAddress());
			System.out.println(income + " <--> " + incomeWithPersProfileAsString);
		});
	}

}
