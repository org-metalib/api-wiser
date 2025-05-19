package org.openapitools.api;

import java.math.BigDecimal;
import org.openapitools.model.Client;
import org.openapitools.model.FakeBigDecimalMap200Response;
import org.openapitools.model.FileSchemaTestClass;
import org.openapitools.model.HealthCheckResult;
import org.joda.time.LocalDate;
import java.util.Map;
import org.openapitools.model.ModelApiResponse;
import org.openapitools.model.OuterComposite;
import org.openapitools.model.OuterEnum;
import org.openapitools.model.User;
import org.openapitools.api.FakeApiService;

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
@Path("/fake")
@RequestScoped

@Api(description = "the fake API")


@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSCXFCDIServerCodegen", date = "2025-05-18T20:12:30.067521-05:00[America/Chicago]", comments = "Generator version: 7.13.0")
public class FakeApi  {

  @Context SecurityContext securityContext;

  @Inject FakeApiService delegate;


    @GET
    @Path("/BigDecimalMap")
    
    @Produces({ "*/*" })
    @ApiOperation(value = "", notes = "for Java apache and Java native, test toUrlQueryString for maps with BegDecimal keys", response = FakeBigDecimalMap200Response.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = FakeBigDecimalMap200Response.class) })
    public Response fakeBigDecimalMap() {
        return delegate.fakeBigDecimalMap(securityContext);
    }

    @GET
    @Path("/health")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Health check endpoint", notes = "", response = HealthCheckResult.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "The instance started successfully", response = HealthCheckResult.class) })
    public Response fakeHealthGet() {
        return delegate.fakeHealthGet(securityContext);
    }

    @POST
    @Path("/outer/boolean")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "", notes = "Test serialization of outer boolean types", response = Boolean.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Output boolean", response = Boolean.class) })
    public Response fakeOuterBooleanSerialize(@ApiParam(value = "Input boolean as post body" ) Boolean body) {
        return delegate.fakeOuterBooleanSerialize(body, securityContext);
    }

    @POST
    @Path("/outer/composite")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "", notes = "Test serialization of object with outer number type", response = OuterComposite.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Output composite", response = OuterComposite.class) })
    public Response fakeOuterCompositeSerialize(@ApiParam(value = "Input composite as post body" ) OuterComposite outerComposite) {
        return delegate.fakeOuterCompositeSerialize(outerComposite, securityContext);
    }

    @POST
    @Path("/outer/number")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "", notes = "Test serialization of outer number types", response = BigDecimal.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Output number", response = BigDecimal.class) })
    public Response fakeOuterNumberSerialize(@ApiParam(value = "Input number as post body" ) BigDecimal body) {
        return delegate.fakeOuterNumberSerialize(body, securityContext);
    }

    @POST
    @Path("/outer/string")
    @Consumes({ "application/json" })
    @Produces({ "*/*" })
    @ApiOperation(value = "", notes = "Test serialization of outer string types", response = String.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Output string", response = String.class) })
    public Response fakeOuterStringSerialize(@ApiParam(value = "Input string as post body" ) String body) {
        return delegate.fakeOuterStringSerialize(body, securityContext);
    }

    @GET
    @Path("/array-of-enums")
    
    @Produces({ "application/json" })
    @ApiOperation(value = "Array of Enums", notes = "", response = OuterEnum.class, responseContainer = "List", tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Got named array of enums", response = OuterEnum.class, responseContainer = "List") })
    public Response getArrayOfEnums() {
        return delegate.getArrayOfEnums(securityContext);
    }

    @PUT
    @Path("/body-with-file-schema")
    @Consumes({ "application/json" })
    
    @ApiOperation(value = "", notes = "For this test, the body for this request much reference a schema named `File`.", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class) })
    public Response testBodyWithFileSchema(@ApiParam(value = "" ,required=true) FileSchemaTestClass fileSchemaTestClass) {
        return delegate.testBodyWithFileSchema(fileSchemaTestClass, securityContext);
    }

    @PUT
    @Path("/body-with-query-params")
    @Consumes({ "application/json" })
    
    @ApiOperation(value = "", notes = "", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class) })
    public Response testBodyWithQueryParams( @NotNull @ApiParam(value = "",required=true)  @QueryParam("query") String query, @ApiParam(value = "" ,required=true) User user) {
        return delegate.testBodyWithQueryParams(query, user, securityContext);
    }

    @PATCH
    
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "To test \"client\" model", notes = "To test \"client\" model", response = Client.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Client.class) })
    public Response testClientModel(@ApiParam(value = "client model" ,required=true) Client client) {
        return delegate.testClientModel(client, securityContext);
    }

    @POST
    
    @Consumes({ "application/x-www-form-urlencoded" })
    
    @ApiOperation(value = "Fake endpoint for testing various parameters 假端點 偽のエンドポイント 가짜 엔드 포인트 ", notes = "Fake endpoint for testing various parameters 假端點 偽のエンドポイント 가짜 엔드 포인트 ", response = Void.class, authorizations = {
        
        @Authorization(value = "http_basic_test")
         }, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid username supplied", response = Void.class),
        @ApiResponse(code = 404, message = "User not found", response = Void.class) })
    public Response testEndpointParameters(@FormParam(value = "number")  BigDecimal number, @FormParam(value = "double")  Double _double, @FormParam(value = "pattern_without_delimiter")  String patternWithoutDelimiter, @FormParam(value = "byte")  byte[] _byte, @FormParam(value = "integer")  Integer integer, @FormParam(value = "int32")  Integer int32, @FormParam(value = "int64")  Long int64, @FormParam(value = "float")  Float _float, @FormParam(value = "string")  String string,  @Multipart(value = "binary" , required = false) Attachment binaryDetail, @FormParam(value = "date")  LocalDate date, @FormParam(value = "dateTime")  java.util.Date dateTime, @FormParam(value = "password")  String password, @FormParam(value = "callback")  String paramCallback) {
        return delegate.testEndpointParameters(number, _double, patternWithoutDelimiter, _byte, integer, int32, int64, _float, string, binary, date, dateTime, password, paramCallback, securityContext);
    }

    @GET
    
    @Consumes({ "application/x-www-form-urlencoded" })
    
    @ApiOperation(value = "To test enum parameters", notes = "To test enum parameters", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Invalid request", response = Void.class),
        @ApiResponse(code = 404, message = "Not found", response = Void.class) })
    public Response testEnumParameters( @ApiParam(value = "Header parameter enum test (string array)" , allowableValues=">, $")@HeaderParam("enum_header_string_array") List<String> enumHeaderStringArray,  @ApiParam(value = "Header parameter enum test (string)" , allowableValues="_abc, -efg, (xyz)", defaultValue="-efg")@HeaderParam("enum_header_string") String enumHeaderString, @ApiParam(value = "Query parameter enum test (string array)")  @QueryParam("enum_query_string_array") List<String> enumQueryStringArray, @ApiParam(value = "Query parameter enum test (string)", allowableValues="_abc, -efg, (xyz)", defaultValue="-efg") @DefaultValue("-efg")  @QueryParam("enum_query_string") String enumQueryString, @ApiParam(value = "Query parameter enum test (double)", allowableValues="1, -2")  @QueryParam("enum_query_integer") Integer enumQueryInteger, @ApiParam(value = "Query parameter enum test (double)", allowableValues="1.1, -1.2")  @QueryParam("enum_query_double") Double enumQueryDouble, @FormParam(value = "enum_form_string_array")  List<String> enumFormStringArray, @FormParam(value = "enum_form_string")  String enumFormString) {
        return delegate.testEnumParameters(enumHeaderStringArray, enumHeaderString, enumQueryStringArray, enumQueryString, enumQueryInteger, enumQueryDouble, enumFormStringArray, enumFormString, securityContext);
    }

    @DELETE
    
    
    
    @ApiOperation(value = "Fake endpoint to test group parameters (optional)", notes = "Fake endpoint to test group parameters (optional)", response = Void.class, authorizations = {
        
        @Authorization(value = "bearer_test")
         }, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 400, message = "Something wrong", response = Void.class) })
    public Response testGroupParameters( @NotNull @ApiParam(value = "Required String in group parameters",required=true)  @QueryParam("required_string_group") Integer requiredStringGroup,  @NotNull  @ApiParam(value = "Required Boolean in group parameters" ,required=true)@HeaderParam("required_boolean_group") Boolean requiredBooleanGroup,  @NotNull @ApiParam(value = "Required Integer in group parameters",required=true)  @QueryParam("required_int64_group") Long requiredInt64Group, @ApiParam(value = "String in group parameters")  @QueryParam("string_group") Integer stringGroup,  @ApiParam(value = "Boolean in group parameters" )@HeaderParam("boolean_group") Boolean booleanGroup, @ApiParam(value = "Integer in group parameters")  @QueryParam("int64_group") Long int64Group) {
        return delegate.testGroupParameters(requiredStringGroup, requiredBooleanGroup, requiredInt64Group, stringGroup, booleanGroup, int64Group, securityContext);
    }

    @POST
    @Path("/inline-additionalProperties")
    @Consumes({ "application/json" })
    
    @ApiOperation(value = "test inline additionalProperties", notes = "", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response testInlineAdditionalProperties(@ApiParam(value = "request body" ,required=true) Map<String, String> requestBody) {
        return delegate.testInlineAdditionalProperties(requestBody, securityContext);
    }

    @GET
    @Path("/jsonFormData")
    @Consumes({ "application/x-www-form-urlencoded" })
    
    @ApiOperation(value = "test json serialization of form data", notes = "", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = Void.class) })
    public Response testJsonFormData(@FormParam(value = "param")  String param, @FormParam(value = "param2")  String param2) {
        return delegate.testJsonFormData(param, param2, securityContext);
    }

    @PUT
    @Path("/test-query-parameters")
    
    
    @ApiOperation(value = "", notes = "To test the collection format in query parameters", response = Void.class, tags={ "fake" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class) })
    public Response testQueryParameterCollectionFormat( @NotNull @ApiParam(value = "",required=true)  @QueryParam("pipe") List<String> pipe,  @NotNull @ApiParam(value = "",required=true)  @QueryParam("ioutil") List<String> ioutil,  @NotNull @ApiParam(value = "",required=true)  @QueryParam("http") List<String> http,  @NotNull @ApiParam(value = "",required=true)  @QueryParam("url") List<String> url,  @NotNull @ApiParam(value = "",required=true)  @QueryParam("context") List<String> context) {
        return delegate.testQueryParameterCollectionFormat(pipe, ioutil, http, url, context, securityContext);
    }

    @POST
    @Path("/{petId}/uploadImageWithRequiredFile")
    @Consumes({ "multipart/form-data" })
    @Produces({ "application/json" })
    @ApiOperation(value = "uploads an image (required)", notes = "", response = ModelApiResponse.class, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "write:pets", description = "modify pets in your account"),
            @AuthorizationScope(scope = "read:pets", description = "read your pets") })
         }, tags={ "pet" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "successful operation", response = ModelApiResponse.class) })
    public Response uploadFileWithRequiredFile(@ApiParam(value = "ID of pet to update",required=true) @PathParam("petId") Long petId,  @Multipart(value = "requiredFile" ) Attachment requiredFileDetail, @Multipart(value = "additionalMetadata", required = false)  String additionalMetadata) {
        return delegate.uploadFileWithRequiredFile(petId, requiredFileDetail, additionalMetadata, securityContext);
    }
}
