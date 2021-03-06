package org.gb.sample.spark.nytaxitrips;

import java.time.Month;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.util.LongAccumulator;

public class TripsByPassengerCountMain {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("New York Yellow Taxis - selected trips");
		JavaSparkContext sparkContext = new JavaSparkContext(conf);
		LongAccumulator defectiveRecords = sparkContext.sc().longAccumulator("Defective Records");
		JavaRDD<TaxiTrip> tripData = loadTripData(sparkContext, args[0], defectiveRecords);
		TripAnalyzer tripAnalyzer = new RDDTripAnalyzer(tripData);
		final int tshldPsngrCnt = 8;
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);
		System.out.printf("No. of taxi trips with at least %d passengers: %d.\n", tshldPsngrCnt,
				tripsWithPsngrsAboveTshld.size());
		Map<Integer, Integer> tripCountPerPsngrCount = tripAnalyzer.getTripCountPerPsngrCount();
		tripCountPerPsngrCount.forEach((psngrCnt, tripCnt) -> System.out.printf("%d <--> %d\n", psngrCnt, tripCnt));
		Map<Month, Double> avgPsngrCountPerMonth = tripAnalyzer.getAvgPsngrCountPerMonth();
		avgPsngrCountPerMonth.forEach((month, avgPsngrCnt) -> System.out
				.printf("Average passenger count for %s --> %2.2f\n", month, avgPsngrCnt));
		sparkContext.close();
	}

	private static JavaRDD<TaxiTrip> loadTripData(JavaSparkContext sparkContext, String taxiTripFile,
			LongAccumulator defectiveRecords) {
		TripDataLoader loader = new TextFileLoader(sparkContext, taxiTripFile, defectiveRecords);
		return loader.fetchRecords();
	}
}
