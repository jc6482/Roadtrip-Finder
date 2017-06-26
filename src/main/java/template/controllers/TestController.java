package template.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TestController {
	@Autowired
	private FooService fooService;

	@RequestMapping("/test")
	public String testLanding(@RequestParam(value = "param1", required = false) String value1, @RequestParam(value = "param2") String value2, Model model) {
		model.addAttribute("data1", "We have some data passed to our view!");

		String result = fooService.foo(value1, value2);
		model.addAttribute("serviceResult", result);
		return "test";
	}

	@Service 
	public static class FooService {
		public String foo(String value1, String value2) {
			String result = value1;
			return result;
		}
	}
}