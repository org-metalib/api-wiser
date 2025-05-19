package org.openapitools.api;

import org.openapitools.api.*;
import org.openapitools.model.*;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

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

import java.util.List;

import java.io.InputStream;

import javax.validation.constraints.*;
import javax.validation.Valid;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSCXFCDIServerCodegen", date = "2025-05-18T20:12:30.067521-05:00[America/Chicago]", comments = "Generator version: 7.13.0")public interface FakeApiService {
      public Response fakeBigDecimalMap(SecurityContext securityContext);
      public Response fakeHealthGet(SecurityContext securityContext);
      public Response fakeOuterBooleanSerialize(Boolean body, SecurityContext securityContext);
      public Response fakeOuterCompositeSerialize(OuterComposite outerComposite, SecurityContext securityContext);
      public Response fakeOuterNumberSerialize(BigDecimal body, SecurityContext securityContext);
      public Response fakeOuterStringSerialize(String body, SecurityContext securityContext);
      public Response getArrayOfEnums(SecurityContext securityContext);
      public Response testBodyWithFileSchema(FileSchemaTestClass fileSchemaTestClass, SecurityContext securityContext);
      public Response testBodyWithQueryParams(String query, User user, SecurityContext securityContext);
      public Response testClientModel(Client client, SecurityContext securityContext);
      public Response testEndpointParameters(BigDecimal number, Double _double, String patternWithoutDelimiter, byte[] _byte, Integer integer, Integer int32, Long int64, Float _float, String string, Attachment binaryDetail, LocalDate date, java.util.Date dateTime, String password, String paramCallback, SecurityContext securityContext);
      public Response testEnumParameters(List<String> enumHeaderStringArray, String enumHeaderString, List<String> enumQueryStringArray, String enumQueryString, Integer enumQueryInteger, Double enumQueryDouble, List<String> enumFormStringArray, String enumFormString, SecurityContext securityContext);
      public Response testGroupParameters(Integer requiredStringGroup, Boolean requiredBooleanGroup, Long requiredInt64Group, Integer stringGroup, Boolean booleanGroup, Long int64Group, SecurityContext securityContext);
      public Response testInlineAdditionalProperties(Map<String, String> requestBody, SecurityContext securityContext);
      public Response testJsonFormData(String param, String param2, SecurityContext securityContext);
      public Response testQueryParameterCollectionFormat(List<String> pipe, List<String> ioutil, List<String> http, List<String> url, List<String> context, SecurityContext securityContext);
      public Response uploadFileWithRequiredFile(Long petId, Attachment requiredFileDetail, String additionalMetadata, SecurityContext securityContext);
}
