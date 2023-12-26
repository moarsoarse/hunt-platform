package com.hunt.worker.handler;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
@ExternalTaskSubscription("check-number")
public class ExampleCheckNumberWorker implements ExternalTaskHandler {


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        // Get a process variable
        String someProcessVariable = (String) externalTask.getVariable("someProcessVariable");

        boolean isNumber = false;
        try {
            Long.parseLong(someProcessVariable);
            isNumber = true;
        } catch (NumberFormatException e) {
        }


        // Complete the task
        externalTaskService.complete(externalTask, Collections.singletonMap("isNumber", isNumber));
    }

}
