/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mx.redhat.findep.credito.web.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

import mx.redhat.findep.credito.web.model.AnalisisPendientes;
import mx.redhat.findep.credito.web.model.Solicitud;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class BusinessProcessService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    @Inject
    private Event<Solicitud> solicitudEventSrc;
    
    // BPM Suite Process and Project constants
    private final String P_APPLICATION = "solicitud";
    private final String DEPLOYMENT_ID = "mx.com.findep:process-model:1.0";
	private final String USERNAME = "bpmsAdmin";
	private final String PASSWORD = "Redhat1!";
	private final String PROCESS_ID = "findep.credito";
	private final String SERVER_URL = "http://192.168.1.89:8081/business-central";
	
	// BPM Suite classes
	private RuntimeEngine engine;
	private KieSession ksession;
	private TaskService taskService;
	private AuditService auditService;
	
	public BusinessProcessService() throws MalformedURLException 
	{
		engine = RemoteRuntimeEngineFactory.newRestBuilder()
				.addDeploymentId(DEPLOYMENT_ID)
				.addUserName(USERNAME)
				.addPassword(PASSWORD)
				.addUrl(new URL(SERVER_URL))
				.addExtraJaxbClasses(Solicitud.class)
				.build();
		taskService = engine.getTaskService();
		ksession = engine.getKieSession();
		auditService = engine.getAuditService();
	}

    public Solicitud startProcess(Solicitud solicitud) throws Exception 
    {
        log.info("Processing application.");
        
//        em.persist(solicitud);
        Map<String, Object> params = new HashMap<String, Object>();
        
		params.put(P_APPLICATION, solicitud);
		
		ProcessInstance instance = ksession.startProcess(PROCESS_ID, params);
		
		solicitud.setId(instance.getId());
        
        solicitudEventSrc.fire(solicitud);
        
        log.info("Application process started with ID: " + solicitud.getId());
        
        return solicitud;
    }
    
    public List<AnalisisPendientes> getTasks(String user) throws Exception
    {
		RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
				.addDeploymentId(DEPLOYMENT_ID)
				.addUserName(user)
				.addPassword(PASSWORD)
				.addUrl(new URL(SERVER_URL))
				.addExtraJaxbClasses(Solicitud.class)
				.build();
		
		TaskService taskService = engine.getTaskService();
		
		List<TaskSummary> tasks = taskService.getTasksOwned(user, "en-us");
		
		System.out.println(tasks.size());
		
		List<AnalisisPendientes> pendientes = new ArrayList<AnalisisPendientes>();
		
		AnalisisPendientes ap = null;
		
		for (TaskSummary tsummary : tasks) 
		{
			Long taskId = tsummary.getId();
			
			ap = new AnalisisPendientes();
			ap.setId(taskId);
			ap.setNombre(tsummary.getName());
			
			pendientes.add(ap);
			
			System.out.println(ap.getId() + " => " + ap.getNombre());
		}
	
		return pendientes;
    }
}
