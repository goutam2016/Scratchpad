package org.gb.sample.hadoop.income;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TopIncomeReducer extends Reducer<Text, PersonIncome, BigDecimal, BriefPersonProfile> {

	private PersonIncome copyPersonIncome(PersonIncome origPersIncome) {
		PersonIncome copiedPersIncome = new PersonIncome();
		copiedPersIncome.setFirstName(origPersIncome.getFirstName());
		copiedPersIncome.setLastName(origPersIncome.getLastName());
		copiedPersIncome.setIncome(origPersIncome.getIncome());
		copiedPersIncome.setCompanyName(origPersIncome.getCompanyName());
		copiedPersIncome.setEmailAddress(origPersIncome.getEmailAddress());
		return copiedPersIncome;
	}
	
	private BriefPersonProfile createBriefPersProfile(PersonIncome personIncome) {
		BriefPersonProfile briefPersonProfile = new BriefPersonProfile();
		briefPersonProfile.setFullName(String.join(" ", personIncome.getFirstName(), personIncome.getLastName()));
		briefPersonProfile.setCompanyName(personIncome.getCompanyName());
		briefPersonProfile.setEmailAddress(personIncome.getEmailAddress());
		return briefPersonProfile;
	}
	
	@Override
	protected void reduce(Text key, Iterable<PersonIncome> values,
			Reducer<Text, PersonIncome, BigDecimal, BriefPersonProfile>.Context context)
			throws IOException, InterruptedException {
		List<PersonIncome> personIncomes = new ArrayList<>();
		values.forEach(persIncome -> personIncomes.add(copyPersonIncome(persIncome)));

		Comparator<PersonIncome> incomeComparator = Comparator.comparing(PersonIncome::getIncome).reversed();
		personIncomes.sort(incomeComparator);

		int numberFromTop = (personIncomes.size() < 10) ? personIncomes.size() : 10;

		for (int i = 0; i < numberFromTop; i++) {
			PersonIncome personIncome = personIncomes.get(i);
			context.write(personIncome.getIncome(), createBriefPersProfile(personIncome));
		}
	}	
}
