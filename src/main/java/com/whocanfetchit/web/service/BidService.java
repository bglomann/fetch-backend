package com.whocanfetchit.web.service;

import java.util.ArrayList;
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
import com.whocanfetchit.domain.BidState;
import com.whocanfetchit.domain.Participant;
import com.whocanfetchit.domain.Task;

@Controller
@RequestMapping("/service/bids*")
public class BidService {
	
	private static final Logger log = LoggerFactory.getLogger(BidService.class);

	private ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Map<?, ?> createBid(@RequestBody String body, HttpServletRequest request) {
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
		
		long taskId;
		try {
			taskId = Long.parseLong(root.get("taskId").getTextValue());
		} catch (NumberFormatException e) {
			taskId = -1;
		}
		Task task = Task.findTask(taskId);

		String bidderLogin = root.get("bidder").getTextValue();
		List<Participant> list = Participant.findParticipantsByLoginEquals(bidderLogin).getResultList();
		if (list.isEmpty()) {
			result.put("error", "User " + bidderLogin + " not found");
			return result;
		}
		Participant bidder = list.get(0);

		Bid bid = new Bid();
		bid.setBidState(BidState.OPEN);
		bid.setTask(task);
		bid.setBidder(bidder);
		double amount;
		try {
			amount = Double.parseDouble(root.get("amount").getTextValue());
		} catch (NumberFormatException e) {
			amount = -1;
		}
		if (amount < 0) {
			result.put("error", "Invalid bid amount");
			return result;
		}
		bid.setAmount(amount);
		
		bid.persist();

		result.put("id", bid.getId());
		return result;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Map<?, ?> findBids(HttpServletRequest request) {
		String taskIdStr = request.getParameter("taskId");
		log.debug("taskId=" + taskIdStr);

		Map<Object, Object> result = new HashMap<Object, Object>();
		long taskId;
		try {
			taskId = Long.parseLong(taskIdStr);
		} catch (NumberFormatException e) {
			result.put("error", "Invalid request parameters");
			return result;
		}
		Task task = Task.findTask(taskId);

		List<Object> resultBids = new ArrayList<Object>();
		for (Bid bid : task.getBids()) {
			resultBids.add(bidToJson(bid));
		}

		result.put("bids", resultBids);
		return result;
	}
	
	private Map<?, ?> bidToJson(Bid bid) {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("id", bid.getId());
		map.put("amount", bid.getAmount());
		map.put("bidder", bid.getBidder().getLogin());
		return map;
	}
}
