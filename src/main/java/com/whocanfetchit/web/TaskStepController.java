package com.whocanfetchit.web;
import com.whocanfetchit.domain.TaskStep;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/tasksteps")
@Controller
@RooWebScaffold(path = "tasksteps", formBackingObject = TaskStep.class)
public class TaskStepController {
}
