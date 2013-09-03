package com.whocanfetchit.web.service;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import com.whocanfetchit.domain.Location;
import com.whocanfetchit.domain.Task;
import com.whocanfetchit.domain.TaskStep;

public class TaskServiceTest {

	@Test
	public void test() throws JsonProcessingException, IOException {
		String request = "{\"owner\":\"anna@glomann.com\",\"description\":\"Pick up dry cleaning\",\"expenses\":\"48\",\"steps\":[{\"address\":\"Lean Cleaners, 1234 Example Rd, Miami FL\",\"latitude\":\"\",\"longitude\":\"\"}],\"dueDate\":\"2013-08-25T17:30:00.000-0400\"}";
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(request);
		Task task = new Task();
		task.setDescription(root.get("description").getTextValue());
		task.setExpenses(root.get("expenses").getDoubleValue());
		String dateStr = root.get("dueDate").getTextValue();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		Date date = null;
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			date = null;
		}
		task.setDueDate(date);
		List<TaskStep> steps = new ArrayList<TaskStep>();
		for (JsonNode stepNode : root.get("steps")) {
			Location location = new Location();
			location.setAddress(stepNode.get("address").getTextValue());
			location.setLatitude(stepNode.get("latitude").getDoubleValue());
			location.setLongitude(stepNode.get("longitude").getDoubleValue());
			TaskStep step = new TaskStep();
			step.setLocation(location);
			steps.add(step);
		}
		task.setSteps(steps);
	}

}
