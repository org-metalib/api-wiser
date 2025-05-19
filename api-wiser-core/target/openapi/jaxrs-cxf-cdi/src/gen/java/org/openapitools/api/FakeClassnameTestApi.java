package org.openapitools.api;

import org.openapitools.model.Client;
import org.openapitools.api.FakeClassnameTestApiService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import io.swagger.annotations.*;
import java.io.InputStream;

import org.apache.cxf.jaxrs.ext.PATCH;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;
@Path("/fake_classname_test")
@RequestScoped

@Api(description = "the fake_classname_test API")


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSCXFCDIServerCodegen", date = "2025-05-18T20:12:30.067521-05:00[America/Chicago]", comments = "Generator version: 7.13.0")
public class FakeClassnameTestApi  {

  @Context SecurityContext securityContext;

  @Inject FakeClassnameTestApiService delegate;


    @PATCH
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "To test class name in snake case", notes = "To test class name in snake case", response = Client.class, authorizations = {
        
        @Authorization(value = "api_key_query")
         }, tags={ "fake_classname_tags 123#$%^" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Client.class) })
    public Response testClassname(@ApiParam(value = "client model" ,required=true) Client client) {
        return delegate.testClassname(client, securityContext);
    }
}
