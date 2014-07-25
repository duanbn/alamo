package com.duanbn.alamo.test;

import org.junit.Test;

import com.duanbn.alamo.Rule;
import com.duanbn.alamo.RuleBuilder;
import com.duanbn.alamo.Validate;
import com.duanbn.alamo.validator.impl.MobileValidator;

public class ValidateTest {

	@Test
	public void testMobile() throws Exception {
		Rule r = RuleBuilder.build();
		r.addValidator(MobileValidator.class);
		Validate.check("14527268854", r);
	}

}
