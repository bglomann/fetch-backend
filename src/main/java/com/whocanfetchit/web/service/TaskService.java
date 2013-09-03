package com.whocanfetchit.web.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.whocanfetchit.domain.Bid;
import com.whocanfetchit.domain.Location;
import com.whocanfetchit.domain.Participant;
import com.whocanfetchit.domain.Task;
import com.whocanfetchit.domain.TaskState;
import com.whocanfetchit.domain.TaskStep;


@Controller
@RequestMapping("/service/tasks*")
public class TaskService {
	
	private static final Logger log = LoggerFactory.getLogger(TaskService.class);
	
	private ObjectMapper mapper = new ObjectMapper();
	
	//private String datePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	private String datePattern = "M/d/yyyy";

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<?, ?> createTask(@RequestBody String body, HttpServletRequest request) {
		log.debug("body=" + body);
		Map<Object, Object> result = new HashMap<Object, Object>();
		JsonNode root;
		try {
			root = mapper.readTree(body);
		} catch (final Exception e1) {
			log.warn("Failed to parse request body", e1);
			result.put("error", e1.getMessage());
			return result;
		}
		
		String ownerLogin = root.get("owner").getTextValue();
		List<Participant> list = Participant.findParticipantsByLoginEquals(ownerLogin).getResultList();
		if (list.isEmpty()) {
			result.put("error", "User " + ownerLogin + " not found");
			return result;
		}
		Participant owner = list.get(0);

		Task task = new Task();
		task.setOwner(owner);
		task.setTaskState(TaskState.OPEN);
		task.setDescription(root.get("description").getTextValue());
		String str = root.get("expenses").getTextValue();
		double expenses = 0;
		try {
			expenses = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			expenses = 0;
		}
		task.setExpenses(expenses);
		String dateStr = root.get("dueDate").getTextValue();
		DateFormat dateFormat = new SimpleDateFormat(datePattern);
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
			location.persist();
			TaskStep step = new TaskStep();
			step.setLocation(location);
			step.persist();
			steps.add(step);
		}
		task.setSteps(steps);
		
		task.persist();

		result.put("id", task.getId());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<?, ?> findTasks(HttpServletRequest request) {
		String latStr = request.getParameter("lat");
		String lngStr = request.getParameter("lng");
		String radiusStr = request.getParameter("radius");
		log.debug("lat=" + latStr + " lng=" + lngStr + " radius=" + radiusStr);

		Map<Object, Object> result = new HashMap<Object, Object>();
		double lat, lng, radius;
		try {
			lat = Double.parseDouble(latStr);
			lng = Double.parseDouble(lngStr);
			radius = Double.parseDouble(radiusStr);
		} catch (NumberFormatException e) {
			result.put("error", "Invalid request parameters");
			return result;
		}
		
		// TODO: compute bounds for lat&lng to query for
		List<Task> tasks = Task.findTasksByTaskState(TaskState.OPEN).getResultList();
		List<Object> resultTasks = new ArrayList<Object>();
		// TODO: use Google distance matrix service to get driving distances

		DateFormat dateFormat = new SimpleDateFormat(datePattern);
		for (Task task : tasks) {
			boolean allInRange = true;
			for (TaskStep step : task.getSteps()) {
				double distance = computeDistance(lat, lng,
						step.getLocation().getLatitude(), step.getLocation().getLongitude());
				if (distance > radius) {
					allInRange = false;
					break;
				}
			}
			
			if (allInRange && !task.getSteps().isEmpty()) {
				resultTasks.add(taskToJson(task, dateFormat));
			}
		}
		result.put("tasks", resultTasks);
		return result;
	}
	
	private Map<?, ?> taskToJson(Task task, DateFormat dateFormat) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("id", task.getId());
		map.put("description", task.getDescription());
		map.put("expenses", task.getExpenses());
		map.put("dueDate", dateFormat.format(task.getDueDate()));
		
		List<Object> steps = new ArrayList<Object>();
		for (TaskStep step : task.getSteps()) {
			Map<Object, Object> location = new HashMap<Object, Object>();
			location.put("address", step.getLocation().getAddress());
			location.put("latitude", step.getLocation().getLatitude());
			location.put("longitude", step.getLocation().getLongitude());
			steps.add(location);
		}
		map.put("steps", steps);

		double lowestBid = Double.POSITIVE_INFINITY;
		for (Bid bid : task.getBids()) {
			if (bid.getAmount() < lowestBid) {
				lowestBid = bid.getAmount();
			}
		}
		if (!Double.isInfinite(lowestBid)) {
			map.put("lowestBid", lowestBid);
		}
		
		return map;
	}
	
	
	private double computeDistance(double lat1, double lon1, double lat2, double lon2) {
		double R = 3956.4; // miles
		double dLat = Math.toRadians(lat2-lat1);
		double dLon = Math.toRadians(lon2-lon1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		        Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2); 
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
		return R * c;
	}
}
