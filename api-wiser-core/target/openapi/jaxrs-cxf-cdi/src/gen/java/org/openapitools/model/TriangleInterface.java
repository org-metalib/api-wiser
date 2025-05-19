package org.openapitools.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import javax.validation.Valid;


import io.swagger.annotations.*;
import java.util.Objects;



public class TriangleInterface   {
  
  private String triangleType;

  /**
   **/
  public TriangleInterface triangleType(String triangleType) {
    this.triangleType = triangleType;
    return this;
  }

  
  @ApiModelProperty(required = true, value = "")
  @JsonProperty("triangleType")
  @NotNull
  public String getTriangleType() {
    return triangleType;
  }
  public void setTriangleType(String triangleType) {
    this.triangleType = triangleType;
  }



  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TriangleInterface triangleInterface = (TriangleInterface) o;
    return Objects.equals(this.triangleType, triangleInterface.triangleType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(triangleType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TriangleInterface {\n");
    
    sb.append("    triangleType: ").append(toIndentedString(triangleType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

