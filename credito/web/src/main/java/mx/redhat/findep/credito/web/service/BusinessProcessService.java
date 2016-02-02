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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.audit.AuditService;
import org.kie.api.runtime.manager.audit.VariableInstanceLog;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.remote.client.api.RemoteRuntimeEngineFactory;

import mx.redhat.findep.credito.web.model.Pendiente;
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
    private final String P_APPLICATION_ID = "solicitud_id";
    private final String P_APPLICATION = "solicitud";
    private final String P_AMOUNT = "monto";
    private final String P_CLIENT_LIST = "clientes_list";
    private final String DEPLOYMENT_ID = "mx.com.findep:process-model:1.0";
	private final String USERNAME = "bpmsAdmin";
	private final String PASSWORD = "Redhat1!";
	private final String PROCESS_ID = "findep.credito";
	private final String SERVER_URL = "http://localhost:8081/business-central";
	
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
        
        Map<String, Object> params = new HashMap<String, Object>();
        
//        params.put(P_APPLICATION_ID, solicitud.getId());
		params.put(P_APPLICATION, solicitud);
		params.put(P_CLIENT_LIST, solicitud.getClientes());
		
		ProcessInstance instance = ksession.startProcess(PROCESS_ID, params);
		
		solicitud.setId(instance.getId());
		
        solicitudEventSrc.fire(solicitud);
        
        log.info("Application process started with ID: " + solicitud.getId());
        
        return solicitud;
    }
    
    public List<Pendiente> getTasks(String user) throws Exception
    {
    	log.info("Getting Tasks.");
    	
		RuntimeEngine engine = RemoteRuntimeEngineFactory.newRestBuilder()
				.addDeploymentId(DEPLOYMENT_ID)
				.addUserName(user)
				.addPassword(PASSWORD)
				.addUrl(new URL(SERVER_URL))
				.addExtraJaxbClasses(Solicitud.class)
				.build();
		
		TaskService taskService = engine.getTaskService();
		
		List<TaskSummary> tasks = taskService.getTasksAssignedAsPotentialOwner(user, "en-us");
		
		System.out.println(tasks.size());
		
		List<Pendiente> pendientes = new ArrayList<Pendiente>();
		
		Pendiente ap = null;
		
		for (TaskSummary tsummary : tasks) 
		{
			ap = new Pendiente();
			
			ap.setSolicitud(getVariableValue(tsummary.getProcessInstanceId(), P_APPLICATION_ID));
			ap.setMonto(getVariableValue(tsummary.getProcessInstanceId(), P_AMOUNT));;
			
			pendientes.add(ap);
		}
	
		return pendientes;
    }

    public Solicitud getApplication(String applicationKey) 
    {
    	log.info("Retrieving application.");
    	
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	CriteriaQuery<Solicitud> cq = cb.createQuery(Solicitud.class);
    	
    	Root<Solicitud> application = cq.from(Solicitud.class);
    	
    	ParameterExpression<String> p = cb.parameter(String.class);
    	
    	cq.select(application).where(cb.equal(application.get("solicitud"), p));
    	
    	TypedQuery<Solicitud> tq = em.createQuery(cq);
    	
    	tq.setParameter(p, applicationKey);
    	
    	Solicitud solicitud = tq.getSingleResult();
    	
    	return solicitud;
    }
    
    private String getVariableValue(long piid, String varName) 
    {
        String value = null;
        List<? extends VariableInstanceLog> variables = auditService.findVariableInstances(piid, varName);
        if (variables.size() > 0)
            value = variables.get(0).getValue();
        return value;
    }
}
