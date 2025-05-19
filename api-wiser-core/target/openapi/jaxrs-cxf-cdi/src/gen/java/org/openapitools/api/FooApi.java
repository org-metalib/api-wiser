package org.openapitools.api;

import org.openapitools.model.FooGetDefaultResponse;
import org.openapitools.api.FooApiService;

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
@Path("/foo")
@RequestScoped

@Api(description = "the foo API")


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSCXFCDIServerCodegen", date = "2025-05-18T20:12:30.067521-05:00[America/Chicago]", comments = "Generator version: 7.13.0")
public class FooApi  {

  @Context SecurityContext securityContext;

  @Inject FooApiService delegate;


    @GET
    
    
    @Produces({ "application/json" })
    @ApiOperation(value = "", notes = "", response = FooGetDefaultResponse.class, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "response", response = FooGetDefaultResponse.class) })
    public Response fooGet() {
        return delegate.fooGet(securityContext);
    }
}
