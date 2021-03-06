package com.constanttherapy.service.proxies;

import com.constanttherapy.ServiceBase;
import com.constanttherapy.util.CTLogger;

import javax.ws.rs.core.UriInfo;
import java.util.Map;
import java.util.TreeMap;

public class PatientServiceProxy extends ServiceProxyBase
{
	private UriInfo uriInfo;

	public PatientServiceProxy(UriInfo uriInfo)
	{
		super(uriInfo, "ct-service-path");
	}

	public String doScheduleAdd(String scheduleJson, String patientIdString, String token, boolean autogenerated)
	{
		try
		{
			CTLogger.info("PatientServiceProxy::doScheduleAdd() - " + String.format("patientId=%s", patientIdString), 0);
			CTLogger.debug("schedule=" + scheduleJson);

			Map<String, String> queryParams = new TreeMap<String, String>();
			queryParams.put("token", token);

			if (autogenerated) queryParams.put("bot", "T");

			String path = "api/patient/" + patientIdString + "/schedule/add";

			String resp = doServicePost(path, queryParams, scheduleJson);

			return resp;
		}
		catch (Exception e)
		{
			CTLogger.error(e);
			return ServiceBase.constructErrorMessage("Error adding schedule", e);
		}
		finally
		{
			CTLogger.unindent();
		}
	}
}
