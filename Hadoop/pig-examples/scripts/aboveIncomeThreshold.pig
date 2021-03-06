-- pig -x local -param_file pig-examples/scripts/aboveIncomeThreshold.param.local pig-examples/scripts/aboveIncomeThreshold.pig
-- pig -x mapreduce -param minIncome=139950 pig-examples/scripts/aboveIncomeThreshold.pig
-- pig -x local -param minIncome=75000 pig-examples/scripts/aboveIncomeThreshold.pig

REGISTER pig-examples/target/pig-examples-0.0.1-SNAPSHOT.jar;
DEFINE checkIncomeThreshold org.gb.sample.pig.income.CheckIncomeThreshold();
DEFINE nameVsIncomeStorage org.gb.sample.pig.income.NameVsIncomeStorage();

nameVsIncome = LOAD '$nameVsIncomeDataFile' USING PigStorage(',') AS (firstName:chararray,lastName:chararray,income:int);
namesWithIncomeOverThreshold = FILTER nameVsIncome BY checkIncomeThreshold($minIncome, income);
-- Delete the output directory if it already exists.
RMF $outputPath
STORE namesWithIncomeOverThreshold INTO '$outputPath' USING nameVsIncomeStorage;
