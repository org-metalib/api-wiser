package org.openapitools.api;

import org.openapitools.model.Client;
import org.openapitools.api.AnotherFakeApiService;

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
@Path("/another-fake/dummy")
@RequestScoped

@Api(description = "the another-fake API")


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSCXFCDIServerCodegen", date = "2025-05-18T20:12:30.067521-05:00[America/Chicago]", comments = "Generator version: 7.13.0")
public class AnotherFakeApi  {

  @Context SecurityContext securityContext;

  @Inject AnotherFakeApiService delegate;


    @PATCH
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "To test special tags", notes = "To test special tags and operation ID starting with number", response = Client.class, tags={ "$another-fake?" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Client.class) })
    public Response call123testSpecialTags(@ApiParam(value = "client model" ,required=true) Client client) {
        return delegate.call123testSpecialTags(client, securityContext);
    }
}
