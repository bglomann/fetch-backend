package com.whocanfetchit.web;
import com.whocanfetchit.domain.Participant;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/participants")
@Controller
@RooWebScaffold(path = "participants", formBackingObject = Participant.class)
public class ParticipantController {
}
