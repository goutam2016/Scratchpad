package org.gb.sample.spark.nytaxitrips;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TripAnalyzerTest {

	private static final String TAXI_TRIP_FILE = "data/nytaxitrips/yellow_tripdata_1000.csv";
	//private static final String TAXI_TRIP_FILE = "data/nytaxitrips/bkp.txt";
	private static JavaSparkContext sparkContext;
	private static TripAnalyzer tripAnalyzer;

	@BeforeClass
	public static void setupForAll() {
		SparkConf conf = new SparkConf().setMaster("local").setAppName("New York Yellow Taxi trips");
		sparkContext = new JavaSparkContext(conf);
		JavaRDD<String> taxiTripLines = sparkContext.textFile(TAXI_TRIP_FILE);
		tripAnalyzer = new TripAnalyzer(taxiTripLines);
	}

	@AfterClass
	public static void teardownForAll() {
		sparkContext.close();
	}

	@Test
	public void getTripsWithPsngrsAboveTshld() {
		// Prepare test data
		final int tshldPsngrCnt = 6;

		// Setup expectations
		final int exptdTripCount = 39;

		// Invoke test target
		List<TaxiTrip> tripsWithPsngrsAboveTshld = tripAnalyzer.getTripsWithPsngrsAboveTshld(tshldPsngrCnt);

		// Verify results
		Assert.assertEquals(exptdTripCount, tripsWithPsngrsAboveTshld.size());
	}

	@Test
	public void getTripsBetweenPickupDropoffTimes() {
		// Prepare test data
		final LocalTime earliestPickupTime = LocalTime.MIDNIGHT;
		final LocalTime latestDropoffTime = LocalTime.of(6, 0);
		
		// Setup expectations
		final int exptdTripCount = 169;

		// Invoke test target
		List<TaxiTrip> tripsBtwnPickupDropoffTimes = tripAnalyzer.getTripsBetweenPickupDropoffTimes(earliestPickupTime,
				latestDropoffTime);

		// Verify results
		Assert.assertEquals(exptdTripCount, tripsBtwnPickupDropoffTimes.size());
	}
	
	@Test
	public void computeAvgTipAsPercentageOfFare() {
		// Setup expectations
		final double exptdAvgTipPercentage = 13.51;
		
		// Invoke test target
		double retndAvgTipPercentage = tripAnalyzer.computeAvgTipAsPercentageOfFare();
		
		// Verify results
		Assert.assertEquals(exptdAvgTipPercentage, retndAvgTipPercentage, 0.0);
	}
	
	@Test
	public void getTripStatsPerTimeBand() {
		TimeBand earlyMorning = new TimeBand(LocalTime.MIDNIGHT, LocalTime.of(6, 0));
		TimeBand morning = new TimeBand(LocalTime.of(6, 0), LocalTime.NOON);
		TimeBand afternoon = new TimeBand(LocalTime.NOON, LocalTime.of(18, 0));
		TimeBand night = new TimeBand(LocalTime.of(18, 0));

		List<TimeBand> timeBands = Arrays.asList(earlyMorning, morning, afternoon, night);
		
		Map<TimeBand, TripStats> tripStatsPerTimeBand = tripAnalyzer.getTripStatsPerTimeBand(timeBands);
		
		tripStatsPerTimeBand.forEach((timeBand, tripStats) -> System.out.println(timeBand + " --> " + tripStats));
		//tripStatsPerTimeBand.forEach((timeBand, tripStats) -> System.out.println(timeBand));
	}
}
